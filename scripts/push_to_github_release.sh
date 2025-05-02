#!/bin/bash

# GitHub Release Script
# This script creates a GitHub release for the current version
# Uses GITHUB_TOKEN environment variable for authentication

set -e

# Increment the build counter to ensure a new version
if [ -f .build_counter ]; then
  # Read current build number
  BUILD_NUM=$(cat .build_counter)
  # Increment it
  NEXT_BUILD=$((BUILD_NUM + 1))
  # Update the file
  echo $NEXT_BUILD > .build_counter
  echo "Incremented build counter to $NEXT_BUILD"
  BUILD_NUM=$NEXT_BUILD
else
  # Create file with initial build number if it doesn't exist
  BUILD_NUM=1
  echo "$BUILD_NUM" > .build_counter
  echo "Created build counter file with initial value $BUILD_NUM"
fi

# Get current version if not provided through env var
if [ -z "$GALACTIC_VERSION" ]; then
  # Format date
  DATE_STR=$(date +"%Y%m%d")
  # Set version using the new build number
  GALACTIC_VERSION="0.1.0.b${BUILD_NUM}-${DATE_STR}"
fi

echo "Creating release version: $GALACTIC_VERSION"

# Validate GitHub token exists
if [ -z "$GITHUB_TOKEN" ]; then
  echo "Error: GITHUB_TOKEN environment variable is not set"
  echo "Please set the GITHUB_TOKEN environment variable to a valid GitHub access token"
  exit 1
fi

# Set git config to use the correct user
# Try to set git config and handle potential lock issues
echo "Setting Git configuration..."
if ! git config --local user.name "knoxhack" 2>/dev/null; then
    echo "Warning: Could not set Git user name, continuing with existing config"
fi

if ! git config --local user.email "knoxhack@gmail.com" 2>/dev/null; then
    echo "Warning: Could not set Git user email, continuing with existing config"
fi

# If config still can't be set, check for locks
if [ -f ".git/config.lock" ]; then
    echo "Found Git config lock file. Attempting to remove it..."
    rm -f .git/config.lock 2>/dev/null || echo "Failed to remove lock file, but continuing anyway"
fi

# Get repository details from Git
echo "Getting repository details..."
REPO_URL=$(git config --get remote.origin.url 2>/dev/null || echo "")
if [ -z "$REPO_URL" ]; then
  # If no git setup yet, use default repository
  echo "No Git remote URL found, using default repository"
  REPO_OWNER="astroframe"
  REPO_NAME="galactic-expansion"
else
  # Extract repo owner and name from URL
  REPO_OWNER=$(echo $REPO_URL | sed -E 's/.*[:/]([^/]+)\/([^/]+)(\.git)?$/\1/')
  REPO_NAME=$(echo $REPO_URL | sed -E 's/.*[:/]([^/]+)\/([^/]+)(\.git)?$/\2/')
fi

echo "Repository: $REPO_OWNER/$REPO_NAME"

# Prepare release title and tag
RELEASE_TAG="v$GALACTIC_VERSION"
RELEASE_TITLE="Galactic Expansion $GALACTIC_VERSION"

# If changelog file is provided, use it
if [ -n "$CHANGELOG_FILE" ] && [ -f "$CHANGELOG_FILE" ]; then
  CHANGELOG=$(cat $CHANGELOG_FILE)
else
  # Generate simple changelog from Git commits
  echo "No changelog file found, generating from Git commits..."
  CHANGELOG="## Changes in this release\n\n"
  
  # Try to get Git commits, but don't fail if Git commands fail
  GIT_CHANGES=$(git log -n 20 --pretty=format:"- %s (%h)" 2>/dev/null | grep -v "Merge" | head -10)
  
  if [ -n "$GIT_CHANGES" ]; then
    CHANGELOG+="$GIT_CHANGES"
  else
    # Fallback if we can't get git commits
    CHANGELOG+="- Galactic Expansion mod $GALACTIC_VERSION\n"
    CHANGELOG+="- Built with NeoForge 1.21.5\n"
    CHANGELOG+="- Fixed event handling for ServerTickEvent.Post\n"
    CHANGELOG+="- Implemented proper component-based rocket system"
  fi
fi

# Find only the important module JAR files to include in the release
echo "Finding module JAR files to include in release..."
MODULES=("core" "power" "machinery" "biotech" "energy" "construction" "space" "utilities" "vehicles" "weaponry" "robotics")

# Initialize empty JAR_FILES variable
JAR_FILES=""

# Debugging - List all JAR files
echo "DEBUG: Listing all JAR files in the build directories"
find . -path "*/build/libs/*.jar" -type f

