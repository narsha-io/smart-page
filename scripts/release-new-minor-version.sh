#!/usr/bin/env bash

CURRENT_FOLDER="$(dirname "$0")"

echo "CWI pre-proc" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install \
&& echo "CWI clean" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -Prelease --batch-mode release:clean \
&& echo "CWI install" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -pl smart-page-test,smart-page-spring/smart-page-spring-test -am \
&& echo "CWI prepare" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -Prelease --batch-mode release:prepare \
&& echo "CWI install 2" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install -pl smart-page-test,smart-page-spring/smart-page-spring-test -am \
&& echo "CWI install 3" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml clean install  \
&& echo "CWI deploy" \
&& ./mvnw -f $CURRENT_FOLDER/../pom.xml verify -Prelease --batch-mode release:perform