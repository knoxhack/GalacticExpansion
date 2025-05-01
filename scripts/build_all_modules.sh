#!/bin/bash

# Build all modules in the Galactic Expansion mod
# This script builds all modules without pushing to GitHub

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting build process for all modules...${NC}"

# Set Java Home
export JAVA_HOME=/nix/store/sziqmjk1i28cxcr5x29jbz3dzhiz1pii-openjdk-headless-21+35

# Module list
MODULES=(
    "core"
    "power"
    "machinery"
    "biotech"
    "energy"
    "construction"
    "space"
    "utilities"
    "vehicles"
    "weaponry"
    "robotics"
)

# Build main project first
echo -e "${YELLOW}Building main project...${NC}"
./gradlew build --no-daemon

# Check if main build was successful
if [ $? -ne 0 ]; then
    echo -e "${RED}Main build failed. Stopping build process.${NC}"
    exit 1
fi

echo -e "${GREEN}Main build successful!${NC}"

# Build each module individually
for module in "${MODULES[@]}"; do
    echo -e "${BLUE}Building module: ${module}...${NC}"
    ./gradlew :${module}:build --no-daemon
    
    # Check if module build was successful
    if [ $? -ne 0 ]; then
        echo -e "${RED}Module ${module} build failed.${NC}"
    else
        echo -e "${GREEN}Module ${module} built successfully!${NC}"
    fi
done

# Count total JARs built
MAIN_JAR_COUNT=$(find ./build/libs -name "*.jar" | wc -l)
MODULE_JAR_COUNT=$(find ./*/build/libs -name "*.jar" | wc -l)
TOTAL_JARS=$((MAIN_JAR_COUNT + MODULE_JAR_COUNT))

echo -e "${GREEN}Build process completed.${NC}"
echo -e "${GREEN}Total JAR files built: ${TOTAL_JARS}${NC}"
echo -e "${GREEN}Main JAR files: ${MAIN_JAR_COUNT}${NC}"
echo -e "${GREEN}Module JAR files: ${MODULE_JAR_COUNT}${NC}"

# List all JAR files
echo -e "${YELLOW}JAR files locations:${NC}"
find . -name "*.jar" -path "*/build/libs/*" | sort