#!/bin/bash

# Fetch all remote branches to ensure the list is up-to-date
git fetch --all

# Get a list of all branches starting with "dev/"
branches=$(git branch -r | grep "origin/dev/" | sed 's/origin\///')

# Loop through each branch and perform the update
for branch in $branches; do
  echo "Processing branch: $branch"

  # Check out the branch
  git checkout $branch
  if [ $? -ne 0 ]; then
    echo "Failed to check out branch: $branch. Skipping..."
    continue
  fi

  # Update Playwright dependency
  mvn versions:use-latest-versions -Dincludes=com.microsoft.playwright:playwright
  if [ $? -ne 0 ]; then
    echo "Failed to update Playwright in branch: $branch. Skipping..."
    continue
  fi

  # Stage and commit changes
  git add pom.xml
  git commit -m "Updated Playwright version to the latest version"
  if [ $? -ne 0 ]; then
    echo "Failed to commit changes in branch: $branch. Skipping..."
    continue
  fi

  # Push changes to GitHub
  git push origin $branch
  if [ $? -ne 0 ]; then
    echo "Failed to push changes for branch: $branch. Skipping..."
    continue
  fi

  echo "Successfully updated branch: $branch"
done

echo "All branches starting with 'dev/' have been processed."