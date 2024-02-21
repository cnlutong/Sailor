# **Sailor**

Sailor是一个基于SpringBoot构建的WireGuard VPN管理工具，支持在Ubuntu服务器上快速部署。它提供一个网页界面，通过该界面，用户能够轻松创建和管理VPN服务及其客户端。

## **技术栈**

- **后端**：使用SpringBoot框架，采用洋葱结构设计，代码清晰，可维护性高。
- **数据持久化**：使用MariaDB作为数据库系统。
- **前端**：界面使用HTML和CSS构建，提供直观友好的用户操作界面。

## **核心功能**

- **用户管理**：支持用户注册与登录功能，确保服务的安全性。
- **VPN服务管理**：允许用户创建和删除VPN服务实例，便于管理不同的网络环境。
- **客户端管理**：支持创建和删除VPN客户端，方便用户根据需求配置网络访问。

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

### **关于**

软件目前还处在开发测试阶段，请您谨慎使用，请勿用作生产环境。

### **联系方式**

cnlutong@gmail.com / tong.lu@hhu.de