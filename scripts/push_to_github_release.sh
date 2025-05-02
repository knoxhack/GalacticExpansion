#!/bin/bash

# GitHub Release Script
# This script creates a GitHub release for the current version
# Uses GITHUB_TOKEN environment variable for authentication

set -e

# Get current version if not provided through env var
if [ -z "$GALACTIC_VERSION" ]; then
  # Read the current build number
  BUILD_NUM=$(cat .build_counter 2>/dev/null || echo "1")
  # Format date
  DATE_STR=$(date +"%Y%m%d")
  # Set version
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

# Add module JARs explicitly
for module in "${MODULES[@]}"; do
  # Look for the specific module JAR in the build/libs directory
  module_jar=$(find "./$module/build/libs" -name "$module*.jar" -not -name "*-dev.jar" -not -name "*-sources.jar" 2>/dev/null)
  
  if [ -n "$module_jar" ]; then
    echo "Found module JAR: $module_jar"
    JAR_FILES="$JAR_FILES $module_jar"
  else
    echo "Warning: No JAR found for module $module"
  fi
done

# Create temporary directory for release files with absolute path to avoid any path issues
RELEASE_DIR="$(pwd)/.release_tmp"
echo "Creating release directory at: $RELEASE_DIR"
rm -rf "$RELEASE_DIR"  # Clean up any existing directory first
mkdir -p "$RELEASE_DIR"

# Copy JAR files to release directory
for jar in $JAR_FILES; do
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
  cp "$jar" "$dest_file"
done

# Create GitHub release using GitHub API
echo "Creating GitHub release with tag: $RELEASE_TAG"

# Properly escape the changelog for JSON
CHANGELOG_ESCAPED=$(echo "$CHANGELOG" | sed 's/\\/\\\\/g' | sed 's/"/\\"/g' | sed 's/\n/\\n/g')

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
for file in $RELEASE_DIR/*; do
  filename=$(basename "$file")
  echo "Uploading $filename to release..."
  
  curl -s \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Content-Type: application/java-archive" \
    -H "Accept: application/vnd.github.v3+json" \
    --data-binary @"$file" \
    "https://uploads.github.com/repos/$REPO_OWNER/$REPO_NAME/releases/$RELEASE_ID/assets?name=$filename"
  
  echo ""
done

# Clean up temporary directory and files
rm -rf $RELEASE_DIR
rm -f release_payload.json

echo "Release created successfully!"
echo "View your release at: https://github.com/$REPO_OWNER/$REPO_NAME/releases/tag/$RELEASE_TAG"

exit 0