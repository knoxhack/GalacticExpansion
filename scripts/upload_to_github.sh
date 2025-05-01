#!/bin/bash

# Upload build artifacts to GitHub
# This script uploads the JAR files from ./build/libs and all module build/libs directories to GitHub

# Configuration - update these values
GITHUB_USERNAME=""
GITHUB_TOKEN=""
GITHUB_REPO=""
GITHUB_BRANCH="builds"  # A dedicated branch for builds

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo -e "${RED}Error: git is not installed. Please install git and try again.${NC}"
    exit 1
fi

# Verify GitHub credentials are set
if [ -z "$GITHUB_USERNAME" ] || [ -z "$GITHUB_TOKEN" ] || [ -z "$GITHUB_REPO" ]; then
    echo -e "${YELLOW}GitHub credentials are not set in the script.${NC}"
    echo -e "${YELLOW}Please provide the following information:${NC}"
    
    # Prompt for GitHub username if not set
    if [ -z "$GITHUB_USERNAME" ]; then
        read -p "GitHub Username: " GITHUB_USERNAME
    fi
    
    # Prompt for GitHub token if not set
    if [ -z "$GITHUB_TOKEN" ]; then
        read -p "GitHub Personal Access Token: " GITHUB_TOKEN
    fi
    
    # Prompt for GitHub repository if not set
    if [ -z "$GITHUB_REPO" ]; then
        read -p "GitHub Repository (format: username/repo): " GITHUB_REPO
    fi
fi

# Create a temporary directory for our operations
TEMP_DIR=$(mktemp -d)
echo -e "${GREEN}Created temporary directory: $TEMP_DIR${NC}"

# Clone the repository
echo -e "${GREEN}Cloning repository...${NC}"
git clone https://github.com/$GITHUB_REPO.git $TEMP_DIR
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to clone repository. Please check your credentials and repository name.${NC}"
    exit 1
fi

cd $TEMP_DIR

# Check if the builds branch exists, if not create it
git fetch origin
if ! git branch -a | grep -q "remotes/origin/$GITHUB_BRANCH"; then
    echo -e "${GREEN}Creating builds branch...${NC}"
    git checkout --orphan $GITHUB_BRANCH
    git rm -rf .
    echo "# Automated Builds" > README.md
    git add README.md
    git commit -m "Initialize builds branch"
else
    echo -e "${GREEN}Checking out builds branch...${NC}"
    git checkout $GITHUB_BRANCH || git checkout -b $GITHUB_BRANCH origin/$GITHUB_BRANCH
fi

# Create the directory structure
mkdir -p builds/$(date +%Y-%m-%d)
BUILD_DIR="builds/$(date +%Y-%m-%d)/$(date +%H-%M-%S)"
mkdir -p "$BUILD_DIR"
mkdir -p "$BUILD_DIR/modules"

# Copy main JAR files
echo -e "${GREEN}Copying main JAR files...${NC}"
cp -v ../../build/libs/*.jar "$BUILD_DIR/" 2>/dev/null || echo -e "${YELLOW}No main JAR files found.${NC}"

# Copy module JAR files
echo -e "${GREEN}Copying module JAR files...${NC}"
MODULE_COUNT=0
for MODULE_DIR in ../../*/build/libs; do
    MODULE=$(echo $MODULE_DIR | awk -F/ '{print $(NF-2)}')
    if [ -d "$MODULE_DIR" ] && [ -n "$(ls -A $MODULE_DIR 2>/dev/null)" ]; then
        mkdir -p "$BUILD_DIR/modules/$MODULE"
        cp -v $MODULE_DIR/*.jar "$BUILD_DIR/modules/$MODULE/" 2>/dev/null
        let MODULE_COUNT++
    fi
done

echo -e "${GREEN}Copied JAR files from $MODULE_COUNT modules.${NC}"

# Create a build info file
echo -e "${GREEN}Creating build info file...${NC}"
cat > "$BUILD_DIR/build_info.txt" << EOF
Build Date: $(date)
Commit: $(cd ../../ && git rev-parse HEAD 2>/dev/null || echo "Unknown")
Branch: $(cd ../../ && git rev-parse --abbrev-ref HEAD 2>/dev/null || echo "Unknown")
Module Count: $MODULE_COUNT
EOF

# Add all files to git
git add -A
git status

# Commit the files
echo -e "${GREEN}Committing files...${NC}"
git commit -m "Automated build upload - $(date)"

# Push to GitHub
echo -e "${GREEN}Pushing to GitHub...${NC}"
git remote set-url origin https://$GITHUB_USERNAME:$GITHUB_TOKEN@github.com/$GITHUB_REPO.git
git push origin $GITHUB_BRANCH

# Check if push was successful
if [ $? -eq 0 ]; then
    echo -e "${GREEN}Successfully uploaded JAR files to GitHub!${NC}"
    echo -e "${GREEN}Files are available at: https://github.com/$GITHUB_REPO/tree/$GITHUB_BRANCH/$BUILD_DIR${NC}"
else
    echo -e "${RED}Failed to push to GitHub. Please check your credentials and try again.${NC}"
fi

# Clean up
cd ../../
rm -rf $TEMP_DIR
echo -e "${GREEN}Cleaned up temporary directory.${NC}"