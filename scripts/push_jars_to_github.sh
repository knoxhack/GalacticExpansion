#!/bin/bash

# Simple script to push JAR files to GitHub
# Run this after a successful build

# Configuration
GIT_USERNAME=""
GIT_EMAIL=""
GIT_BRANCH="builds"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Preparing to push JAR files to GitHub...${NC}"

# Check if there are any JAR files to push
MAIN_JAR_COUNT=$(find ./build/libs -name "*.jar" | wc -l)
MODULE_JAR_COUNT=$(find ./*/build/libs -name "*.jar" | wc -l)

if [ "$MAIN_JAR_COUNT" -eq 0 ] && [ "$MODULE_JAR_COUNT" -eq 0 ]; then
    echo -e "${RED}No JAR files found. Run a build first.${NC}"
    exit 1
fi

# Set up Git if not already configured
if [ -z "$(git config user.name)" ]; then
    if [ -z "$GIT_USERNAME" ]; then
        read -p "Enter your Git username: " GIT_USERNAME
    fi
    git config user.name "$GIT_USERNAME"
fi

if [ -z "$(git config user.email)" ]; then
    if [ -z "$GIT_EMAIL" ]; then
        read -p "Enter your Git email: " GIT_EMAIL
    fi
    git config user.email "$GIT_EMAIL"
fi

# Check current branch and save it
CURRENT_BRANCH=$(git branch --show-current)
echo -e "${YELLOW}Current branch is: $CURRENT_BRANCH${NC}"

# Check if the builds branch exists
if ! git show-ref --verify --quiet refs/heads/$GIT_BRANCH; then
    echo -e "${YELLOW}Creating '$GIT_BRANCH' branch...${NC}"
    git checkout -b $GIT_BRANCH
else
    echo -e "${YELLOW}Switching to '$GIT_BRANCH' branch...${NC}"
    git checkout $GIT_BRANCH
fi

# Create directory structure
BUILD_DATE=$(date +%Y-%m-%d)
BUILD_TIME=$(date +%H-%M-%S)
ARTIFACTS_DIR="builds/$BUILD_DATE/$BUILD_TIME"

mkdir -p $ARTIFACTS_DIR/modules

# Copy main JARs
if [ "$MAIN_JAR_COUNT" -gt 0 ]; then
    echo -e "${GREEN}Copying main JAR files...${NC}"
    cp -v ./build/libs/*.jar $ARTIFACTS_DIR/
fi

# Copy module JARs
if [ "$MODULE_JAR_COUNT" -gt 0 ]; then
    echo -e "${GREEN}Copying module JAR files...${NC}"
    
    for MODULE_DIR in ./*/build/libs; do
        if [ -d "$MODULE_DIR" ]; then
            MODULE=$(echo $MODULE_DIR | awk -F/ '{print $(NF-2)}')
            mkdir -p "$ARTIFACTS_DIR/modules/$MODULE"
            cp -v $MODULE_DIR/*.jar "$ARTIFACTS_DIR/modules/$MODULE/"
        fi
    done
fi

# Create build info file
echo -e "${GREEN}Creating build info file...${NC}"
cat > "$ARTIFACTS_DIR/build_info.txt" << EOF
Build Date: $(date)
Branch: $CURRENT_BRANCH
Commit: $(git rev-parse HEAD)
Total JAR files: $(($MAIN_JAR_COUNT + $MODULE_JAR_COUNT))
EOF

# Add files to git
git add $ARTIFACTS_DIR

# Commit
echo -e "${GREEN}Committing build artifacts...${NC}"
git commit -m "Build artifacts: $BUILD_DATE $BUILD_TIME"

# Push to GitHub
echo -e "${GREEN}Pushing to GitHub...${NC}"
git push origin $GIT_BRANCH

# Return to original branch
echo -e "${GREEN}Returning to original branch: $CURRENT_BRANCH${NC}"
git checkout $CURRENT_BRANCH

echo -e "${GREEN}Done! Build artifacts pushed to $GIT_BRANCH branch.${NC}"
echo -e "${GREEN}View your artifacts at: https://github.com/[your-username]/[your-repo]/tree/$GIT_BRANCH/$ARTIFACTS_DIR${NC}"