#!/bin/bash



# 定义变量
MYSQL_ROOT_PASSWORD="rgbwQLj9nfPYaHVZKaFF"
JDK_VERSION="21"

initialize() {
    # 1. 安装 JDK
    echo "**** Installing OpenJDK $JDK_VERSION..."
    sudo apt-get update
    sudo apt-get install -y openjdk-$JDK_VERSION-jdk
    java -version

    # 2. 下载最新的发行版 JAR 从 GitHub
    echo "**** Downloading latest release from GitHub..."
    LATEST_RELEASE=$(curl -s https://api.github.com/repos/cnlutong/Sailor/releases/latest | grep "browser_download_url.*jar" | cut -d '"' -f 4)
    curl -L $LATEST_RELEASE -o sailor_app.jar

    # 3. 安装 Docker
    echo "**** Installing Docker..."
    for pkg in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc; do sudo apt-get remove -y $pkg; done
    sudo apt-get update
    sudo apt-get install -y ca-certificates curl
    sudo install -m 0755 -d /etc/apt/keyrings
    sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
    sudo chmod a+r /etc/apt/keyrings/docker.asc
    # Add the repository to Apt sources
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
      $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt-get update
    sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

    # 4. 下载 docker-compose.yml 文件
    echo "**** Downloading docker-compose.yml..."
    curl -sSL -o docker-compose.yml https://raw.githubusercontent.com/cnlutong/Sailor/master/docker-compose.yml

    # 5. 启动 MariaDB 容器
    echo "**** Starting MariaDB container with Docker Compose..."
    docker compose up -d

    # 6. 等待 MariaDB 容器完全启动
    echo "**** Waiting for MariaDB container to be fully up..."
    sleep 10

    # 7. 执行数据库初始化脚本
    echo "**** Initializing the Sailor database..."
    curl -sSL https://raw.githubusercontent.com/cnlutong/Sailor/master/init_db.sql | docker exec -i sailor_mariadb mariadb -uroot -p"$MYSQL_ROOT_PASSWORD"
    echo "Database initialized successfully."

    # 8. 开启防火墙
    echo "**** Opening the Firewall."
    sudo ufw allow 8080

    echo "
    
    **** Initialization complete.

    "

}

run_app() {
    # 9. 运行下载的 JAR 文件
    echo "**** Running downloaded JAR file in the background..."
    nohup sudo java -jar sailor_app.jar > sailor_app.log 2>&1 &

    echo "

    **** Sailor app is now running in the background.

    "
}

stop_app() {
    echo "**** Stopping Sailor app..."
    PID=$(ps aux | grep 'sailor_app.jar' | grep -v grep | awk '{print $2}')
    if [ -z "$PID" ]; then
        echo "
        
        Sailor app is not running.

        "
    else
        sudo kill "$PID"
        echo "
        
        Sailor app has been stopped.

        "
    fi
}


while true; do
    # 显示欢迎信息和Sailor图形艺术化大logo
    cat << "EOF"

*********************************************
*                                           *
*          Welcome to Sailor!               *
*                                           *
*********************************************

   _____           _   _
  / ____|         (_) | |
 | (___     __ _   _  | |   ___    _ __
  \___ \   / _` | | | | |  / _ \  | '__|
  ____) | | (_| | | | | | | (_) | | |
 |_____/   \__,_| |_| |_|  \___/  |_|


https://github.com/cnlutong/Sailor

Please choose an option:
1. Initialize
2. Run Application
3. Stop Application
4. Exit

EOF

    # 使用 read 命令接受用户输入
read -p "Enter your choice [1-4]: " choice

case $choice in
    1)
        initialize
        ;;
    2)
        run_app
        ;;
    3)
        stop_app
        ;;
    4)
        echo "Exiting..."
        exit 0
        ;;
    *)
        echo "Invalid option $choice. Please enter 1, 2, 3, or 4."
        ;;
esac

done
