#!/bin/bash

export DEBIAN_FRONTEND=noninteractive


### Update
apt update
apt upgrade -y

### Parameters
java_version=23
maven_version=3.9.9
architecture=arm64

### JDK ###
apt install -y wget apt-transport-https gnupg
wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | apt-key add -
echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list

apt update
apt install "temurin-$java_version-jdk" -y

### Maven ###
wget "https://dlcdn.apache.org/maven/maven-3/$maven_version/binaries/apache-maven-$maven_version-bin.tar.gz"
tar -xzf "apache-maven-$maven_version-bin.tar.gz" -C /opt
ln -s "/opt/apache-maven-$maven_version" /opt/maven

maven_file="/etc/profile.d/maven.sh"
wget https://raw.githubusercontent.com/epomatti/azure-servicebus-performance-benchmark/main/maven.sh -O "$maven_file"

echo -e "\n" >> ~/.profile
echo "source $maven_file $architecture" >> ~/.profile