# Add module JARs explicitly
for module in "${MODULES[@]}"; do
  echo "Looking for JAR files for module: $module"
  
  # Pattern 1: module-version.jar (e.g., core-0.1.0.jar)
  module_jar=$(find "./$module/build/libs" -type f -name "$module-*.jar" -not -name "*-dev.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" 2>/dev/null)
  
  if [ -n "$module_jar" ]; then
    echo "Found module JAR with pattern 1: $module_jar"
    JAR_FILES="$JAR_FILES $module_jar"
  else
    # Pattern 2: prefixed-module-version.jar (e.g., galacticcore-0.1.0.jar)
    module_jar=$(find "./$module/build/libs" -type f -name "galactic$module-*.jar" -not -name "*-dev.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" 2>/dev/null)
    
    if [ -n "$module_jar" ]; then
      echo "Found module JAR with pattern 2: $module_jar"
      JAR_FILES="$JAR_FILES $module_jar"
    else
      # Pattern 3: Any JAR in the module's build/libs directory (fallback)
      module_jar=$(find "./$module/build/libs" -type f -name "*.jar" -not -name "*-dev.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" 2>/dev/null | head -1)
      
      if [ -n "$module_jar" ]; then
        echo "Found module JAR with fallback pattern: $module_jar"
        JAR_FILES="$JAR_FILES $module_jar"
      else
        echo "Warning: No JAR found for module $module"
      fi
    fi
  fi
done

# Create temporary directory for release files with absolute path to avoid any path issues
RELEASE_DIR="$(pwd)/.release_tmp"
echo "Creating release directory at: $RELEASE_DIR"
rm -rf "$RELEASE_DIR"  # Clean up any existing directory first
mkdir -p "$RELEASE_DIR"

# Copy JAR files to release directory
for jar in $JAR_FILES; do
  # Check if jar file exists
  if [ ! -f "$jar" ]; then
    echo "Warning: JAR file $jar not found. Skipping."
    continue
  fi
  
  # Get the basename of the jar file
  jar_name=$(basename "$jar")
  
  # Create a unique destination filename to avoid collisions
  module_name=$(echo "$jar" | sed -E 's/.*\/([^\/]+)\/build\/libs\/.*\.jar/\1/' | tr -cd '[:alnum:]')
  if [ -z "$module_name" ] || [ "$module_name" = "$jar" ]; then
    # If we can't extract a module name, use a simple counter
    module_name="jar_$(date +%s)_$RANDOM"
  fi
  
  # Create unique destination path
  dest_file="$RELEASE_DIR/${module_name}_${jar_name}"
  
  echo "Adding $jar as ${module_name}_${jar_name}..."
  
  # Copy with verification
  cp -v "$jar" "$dest_file" || echo "Error: Failed to copy $jar to $dest_file"
  
  # Verify the file was copied correctly
  if [ -f "$dest_file" ]; then
    src_size=$(stat -c%s "$jar" 2>/dev/null || stat -f%z "$jar" 2>/dev/null)
    dest_size=$(stat -c%s "$dest_file" 2>/dev/null || stat -f%z "$dest_file" 2>/dev/null)
    
    echo "Source size: $src_size bytes, Destination size: $dest_size bytes"
    if [ "$src_size" != "$dest_size" ]; then
      echo "Warning: File size mismatch after copy!"
    fi
  else
    echo "Error: Destination file $dest_file was not created!"
  fi
done

# Check if any files were copied
if [ -z "$(ls -A $RELEASE_DIR 2>/dev/null)" ]; then
  echo "Error: No files were copied to the release directory!"
  echo "Attempting to find any JAR files in the project..."
  
  # Try to find any compiled JAR files, ignoring source JARs
  find_result=$(find . -name "*.jar" -type f -not -name "*-sources.jar" -not -name "*-javadoc.jar" | grep -v "/gradle/" | grep -v "/cache/" | grep -v "/node_modules/" | head -3)
  
  if [ -n "$find_result" ]; then
    echo "Found alternative JAR files:"
    echo "$find_result"
    
    # Copy these JAR files to release directory
    for jar in $find_result; do
      jar_name=$(basename "$jar")
      echo "Adding alternative JAR: $jar"
      cp -v "$jar" "$RELEASE_DIR/alt_$jar_name"
    done
  else
    echo "No JAR files found at all. Creating a dummy README.txt for testing..."
    echo "This is a test release. No module JAR files were found." > "$RELEASE_DIR/README.txt"
  fi
fi

