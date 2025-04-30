#!/bin/bash

# Script to automate GitHub releases for Galactic Expansion mod
# This handles versioning, changelog generation, and pushing changes

# Exit on any error
set -e

# Configuration
MOD_ID="galacticexpansion"
GITHUB_REPO="astroframe/galactic-expansion" # Replace with your actual GitHub username/repo

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo "Git is not installed. Please install git first."
    exit 1
fi

# Check if gh (GitHub CLI) is installed
if ! command -v gh &> /dev/null; then
    echo "GitHub CLI (gh) is not installed. Please install it first."
    echo "See: https://github.com/cli/cli#installation"
    exit 1
fi

# Ensure we're in the repository root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/.."

# Get the current version from gradle.properties
CURRENT_VERSION=$(grep "mod_version" gradle.properties | cut -d'=' -f2 | tr -d ' ')
echo "Current version: $CURRENT_VERSION"

# Parse the version into components
IFS='.' read -r MAJOR MINOR PATCH <<< "$CURRENT_VERSION"

# Determine the release type (major, minor, patch)
echo "Select release type:"
echo "1) Major release (x.0.0)"
echo "2) Minor release (0.x.0)"
echo "3) Patch release (0.0.x)"
read -p "Enter choice [1-3]: " RELEASE_TYPE

case $RELEASE_TYPE in
    1)
        NEW_MAJOR=$((MAJOR + 1))
        NEW_VERSION="$NEW_MAJOR.0.0"
        RELEASE_LABEL="Major"
        ;;
    2)
        NEW_MINOR=$((MINOR + 1))
        NEW_VERSION="$MAJOR.$NEW_MINOR.0"
        RELEASE_LABEL="Minor"
        ;;
    3)
        NEW_PATCH=$((PATCH + 1))
        NEW_VERSION="$MAJOR.$MINOR.$NEW_PATCH"
        RELEASE_LABEL="Patch"
        ;;
    *)
        echo "Invalid choice, defaulting to patch release"
        NEW_PATCH=$((PATCH + 1))
        NEW_VERSION="$MAJOR.$MINOR.$NEW_PATCH"
        RELEASE_LABEL="Patch"
        ;;
esac

echo "New version will be: $NEW_VERSION"

# Update version in gradle.properties
sed -i "s/mod_version=$CURRENT_VERSION/mod_version=$NEW_VERSION/" gradle.properties

# Generate changelog
CHANGELOG_FILE="CHANGELOG.md"
TEMP_CHANGELOG=$(mktemp)

echo "# Changelog for version $NEW_VERSION" > "$TEMP_CHANGELOG"
echo "" >> "$TEMP_CHANGELOG"
echo "## $RELEASE_LABEL Release - $(date +%Y-%m-%d)" >> "$TEMP_CHANGELOG"
echo "" >> "$TEMP_CHANGELOG"

# Get commits since last tag
if git describe --abbrev=0 --tags &> /dev/null; then
    LAST_TAG=$(git describe --abbrev=0 --tags)
    echo "Getting changes since last tag: $LAST_TAG"
    git log "$LAST_TAG"..HEAD --pretty=format:"- %s" >> "$TEMP_CHANGELOG"
else
    echo "No previous tags found, including all commits"
    git log --pretty=format:"- %s" >> "$TEMP_CHANGELOG"
fi

echo "" >> "$TEMP_CHANGELOG"
echo "" >> "$TEMP_CHANGELOG"

# Prepend to existing changelog or create a new one
if [ -f "$CHANGELOG_FILE" ]; then
    echo "Updating existing changelog"
    cat "$CHANGELOG_FILE" >> "$TEMP_CHANGELOG"
else
    echo "Creating new changelog"
fi

mv "$TEMP_CHANGELOG" "$CHANGELOG_FILE"

# Prompt for release notes
echo "Enter a brief summary of changes for this release (leave blank to use automated changelog):"
read -p "> " RELEASE_NOTES

if [ -z "$RELEASE_NOTES" ]; then
    RELEASE_NOTES=$(cat "$CHANGELOG_FILE" | sed -n '/^## '"$RELEASE_LABEL"' Release/,/^$/p' | tail -n +3)
fi

# Build the mod
echo "Building the mod..."
./gradlew clean build

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "Build failed! Aborting release."
    exit 1
fi

# Get paths to built JARs
CORE_JAR=$(find ./core/build/libs -name "${MOD_ID}-*.jar" -not -name "*-sources.jar" | sort -V | tail -n 1)
ENERGY_JAR=$(find ./energy/build/libs -name "${MOD_ID}_energy-*.jar" -not -name "*-sources.jar" | sort -V | tail -n 1)
MACHINERY_JAR=$(find ./machinery/build/libs -name "${MOD_ID}_machinery-*.jar" -not -name "*-sources.jar" | sort -V | tail -n 1)
EXAMPLE_JAR=$(find ./example/build/libs -name "${MOD_ID}_example-*.jar" -not -name "*-sources.jar" | sort -V | tail -n 1)

# Commit changes
git add gradle.properties "$CHANGELOG_FILE"
git commit -m "Release v$NEW_VERSION"

# Create the tag
git tag -a "v$NEW_VERSION" -m "Version $NEW_VERSION"

# Push to GitHub
echo "Pushing changes to GitHub..."
git push origin main
git push origin "v$NEW_VERSION"

# Create GitHub release
echo "Creating GitHub release..."
gh release create "v$NEW_VERSION" \
    --title "Galactic Expansion v$NEW_VERSION" \
    --notes "$RELEASE_NOTES" \
    $CORE_JAR $ENERGY_JAR $MACHINERY_JAR $EXAMPLE_JAR

echo "Release v$NEW_VERSION completed successfully!"