# GitHub Integration Scripts

This directory contains scripts for automating GitHub integration with the Galactic Expansion mod project.

## Available Scripts

### `github-release.sh`

This script automates the process of creating GitHub releases with versioning and changelog generation.

**Usage:**
```bash
./scripts/github-release.sh
```

**What it does:**
1. Checks for git and GitHub CLI installation
2. Prompts for the release type (major, minor, patch)
3. Updates the version in gradle.properties
4. Generates a changelog entry
5. Builds the mod
6. Commits the changes
7. Creates a tag
8. Pushes to GitHub
9. Creates a GitHub release with the built JARs

### `push-to-github.sh`

This script automates pushing changes to GitHub.

**Usage:**
```bash
./scripts/push-to-github.sh
```

**What it does:**
1. Initializes a git repository if it doesn't exist
2. Checks for changes
3. Prompts for a commit message
4. Commits and pushes changes to GitHub
5. Optionally runs the release script

## Gradle Versioning Tasks

The project includes custom Gradle tasks for version management:

- `./gradlew bumpMajorVersion` - Bumps the major version (x.0.0)
- `./gradlew bumpMinorVersion` - Bumps the minor version (0.x.0)
- `./gradlew bumpPatchVersion` - Bumps the patch version (0.0.x)
- `./gradlew showCurrentVersion` - Shows the current mod version
- `./gradlew setVersion --new-version=x.y.z` - Sets a specific version

## GitHub Actions

The project includes GitHub Actions workflows:

1. **Build Workflow** (.github/workflows/build.yml)
   - Triggered on push to main branch
   - Builds the project
   - Uploads artifacts

2. **Release Workflow** (.github/workflows/release.yml)
   - Triggered on tag push or manually
   - Creates a GitHub release
   - Builds and attaches mod JARs

## Setting Up GitHub Connection

Before using these scripts:

1. Install Git and GitHub CLI
2. Authenticate with GitHub using GitHub CLI:
   ```
   gh auth login
   ```
3. Create a GitHub repository
4. Update the GITHUB_REPO variable in the scripts if needed (default: "astroframe/galactic-expansion")

## Workflow Guide

### Regular Development:

1. Make your changes to the code
2. Run `./scripts/push-to-github.sh` to commit and push changes

### Creating a Release:

**Option 1: Using the script**
```
./scripts/github-release.sh
```

**Option 2: Using Gradle and GitHub Actions**
```
./gradlew bumpMinorVersion
git add gradle.properties CHANGELOG.md
git commit -m "Bump version"
git tag -a "v$(grep "mod_version" gradle.properties | cut -d'=' -f2 | tr -d ' ')" -m "Release version"
git push origin main --tags
```

This will trigger the GitHub Actions workflow to create a release.