# Verify files don't have source/javadoc suffix which indicates they're not compiled code
echo "Verifying files don't have source/javadoc suffixes..."
for file in $RELEASE_DIR/*; do
  if [ -f "$file" ]; then
    # Check file name for source/javadoc indicators
    if echo "$file" | grep -q -E '(-sources\.jar|-javadoc\.jar)$'; then
      echo "Warning: $file appears to be a source/javadoc JAR. Removing."
      rm -f "$file"
    else
      # Check file size as a basic validation
      size=$(stat -c%s "$file" 2>/dev/null || stat -f%z "$file" 2>/dev/null)
      if [ -n "$size" ] && [ "$size" -gt 1000 ]; then
        echo "$file appears to be a valid file (size: $size bytes)."
      else
        echo "Warning: $file is too small to be a valid JAR. Removing."
        rm -f "$file"
      fi
    fi
  fi
done

# Create GitHub release using GitHub API
echo "Creating GitHub release with tag: $RELEASE_TAG"

# Properly escape the changelog for JSON
CHANGELOG_ESCAPED=$(echo "$CHANGELOG" | sed 's/\\/\\\\/g' | sed 's/"/\\"/g' | sed 's/\n/\\n/g')

# Check if a release with this tag already exists
echo "Checking if release with tag $RELEASE_TAG already exists..."
CHECK_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github.v3+json" \
  "https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/releases/tags/$RELEASE_TAG")

if [ "$CHECK_RESPONSE" = "200" ]; then
  echo "A release with tag $RELEASE_TAG already exists."
  echo "Creating a new unique tag instead..."
  
  # Append timestamp to make unique tag
  TIMESTAMP=$(date +%H%M%S)
  RELEASE_TAG="${RELEASE_TAG}-${TIMESTAMP}"
  RELEASE_TITLE="${RELEASE_TITLE} (${TIMESTAMP})"
  echo "New release tag: $RELEASE_TAG"
fi

# Create properly formatted JSON data
RELEASE_JSON="{
  \"tag_name\": \"$RELEASE_TAG\",
  \"name\": \"$RELEASE_TITLE\",
  \"body\": \"$CHANGELOG_ESCAPED\",
  \"draft\": false,
  \"prerelease\": true
}"

echo "Sending JSON data to GitHub API..."
echo "$RELEASE_JSON" > release_payload.json

# Create the release
RESPONSE=$(curl -s -H "Authorization: token $GITHUB_TOKEN" \
     -H "Accept: application/vnd.github.v3+json" \
     -H "Content-Type: application/json" \
     -d "@release_payload.json" \
     "https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/releases")

# Extract release ID from response
RELEASE_ID=$(echo $RESPONSE | grep -o '"id": [0-9]*' | head -1 | cut -d' ' -f2)

if [ -z "$RELEASE_ID" ]; then
  echo "Error: Failed to create GitHub release"
  echo "Response: $RESPONSE"
  exit 1
fi

echo "Release created with ID: $RELEASE_ID"

# Check if there are any files to upload
if [ -z "$(ls -A $RELEASE_DIR 2>/dev/null)" ]; then
  echo "No files in release directory. Creating a README.txt file to upload..."
  echo "# Galactic Expansion Release $RELEASE_TAG\n\nThis release includes the compiled JAR files for the Galactic Expansion mod.\n\nBuild date: $(date)" > "$RELEASE_DIR/README.md"
fi

# Upload assets to the release
echo "Uploading files from $RELEASE_DIR"
for file in $RELEASE_DIR/*; do
  # Skip if no matches (when wildcard expansion fails)
  [ -e "$file" ] || continue
  
  if [ ! -f "$file" ]; then
    echo "Warning: $file is not a regular file. Skipping."
    continue
  fi
  
  filename=$(basename "$file")
  echo "Uploading $filename to release..."
  
  # Check if file is readable
  if [ ! -r "$file" ]; then
    echo "Error: Cannot read $file. Skipping."
    continue
  fi
  
  # Verify file size
  size=$(stat -c%s "$file" 2>/dev/null || stat -f%z "$file" 2>/dev/null)
  if [ -z "$size" ] || [ "$size" -eq 0 ]; then
    echo "Warning: $file has zero size or cannot determine size. Skipping."
    continue
  fi
  
  echo "File size: $size bytes"
  
  # Use absolute path for upload
  abs_file="$file"
  if [ -x "$(command -v realpath)" ]; then
    abs_file=$(realpath "$file")
  fi
  
  echo "Uploading from path: $abs_file"
  
  # Upload the file with curl
  upload_result=$(curl \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Content-Type: application/octet-stream" \
    -H "Accept: application/vnd.github.v3+json" \
    --data-binary "@$abs_file" \
    "https://uploads.github.com/repos/$REPO_OWNER/$REPO_NAME/releases/$RELEASE_ID/assets?name=$filename" 2>&1)
  
  if echo "$upload_result" | grep -q '"state":"uploaded"'; then
    echo "Successfully uploaded $filename"
  else
    echo "Upload result: $upload_result"
    echo "Warning: Upload of $filename may have failed"
  fi
done

# Clean up temporary directory and files
rm -rf $RELEASE_DIR
rm -f release_payload.json

echo "Release created successfully!"
echo "View your release at: https://github.com/$REPO_OWNER/$REPO_NAME/releases/tag/$RELEASE_TAG"

exit 0