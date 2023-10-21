#!/usr/bin/env bash

CURRENT_FOLDER="$(dirname "$0")"

echo "CWI clean" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -Dmaven.test.skip=true -Prelease --batch-mode release:clean \
&& echo "CWI install" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -pl smart-page-spring/smart-page-spring-test -am \
&& echo "CWI prepare" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -Dmaven.test.skip=true -Prelease --batch-mode release:prepare \
&& echo "CWI install 2" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -pl smart-page-spring/smart-page-spring-test -am \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -Dmaven.test.skip=true \
&& echo "CWI deploy" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml verify -Dmaven.test.skip=true -Prelease --batch-mode release:perform