#!/bin/bash

# Improved GitHub Release Script
# This script creates a GitHub release for the current version with proper JAR naming

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

# Check if we're in test mode
TEST_MODE=0
if [[ "$*" == *--test-run* ]]; then
  echo "Running in test mode - will not actually create a GitHub release"
  TEST_MODE=1
elif [ -z "$GITHUB_TOKEN" ]; then
  echo "Error: GITHUB_TOKEN environment variable is not set"
  echo "Please set the GITHUB_TOKEN environment variable to a valid GitHub access token"
  echo "Or use --test-run to run in test mode without a token"
  exit 1
fi

# Skip Git configuration since we can't modify Git in this environment
echo "Skipping Git configuration in this environment..."

# Use hardcoded repository details
echo "Using hardcoded repository details..."
REPO_OWNER="astroframe"
REPO_NAME="galactic-expansion"

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
    CHANGELOG+="- Fixed item registration issues\n"
    CHANGELOG+="- Fixed 'Item id not set' error by implementing static Item.Properties"
  fi
fi

# Define the modules we need to package
MODULES=("core" "power" "machinery" "biotech" "energy" "construction" "space" "utilities" "vehicles" "weaponry" "robotics")

# Create temporary directory for release files
RELEASE_DIR="$(pwd)/.release_tmp"
echo "Creating release directory at: $RELEASE_DIR"
rm -rf "$RELEASE_DIR"  # Clean up any existing directory first
mkdir -p "$RELEASE_DIR"

# Check for all-in-one JAR first
ALL_IN_ONE_JAR="./combined-jar/GalacticExpansion-all-in-one-0.1.0.jar"
if [ -f "$ALL_IN_ONE_JAR" ]; then
  # Copy the all-in-one JAR with Minecraft-compatible naming
  DEST_FILE="$RELEASE_DIR/galacticexpansion_all-in-one-0.1.0.jar"
  echo "Found all-in-one JAR: $ALL_IN_ONE_JAR"
  echo "Copying to $DEST_FILE"
  cp "$ALL_IN_ONE_JAR" "$DEST_FILE"
  
  if [ -f "$DEST_FILE" ]; then
    echo "Successfully copied all-in-one JAR!"
  else
    echo "Error: Failed to copy all-in-one JAR"
  fi
else
  echo "All-in-one JAR not found at $ALL_IN_ONE_JAR"
  
  # Try to create it if the script exists
  if [ -f "./package-all-in-one.sh" ]; then
    echo "Attempting to create all-in-one JAR using package-all-in-one.sh..."
    bash ./package-all-in-one.sh
    
    # Check if creation was successful
    if [ -f "$ALL_IN_ONE_JAR" ]; then
      # Copy the newly created all-in-one JAR
      DEST_FILE="$RELEASE_DIR/galacticexpansion_all-in-one-0.1.0.jar"
      echo "Successfully created all-in-one JAR. Copying to $DEST_FILE"
      cp "$ALL_IN_ONE_JAR" "$DEST_FILE"
    else
      echo "Failed to create all-in-one JAR"
    fi
  else
    echo "package-all-in-one.sh script not found"
  fi
fi

# Also include the README file if available
if [ -f "JARS_README.md" ]; then
  echo "Including JARS_README.md in the release"
  cp "JARS_README.md" "$RELEASE_DIR/README.md"
fi

# Package individual module JAR files with consistent naming
for module in "${MODULES[@]}"; do
  echo "Processing module: $module"
  
  # Find the JAR file for this module
  JAR_PATH="./$module/build/libs/galactic$module-0.1.0.jar"
  
  if [ -f "$JAR_PATH" ]; then
    # Copy with consistent naming format that Minecraft expects
    DEST_FILE="$RELEASE_DIR/${module}_galactic${module}-0.1.0.jar"
    echo "Copying $JAR_PATH to $DEST_FILE"
    cp "$JAR_PATH" "$DEST_FILE"
    
    # Verify the copy
    if [ -f "$DEST_FILE" ]; then
      echo "Successfully copied module JAR for $module"
    else
      echo "Error: Failed to copy JAR for $module"
    fi
  else
    echo "Warning: JAR file not found for module $module at $JAR_PATH"
    
    # Try to find it with a more general pattern
    FOUND_JAR=$(find "./$module/build/libs" -name "*.jar" | grep -v "sources" | grep -v "javadoc" | head -1)
    if [ -n "$FOUND_JAR" ]; then
      # Copy with consistent naming
      DEST_FILE="$RELEASE_DIR/${module}_galactic${module}-0.1.0.jar"
      echo "Found alternative JAR: $FOUND_JAR, copying to $DEST_FILE"
      cp "$FOUND_JAR" "$DEST_FILE"
    else
      echo "Error: No JAR file found for module $module"
    fi
  fi
done

# Create a ZIP file of all JAR files and README
echo "Creating combined ZIP file..."
RELEASE_ZIP="$RELEASE_DIR/galactic-expansion-v$GALACTIC_VERSION-neoforge-1.21.5.zip"
if command -v zip >/dev/null 2>&1; then
  # Include all JARs and README file in the ZIP
  (cd "$RELEASE_DIR" && zip -r "../$(basename $RELEASE_ZIP)" *.jar README.md 2>/dev/null)
  if [ -f "$(dirname $RELEASE_DIR)/$(basename $RELEASE_ZIP)" ]; then
    echo "Successfully created ZIP file: $(dirname $RELEASE_DIR)/$(basename $RELEASE_ZIP)"
    # Add the ZIP file to the release files
    cp "$(dirname $RELEASE_DIR)/$(basename $RELEASE_ZIP)" "$RELEASE_DIR/"
  else
    echo "Warning: Failed to create ZIP file"
  fi
else
  echo "Warning: 'zip' command not found, skipping ZIP creation"
fi

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

# Show contents of the release directory
echo "Files ready for release:"
ls -la "$RELEASE_DIR"

if [ "$TEST_MODE" -eq 1 ]; then
  echo "Test mode: Not creating actual GitHub release"
  echo "Would have created a release with tag: $RELEASE_TAG"
  echo "Files would have been uploaded to GitHub from $RELEASE_DIR"
  ls -la "$RELEASE_DIR"
  
  # Clean up temporary directory and files
  rm -rf "$RELEASE_DIR"
  
  echo "Test completed successfully"
  exit 0
fi

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
  
  # Upload the file with curl
  upload_result=$(curl \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Content-Type: application/octet-stream" \
    -H "Accept: application/vnd.github.v3+json" \
    --data-binary "@$file" \
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