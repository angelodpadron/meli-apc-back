#!/bin/sh

BASE_COMMAND="./mvnw build-helper:parse-version versions:set -DnewVersion="
MAJOR_ARGS="\${parsedVersion.nextMajorVersion}.0.0"
MINOR_ARGS="\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion}.0"
PATCH_ARGS="\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion}"
COMMIT_COMMAND="./mvnw versions:commit"
REVERT_COMMAND="./mvnw version:revert"

# Check if Maven Wrapper exists
if [ ! -f "./mvnw" ]; then
  echo "Error: Maven Wrapper (mvnw) not found in this directory."
  echo "Use mvn wrapper:wrapper to generate the required files"
  echo "More info: https://maven.apache.org/wrapper/"
  exit 1
fi

# Function to execute the bump command depending on type
execute_bump() {
  local version_type="$1"
  local version_args="$2"

  echo "Bumping $version_type version..."

  ${BASE_COMMAND}${version_args}

  # Check if the version set was successful
  if [ $? -eq 0 ]; then
    ${COMMIT_COMMAND}
    echo "$version_type version bump successful!"
  else
    echo "Error: Failed to bump $version_type version."
    echo "Rolling back..."

    ${REVERT_COMMAND}

    exit 1
  fi
}

# Bump major version
bump_major() {
  execute_bump "major" "${MAJOR_ARGS}"
}

# Bump minor version
bump_minor() {
  execute_bump "minor" "${MINOR_ARGS}"
}

# Bump patch version
bump_patch() {
  execute_bump "patch" "${PATCH_ARGS}"
}

echo_help() {
  echo "Usage: $0 {major|minor|patch}"
}

# Check if at least one argument is provided
if [ $# -eq 0 ]; then
  echo_help
  exit 1
fi

# Process command-line argument
case $1 in
  major)
    bump_major
  ;;
  minor)
    bump_minor
  ;;
  patch)
    bump_patch
  ;;
  *)
    echo "Invalid argument: $1"
    echo_help
    exit 1
  ;;
esac
