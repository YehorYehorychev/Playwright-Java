#!/bin/bash

set -e

BRANCH="dev"

echo "Fetching latest changes from remote..."
git fetch origin

echo "Checking out branch: $BRANCH"
git checkout $BRANCH
git pull origin $BRANCH

echo "Updating Playwright dependency..."
mvn versions:use-latest-versions -Dincludes=com.microsoft.playwright:playwright

echo "Staging pom.xml..."
git add pom.xml

if git diff --cached --quiet; then
  echo "No changes detected in pom.xml. Nothing to commit."
else
  echo "Committing changes..."
  git commit -m "Update Playwright to latest version"

  echo "Pushing changes to remote..."
  git push origin $BRANCH
  echo "Successfully updated Playwright on branch: $BRANCH"
fi