# **Sailor**

For the Chinese version, click here: [简体中文](https://github.com/cnlutong/Sailor/blob/master/README.zh_CN.md).

Sailor is a WireGuard VPN management tool built on SpringBoot, supporting rapid deployment on Ubuntu servers. It offers a web interface, through which users can easily create and manage VPN services and their clients.

## **Technology Stack**

- **Backend**: Utilizes the SpringBoot framework, adopts an onion architecture design, making the code clear and maintainable.
- **Data Persistence**: Employs MariaDB as the database system.
- **Frontend**: The interface is constructed with HTML and CSS, providing an intuitive and friendly user operation interface.

## **Core Features**

- **User Management**: Supports user registration and login features, ensuring the security of the service.
- **VPN Service Management**: Allows users to create and delete VPN service instances, facilitating the management of different network environments.
- **Client Management**: Supports the creation and deletion of VPN clients, enabling users to configure network access according to their needs.

## **Quick Start**

Please ensure your server meets the following requirements:

- Access to the internet
- Possession of a public IP or located within an internally networked environment with port forwarding configured
- Ubuntu 20.04/Debian 12 or newer versions (or some derivatives based on these)

### **Installation Steps**

One-click deployment script

```bash
curl -sSL -o deploy.sh https://raw.githubusercontent.com/cnlutong/Sailor/master/deploy.sh && chmod +x deploy.sh && sudo ./deploy.sh
```

After completion, please visit http://server_address:8080 to start using Sailor.

### **About**

The software is currently in the development and testing stage, please use it with caution and not for production environments.


### **Contact Information**
cnlutong@gmail.com / tong.lu@hhu.de