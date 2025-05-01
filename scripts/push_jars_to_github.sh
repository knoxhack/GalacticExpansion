#!/bin/bash

# Simple script to push JAR files to GitHub
# Run this after a successful build

# Configuration
GIT_USERNAME="knoxhack"
GIT_EMAIL="knoxhack@users.noreply.github.com"
GIT_REPO="knoxhack/GalacticExpansion"
GIT_BRANCH="builds"

# Use GitHub token from environment variable if available
GITHUB_TOKEN=${GITHUB_TOKEN:-""}

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

# Save current working directory and branch
CURRENT_DIR=$(pwd)
CURRENT_BRANCH=$(git branch --show-current)
echo -e "${YELLOW}Current branch is: $CURRENT_BRANCH${NC}"
echo -e "${YELLOW}Current directory: $CURRENT_DIR${NC}"

# Create a temporary directory for the clone
TEMP_DIR=$(mktemp -d)
echo -e "${YELLOW}Created temporary directory: $TEMP_DIR${NC}"

# Attempt to clone the repository with the token
if [ -n "$GITHUB_TOKEN" ]; then
    echo -e "${GREEN}Cloning repository using GitHub token...${NC}"
    git clone https://$GIT_USERNAME:$GITHUB_TOKEN@github.com/$GIT_REPO.git $TEMP_DIR
else
    echo -e "${YELLOW}No GitHub token found, trying without authentication...${NC}"
    git clone https://github.com/$GIT_REPO.git $TEMP_DIR
fi

# Check if clone was successful
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to clone the repository. Please check your credentials and try again.${NC}"
    rm -rf $TEMP_DIR
    exit 1
fi

# Change to the temporary directory
cd $TEMP_DIR

# Check if the builds branch exists
if git ls-remote --heads origin $GIT_BRANCH | grep -q $GIT_BRANCH; then
    echo -e "${YELLOW}Checking out existing '$GIT_BRANCH' branch...${NC}"
    git checkout $GIT_BRANCH || git checkout -b $GIT_BRANCH origin/$GIT_BRANCH
else
    echo -e "${YELLOW}Creating new '$GIT_BRANCH' branch...${NC}"
    git checkout --orphan $GIT_BRANCH
    git rm -rf .
    echo "# Galactic Expansion Build Artifacts" > README.md
    git add README.md
    git commit -m "Initialize builds branch"
fi

# Create directory structure
BUILD_DATE=$(date +%Y-%m-%d)
BUILD_TIME=$(date +%H-%M-%S)
ARTIFACTS_DIR="builds/$BUILD_DATE/$BUILD_TIME"

mkdir -p $ARTIFACTS_DIR/modules

# Copy main JARs
if [ "$MAIN_JAR_COUNT" -gt 0 ]; then
    echo -e "${GREEN}Copying main JAR files...${NC}"
    cp -v $CURRENT_DIR/build/libs/*.jar $ARTIFACTS_DIR/
fi

# Copy module JARs
if [ "$MODULE_JAR_COUNT" -gt 0 ]; then
    echo -e "${GREEN}Copying module JAR files...${NC}"
    
    for MODULE_DIR in $CURRENT_DIR/*/build/libs; do
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

# Check if we need to update the remote URL to include credentials
REMOTE_URL=$(git remote get-url origin)
if [[ $REMOTE_URL != *"github.com/$GIT_USERNAME"* ]]; then
    echo -e "${YELLOW}Updating remote URL to use your credentials...${NC}"
    # If you're using SSH keys
    if [[ $REMOTE_URL == git@* ]]; then
        git remote set-url origin "git@github.com:$GIT_REPO.git"
    else
        # For HTTPS, we'll use a GitHub personal access token if available
        if [ -n "$GITHUB_TOKEN" ]; then
            git remote set-url origin "https://$GIT_USERNAME:$GITHUB_TOKEN@github.com/$GIT_REPO.git"
        else
            # If no token is available, try with just the username
            git remote set-url origin "https://github.com/$GIT_REPO.git"
        fi
    fi
fi

# Push to the builds branch
git push origin $GIT_BRANCH

# Clean up temporary repository
echo -e "${GREEN}Cleaning up...${NC}"
cd $CURRENT_DIR
rm -rf $TEMP_DIR

echo -e "${GREEN}Done! Build artifacts pushed to $GIT_BRANCH branch.${NC}"
echo -e "${GREEN}View your artifacts at: https://github.com/$GIT_REPO/tree/$GIT_BRANCH/$ARTIFACTS_DIR${NC}"