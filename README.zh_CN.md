# **Sailor**

Sailor是一个基于SpringBoot构建的WireGuard VPN管理工具，支持在Ubuntu服务器上快速部署。它提供一个网页界面，通过该界面，用户能够轻松创建和管理VPN服务及其客户端。

## **快速开始**

请确保您的服务器满足以下条件：

- 可以访问互联网
- 拥有公网IP或在已经配置好端口转发的内网中
- Ubuntu 20.04/Debian 12 或更新版本(或基于此的部分衍生版)

### **安装步骤**

一键部署脚本

```bash
curl -sSL -o deploy.sh https://raw.githubusercontent.com/cnlutong/Sailor/master/deploy.sh && chmod +x deploy.sh && sudo ./deploy.sh
```

完毕后，请访问`http://服务器地址:8080`开始使用Sailor。

### **注意**

软件目前还处在开发测试阶段，请您谨慎使用，请勿用作生产环境。

### **联系方式**

cnlutong@gmail.com / tong.lu@hhu.de