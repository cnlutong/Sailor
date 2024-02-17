#!/bin/bash

# 停止执行脚本时遇到的错误
set -e

# 定义变量
MYSQL_ROOT_PASSWORD="rgbwQLj9nfPYaHVZKaFF"
JDK_VERSION="21"

# 1. 安装 JDK 21
echo "Installing OpenJDK $JDK_VERSION..."
sudo apt-get update
sudo apt-get install -y openjdk-$JDK_VERSION-jdk
java -version

# 2. 下载最新的发行版 JAR 从 GitHub
echo "Downloading latest release from GitHub..."
LATEST_RELEASE=$(curl -s https://api.github.com/repos/cnlutong/Sailor/releases/latest | grep "browser_download_url.*jar" | cut -d '"' -f 4)
curl -L $LATEST_RELEASE -o sailor_app.jar

# 3. 安装 Docker
echo "Installing Docker..."
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
# Add the repository to Apt sources:
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin


# 5. 下载 docker-compose.yml 文件
echo "Downloading docker-compose.yml..."
curl -sSL -o docker-compose.yml https://raw.githubusercontent.com/cnlutong/Sailor/master/docker-compose.yml

# 6. 启动 MariaDB 容器
echo "Starting MariaDB container with Docker Compose..."
docker-compose up -d

# 7. 运行下载的 JAR 文件
echo "Running downloaded JAR file..."
java -jar sailor_app.jar

echo "Deployment completed successfully."
