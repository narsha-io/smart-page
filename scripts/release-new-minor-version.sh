#!/usr/bin/env bash

CURRENT_FOLDER="$(dirname "$0")"

echo "CWI pre-proc" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install \
&& echo "CWI clean" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml -Prelease --batch-mode release:clean \
&& echo "CWI prepare" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml -Prelease --batch-mode release:prepare \
&& echo "CWI perform" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml verify -Prelease --batch-mode release:perform