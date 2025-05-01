#!/bin/bash

# Script to create a new GitHub release and upload JAR files
# Requires: curl, jq (for JSON parsing)

# Configuration
GIT_USERNAME="knoxhack"
GIT_REPO="knoxhack/GalacticExpansion"
RELEASE_PREFIX="GalacticExpansion-Build"

# Use GitHub token from environment variable
if [ -z "$GITHUB_TOKEN" ]; then
    echo "Error: GITHUB_TOKEN environment variable is required."
    echo "Please create a personal access token with 'repo' scope at https://github.com/settings/tokens/new"
    exit 1
fi

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Preparing to create a GitHub release and upload JAR files...${NC}"

# Check if there are any JAR files to push
MAIN_JAR_COUNT=$(find ./build/libs -name "*.jar" | wc -l)
MODULE_JAR_COUNT=$(find ./*/build/libs -name "*.jar" | wc -l)

if [ "$MAIN_JAR_COUNT" -eq 0 ] && [ "$MODULE_JAR_COUNT" -eq 0 ]; then
    echo -e "${RED}No JAR files found. Run a build first.${NC}"
    exit 1
fi

# Create a temporary directory for the release assets
TEMP_DIR=$(mktemp -d)
echo -e "${YELLOW}Created temporary directory: $TEMP_DIR${NC}"

# Build date and time for the release tag
BUILD_DATE=$(date +%Y-%m-%d)
BUILD_TIME=$(date +%H-%M-%S)
RELEASE_TAG="${RELEASE_PREFIX}-${BUILD_DATE}-${BUILD_TIME}"
RELEASE_NAME="Galactic Expansion Build ${BUILD_DATE} ${BUILD_TIME}"

# Get the current branch and commit
CURRENT_BRANCH=$(git branch --show-current)
CURRENT_COMMIT=$(git rev-parse HEAD)

# Create a zip file with main JAR files
if [ "$MAIN_JAR_COUNT" -gt 0 ]; then
    echo -e "${GREEN}Packaging main JAR files...${NC}"
    mkdir -p $TEMP_DIR/main
    cp -v ./build/libs/*.jar $TEMP_DIR/main/
    (cd $TEMP_DIR && zip -r "main-jars.zip" main/)
fi

# Create a zip file for each module
if [ "$MODULE_JAR_COUNT" -gt 0 ]; then
    echo -e "${GREEN}Packaging module JAR files...${NC}"
    mkdir -p $TEMP_DIR/modules
    
    for MODULE_DIR in ./*/build/libs; do
        if [ -d "$MODULE_DIR" ]; then
            MODULE=$(echo $MODULE_DIR | awk -F/ '{print $(NF-2)}')
            echo -e "${GREEN}Packaging module: $MODULE${NC}"
            mkdir -p $TEMP_DIR/modules/$MODULE
            cp -v $MODULE_DIR/*.jar $TEMP_DIR/modules/$MODULE/
        fi
    done
    
    (cd $TEMP_DIR && zip -r "module-jars.zip" modules/)
fi

# Create a combined zip with all JARs
echo -e "${GREEN}Creating combined package...${NC}"
(cd $TEMP_DIR && zip -r "all-jars.zip" main/ modules/ 2>/dev/null)

# Create build info file
echo -e "${GREEN}Creating build info file...${NC}"
cat > "$TEMP_DIR/build_info.txt" << EOF
Build Date: $(date)
Branch: $CURRENT_BRANCH
Commit: $CURRENT_COMMIT
Total JAR files: $(($MAIN_JAR_COUNT + $MODULE_JAR_COUNT))
EOF

# Create the release on GitHub
echo -e "${GREEN}Creating GitHub release: $RELEASE_TAG${NC}"
RELEASE_NOTES="Galactic Expansion Build $BUILD_DATE $BUILD_TIME\n\nBuild Info:\n- Branch: $CURRENT_BRANCH\n- Commit: $CURRENT_COMMIT\n- Total JAR files: $(($MAIN_JAR_COUNT + $MODULE_JAR_COUNT))"

# Create the release using GitHub API
RELEASE_RESPONSE=$(curl -s -X POST \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github.v3+json" \
  -d "{\"tag_name\":\"$RELEASE_TAG\",\"name\":\"$RELEASE_NAME\",\"body\":\"$RELEASE_NOTES\",\"draft\":false,\"prerelease\":true}" \
  "https://api.github.com/repos/$GIT_REPO/releases")

# Extract the release ID from the response
RELEASE_ID=$(echo $RELEASE_RESPONSE | grep -o '"id": [0-9]*' | head -1 | awk '{print $2}')

if [ -z "$RELEASE_ID" ]; then
    echo -e "${RED}Failed to create release. API response:${NC}"
    echo $RELEASE_RESPONSE
    rm -rf $TEMP_DIR
    exit 1
fi

echo -e "${GREEN}Release created with ID: $RELEASE_ID${NC}"

# Upload the zip files
echo -e "${GREEN}Uploading assets to the release...${NC}"

# Helper function to upload a file to the release
upload_asset() {
    local file=$1
    local filename=$(basename $file)
    
    echo -e "${YELLOW}Uploading $filename...${NC}"
    
    UPLOAD_RESPONSE=$(curl -s -X POST \
      -H "Authorization: token $GITHUB_TOKEN" \
      -H "Accept: application/vnd.github.v3+json" \
      -H "Content-Type: application/zip" \
      --data-binary @"$file" \
      "https://uploads.github.com/repos/$GIT_REPO/releases/$RELEASE_ID/assets?name=$filename")
    
    # Check if upload was successful
    if echo $UPLOAD_RESPONSE | grep -q '"state": "uploaded"'; then
        echo -e "${GREEN}Successfully uploaded $filename${NC}"
    else
        echo -e "${RED}Failed to upload $filename. Response:${NC}"
        echo $UPLOAD_RESPONSE
    fi
}

# Upload the zip files
if [ -f "$TEMP_DIR/main-jars.zip" ]; then
    upload_asset "$TEMP_DIR/main-jars.zip"
fi

if [ -f "$TEMP_DIR/module-jars.zip" ]; then
    upload_asset "$TEMP_DIR/module-jars.zip"
fi

if [ -f "$TEMP_DIR/all-jars.zip" ]; then
    upload_asset "$TEMP_DIR/all-jars.zip"
fi

# Upload main JAR file directly (if only one exists)
if [ "$MAIN_JAR_COUNT" -eq 1 ]; then
    MAIN_JAR=$(find ./build/libs -name "*.jar" | head -1)
    if [ -n "$MAIN_JAR" ]; then
        echo -e "${YELLOW}Uploading main JAR file directly: $(basename $MAIN_JAR)${NC}"
        
        UPLOAD_RESPONSE=$(curl -s -X POST \
          -H "Authorization: token $GITHUB_TOKEN" \
          -H "Accept: application/vnd.github.v3+json" \
          -H "Content-Type: application/java-archive" \
          --data-binary @"$MAIN_JAR" \
          "https://uploads.github.com/repos/$GIT_REPO/releases/$RELEASE_ID/assets?name=$(basename $MAIN_JAR)")
        
        if echo $UPLOAD_RESPONSE | grep -q '"state": "uploaded"'; then
            echo -e "${GREEN}Successfully uploaded $(basename $MAIN_JAR)${NC}"
        else
            echo -e "${RED}Failed to upload $(basename $MAIN_JAR)${NC}"
        fi
    fi
fi

# Upload build info
if [ -f "$TEMP_DIR/build_info.txt" ]; then
    echo -e "${YELLOW}Uploading build_info.txt...${NC}"
    
    UPLOAD_RESPONSE=$(curl -s -X POST \
      -H "Authorization: token $GITHUB_TOKEN" \
      -H "Accept: application/vnd.github.v3+json" \
      -H "Content-Type: text/plain" \
      --data-binary @"$TEMP_DIR/build_info.txt" \
      "https://uploads.github.com/repos/$GIT_REPO/releases/$RELEASE_ID/assets?name=build_info.txt")
    
    if echo $UPLOAD_RESPONSE | grep -q '"state": "uploaded"'; then
        echo -e "${GREEN}Successfully uploaded build_info.txt${NC}"
    else
        echo -e "${RED}Failed to upload build_info.txt${NC}"
    fi
fi

# Clean up
echo -e "${GREEN}Cleaning up...${NC}"
rm -rf $TEMP_DIR

echo -e "${GREEN}Done! Release created and assets uploaded.${NC}"
echo -e "${GREEN}View your release at: https://github.com/$GIT_REPO/releases/tag/$RELEASE_TAG${NC}"