# **Sailor**

For the Chinese version, click here: [简体中文](https://github.com/cnlutong/Sailor/blob/master/README.zh_CN.md).

Sailor is a WireGuard VPN management tool built on SpringBoot, supporting rapid deployment on Ubuntu servers. It offers a web interface, through which users can easily create and manage VPN services and their clients.


## **Contact Information**
cnlutong@gmail.com / tong.lu@hhu.de

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

### **Demo video** 
Mandarin/English subtitles

https://youtu.be/vJlzLAs8hGM?si=iEEaMA1Kzi9ZfmaV



## **Tutorial**
### Sigin and Login
Upon first use, you need to set up an administrator account.

The minimum length for the username and password is 5 characters, and the maximum is 20 characters. Usernames may contain letters, numbers, underscores, dots, and dashes.


**Since the login page is directly exposed to the public network, I strongly recommend that you use a complex password.**

To generate a complex password, you can use the following website: https://1password.com/password-generator/

![login page](/tutorial/login.png)

### create a new service
![init](/tutorial/init.png)

To create a new VPN service, you need to provide the following information:

- Service Name: The name of your VPN service. You can choose any name you like.
- Server intranet IP: The server-side IP address of your VPN service.
- Server listen port: The port on which your VPN service is available to external clients.
- Ethernet interface: The Ethernet interface your VPN service runs on. This is useful when your server has multiple network interfaces (either physical or virtual). It allows you to offer different VPN services on different interfaces. A typical use case would be using eth0 for Telekom network and eth1 for Vodafone, providing different VPN services for networks from different ISPs. Or using virtual interfaces with VLAN Tags, which can be useful in more complex network environments.

**If you do not understand what I am saying, then all you need to do is enter a name you like for the Service Name to proceed to the next step.**

### Select your VPN service
![home](/tutorial/home.png)
As you can see, you can create multiple VPN services.

### Service Homepage
![server](/tutorial/server.png)

## **Architecture**
### **Topology**
![sailor](/sailor.draw.png)

### **Technology Stack**
- **Backend**: Utilizes the SpringBoot framework, adopts an onion architecture design, making the code clear and maintainable.
- **Data Persistence**: Employs MariaDB as the database system.
- **Frontend**: The interface is constructed with HTML and CSS, providing an intuitive and friendly user operation interface.

### **Core Features**

- **User Management**: Supports user registration and login features, ensuring the security of the service.
- **VPN Service Management**: Allows users to create and delete VPN service instances, facilitating the management of different network environments.
- **Client Management**: Supports the creation and deletion of VPN clients, enabling users to configure network access according to their needs.

## **Notice**

The software is currently in the development and testing stage, please use it with caution and not for production environments.

