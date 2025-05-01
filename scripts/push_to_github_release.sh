#!/bin/bash

# Script to create a new GitHub release and upload JAR files
# Requires: curl, jq (for JSON parsing)

# Configuration
GIT_USERNAME="knoxhack"
GIT_REPO="knoxhack/GalacticExpansion"
VERSION_BASE="0.1.0"

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

# Auto-incrementing build number
BUILD_COUNTER_FILE=".build_counter"
if [ -f "$BUILD_COUNTER_FILE" ]; then
    BUILD_NUMBER=$(cat "$BUILD_COUNTER_FILE")
    BUILD_NUMBER=$((BUILD_NUMBER + 1))
else
    BUILD_NUMBER=1
fi
echo "$BUILD_NUMBER" > "$BUILD_COUNTER_FILE"

# Create version with date and build number
BUILD_DATE=$(date +%Y%m%d)
VERSION="${VERSION_BASE}.b${BUILD_NUMBER}-${BUILD_DATE}"

# Create a descriptive release name
RELEASE_NAME="Galactic Expansion ${VERSION}"
RELEASE_TAG="v${VERSION}"

echo -e "${GREEN}Creating release version: ${VERSION}${NC}"

# Get the current branch and commit
CURRENT_BRANCH=$(git branch --show-current)
CURRENT_COMMIT=$(git rev-parse HEAD)

# Create build info file
echo -e "${GREEN}Creating build info file...${NC}"
BUILD_INFO_FILE="build_info.txt"
cat > "$BUILD_INFO_FILE" << EOF
Galactic Expansion Build Information
===================================
Version: ${VERSION}
Build Number: ${BUILD_NUMBER}
Build Date: $(date)
Branch: $CURRENT_BRANCH
Commit: $CURRENT_COMMIT
Total JAR files: $(($MAIN_JAR_COUNT + $MODULE_JAR_COUNT))

Modules Included:
EOF

