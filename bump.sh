#!/bin/bash

function die_with() { echo "$*" >&2; exit 1; }

echo "Getting current version from pom.xml"
CURRENT_VERSION="`sed -n 's|.*<version>\(.*\)</version>.*|\1|p' pom.xml | awk '{ print $1; exit }'`"
echo "Current version from pom.xml: $CURRENT_VERSION"

read -p "New version: " NEW_VERSION || die_with "Prompt for new version failed"

if ! echo $NEW_VERSION | grep -i -- '-SNAPSHOT' >/dev/null; then echo "WARNING: changing to a release version!"; fi

if [ -f pom.xml ]; then
    echo "Updating the project version in pom.xml to $NEW_VERSION"
    awk "/${CURRENT_VERSION//./\\.}/{ if (DONE != 1) {gsub(/${CURRENT_VERSION//./\\.}/, \"$NEW_VERSION\"); DONE=1; }} {print;}" < pom.xml > pom.xml.1 && mv -f pom.xml.1 pom.xml || die_with "Failed to update pom.xml!"
fi

if [ -f build.gradle ]; then
    echo "Updating the project version in build.gradle to $NEW_VERSION"
    awk "/${CURRENT_VERSION//./\\.}/{ if (DONE != 1) {gsub(/${CURRENT_VERSION//./\\.}/, \"$NEW_VERSION\"); DONE=1; }} {print;}" < build.gradle > build.gradle.1 && mv -f build.gradle.1 build.gradle || die_with "Failed to update build.gradle!"
fi

if [ -f README.md ]; then
    echo "Updating the project version in README.md to $NEW_VERSION"
    awk "/${CURRENT_VERSION//./\\.}/{ if (DONE != 1) {gsub(/${CURRENT_VERSION//./\\.}/, \"$NEW_VERSION\"); }} {print;}" < README.md > README.md.1 && mv -f README.md.1 README.md || die_with "Failed to update README.md!"
fi
