#!/usr/bin/env bash

CURRENT_FOLDER="$(dirname "$0")"

./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -Prelease --batch-mode release:clean release:prepare release:perform