# Add list of all modules to build_info
for MODULE_DIR in ./*/build/libs; do
    if [ -d "$MODULE_DIR" ]; then
        MODULE=$(echo $MODULE_DIR | awk -F/ '{print $(NF-2)}')
        echo "- $MODULE" >> "$BUILD_INFO_FILE"
    fi
done

# Create the release on GitHub
echo -e "${GREEN}Creating GitHub release: $RELEASE_TAG${NC}"
RELEASE_NOTES="Galactic Expansion ${VERSION}\n\nBuild #${BUILD_NUMBER} (${BUILD_DATE})\n\nBuild Info:\n- Branch: $CURRENT_BRANCH\n- Commit: $CURRENT_COMMIT\n- Total JAR files: $(($MAIN_JAR_COUNT + $MODULE_JAR_COUNT))"

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
    rm -f "$BUILD_INFO_FILE"
    exit 1
fi

echo -e "${GREEN}Release created with ID: $RELEASE_ID${NC}"

# Upload assets to the release
echo -e "${GREEN}Uploading assets to the release...${NC}"

# Helper function to upload a file to the release
upload_asset() {
    local file=$1
    local asset_name=$2
    local content_type=$3
    
    if [ -z "$asset_name" ]; then
        asset_name=$(basename "$file")
    fi
    
    if [ -z "$content_type" ]; then
        if [[ "$file" == *.jar ]]; then
            content_type="application/java-archive"
        elif [[ "$file" == *.zip ]]; then
            content_type="application/zip"
        elif [[ "$file" == *.txt ]]; then
            content_type="text/plain"
        else
            content_type="application/octet-stream"
        fi
    fi
    
    echo -e "${YELLOW}Uploading $asset_name...${NC}"
    
    UPLOAD_RESPONSE=$(curl -s -X POST \
      -H "Authorization: token $GITHUB_TOKEN" \
      -H "Accept: application/vnd.github.v3+json" \
      -H "Content-Type: $content_type" \
      --data-binary @"$file" \
      "https://uploads.github.com/repos/$GIT_REPO/releases/$RELEASE_ID/assets?name=$asset_name")
    
    # Check if upload was successful
    if echo $UPLOAD_RESPONSE | grep -q '"state": "uploaded"'; then
        echo -e "${GREEN}Successfully uploaded $asset_name${NC}"
        return 0
    else
        echo -e "${RED}Failed to upload $asset_name. Response:${NC}"
        echo $UPLOAD_RESPONSE
        return 1
    fi
}

# Upload main JAR with versioned filename
if [ "$MAIN_JAR_COUNT" -gt 0 ]; then
    for MAIN_JAR in $(find ./build/libs -name "*.jar"); do
        # Extract original name and add version
        ORIGINAL_NAME=$(basename "$MAIN_JAR")
        EXTENSION="${ORIGINAL_NAME##*.}"
        NAME_WITHOUT_EXT="${ORIGINAL_NAME%.*}"
        
        # Create a versioned name
        VERSIONED_NAME="${NAME_WITHOUT_EXT}-${VERSION}.${EXTENSION}"
        
        echo -e "${GREEN}Uploading main JAR as $VERSIONED_NAME${NC}"
        upload_asset "$MAIN_JAR" "$VERSIONED_NAME"
    done
fi

# Upload all module JARs with versioned filenames
for MODULE_DIR in ./*/build/libs; do
    if [ -d "$MODULE_DIR" ]; then
        MODULE=$(echo $MODULE_DIR | awk -F/ '{print $(NF-2)}')
        echo -e "${GREEN}Processing module: $MODULE${NC}"
        
        for JAR_FILE in $(find "$MODULE_DIR" -name "*.jar"); do
            # Extract original name and add version
            ORIGINAL_NAME=$(basename "$JAR_FILE")
            EXTENSION="${ORIGINAL_NAME##*.}"
            NAME_WITHOUT_EXT="${ORIGINAL_NAME%.*}"
            
            # Create a versioned name that includes the module
            VERSIONED_NAME="galactic${MODULE}-${VERSION}.${EXTENSION}"
            
            echo -e "${GREEN}Uploading $MODULE JAR as $VERSIONED_NAME${NC}"
            upload_asset "$JAR_FILE" "$VERSIONED_NAME"
        done
    fi
done

# Create a combined zip with all JARs
echo -e "${GREEN}Creating combined package...${NC}"
ZIP_FILE="GalacticExpansion-${VERSION}-all.zip"
TEMP_DIR=$(mktemp -d)

# Copy main JARs
if [ "$MAIN_JAR_COUNT" -gt 0 ]; then
    mkdir -p "$TEMP_DIR/main"
    cp -v ./build/libs/*.jar "$TEMP_DIR/main/"
fi

# Copy module JARs
if [ "$MODULE_JAR_COUNT" -gt 0 ]; then
    mkdir -p "$TEMP_DIR/modules"
    
    for MODULE_DIR in ./*/build/libs; do
        if [ -d "$MODULE_DIR" ]; then
            MODULE=$(echo $MODULE_DIR | awk -F/ '{print $(NF-2)}')
            mkdir -p "$TEMP_DIR/modules/$MODULE"
            cp -v "$MODULE_DIR"/*.jar "$TEMP_DIR/modules/$MODULE/"
        fi
    done
fi

# Create zip
(cd "$TEMP_DIR" && zip -r "$ZIP_FILE" main/ modules/ 2>/dev/null)

# Upload combined zip
if [ -f "$TEMP_DIR/$ZIP_FILE" ]; then
    upload_asset "$TEMP_DIR/$ZIP_FILE" "$ZIP_FILE"
fi

# Upload build info
upload_asset "$BUILD_INFO_FILE" "build_info-${VERSION}.txt" "text/plain"

# Clean up
echo -e "${GREEN}Cleaning up...${NC}"
rm -rf "$TEMP_DIR"
rm -f "$BUILD_INFO_FILE"

echo -e "${GREEN}Done! Release v${VERSION} created and assets uploaded.${NC}"
echo -e "${GREEN}View your release at: https://github.com/$GIT_REPO/releases/tag/$RELEASE_TAG${NC}"