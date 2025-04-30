#!/bin/bash

# Script to automatically push changes to GitHub
# This handles git setup, committing changes, and pushing to the repository

# Exit on any error
set -e

# Configuration
GITHUB_REPO="astroframe/galactic-expansion"  # Replace with your actual GitHub username/repo

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo "Git is not installed. Please install git first."
    exit 1
fi

# Check if the directory is a git repository
if [ ! -d ".git" ]; then
    # Initialize git repository if it doesn't exist
    echo "Initializing git repository..."
    git init
    
    # Setup GitHub remote (will need to be updated with your actual repository URL)
    echo "Setting up remote..."
    git remote add origin "https://github.com/${GITHUB_REPO}.git"
    
    # Create initial commit
    echo "Creating initial commit..."
    git add .
    git commit -m "Initial commit"
else
    echo "Git repository already initialized."
fi

# Check for changes
if git diff-index --quiet HEAD --; then
    echo "No changes to commit."
    exit 0
fi

# Prompt for commit message
echo "Enter a brief description of your changes:"
read -p "> " COMMIT_MESSAGE

if [ -z "$COMMIT_MESSAGE" ]; then
    COMMIT_MESSAGE="Update mod files"
fi

# Stage all changes
git add .

# Commit changes
git commit -m "$COMMIT_MESSAGE"

# Push to GitHub (this will prompt for username/password if not configured)
echo "Pushing changes to GitHub..."
git push origin main

echo "Changes pushed to GitHub successfully!"

# Prompt to create a release
echo "Do you want to create a release? (y/n)"
read -p "> " CREATE_RELEASE

if [ "$CREATE_RELEASE" = "y" ] || [ "$CREATE_RELEASE" = "Y" ]; then
    # Run the release script
    ./scripts/github-release.sh
fi