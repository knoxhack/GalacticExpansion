#!/bin/bash

# Build the project and push the JAR files to GitHub

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting build and push process...${NC}"

# Step 1: Build the project
echo -e "${YELLOW}Building the project...${NC}"
export JAVA_HOME=/nix/store/sziqmjk1i28cxcr5x29jbz3dzhiz1pii-openjdk-headless-21+35
./gradlew clean build --no-daemon

# Check if build was successful
if [ $? -ne 0 ]; then
    echo -e "${RED}Build failed. Please fix the errors and try again.${NC}"
    exit 1
fi

echo -e "${GREEN}Build successful!${NC}"

# Step 2: Push the JAR files to GitHub
echo -e "${YELLOW}Pushing JAR files to GitHub...${NC}"
./scripts/push_jars_to_github.sh

# Check if push was successful
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to push JAR files to GitHub. Check the error messages above.${NC}"
    exit 1
fi

echo -e "${GREEN}Build and push process completed successfully!${NC}"
echo -e "${GREEN}JAR files are now available on your GitHub repository.${NC}"