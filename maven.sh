#!/bin/bash

architecture="$1"

if [ -z "$architecture" ]; then
  echo "You must inform the architecture. Usage: maven.sh [amd64|arm64]"
  exit 1
fi

javaVersion="23"

export JAVA_HOME="/usr/lib/jvm/temurin-$javaVersion-jdk-$architecture"
export M2_HOME=/opt/maven
export MAVEN_HOME=/opt/maven
export PATH=${M2_HOME}/bin:${PATH}
