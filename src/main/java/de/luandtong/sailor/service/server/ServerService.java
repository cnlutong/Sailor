package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.server.QRCodeGenerator;
import de.luandtong.sailor.domian.server.Server;
import de.luandtong.sailor.domian.wg.ClientInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

//服务器初始化相关
@Service
public class ServerService {


    private Server server;
    @Autowired
    private ServerInterfaceService serverInterfaceService;
    @Autowired
    private ClientInterfaceService clientInterfaceService;
    @Autowired
    private InterfaceKeyService interfaceKeyService;

    public ServerService() {
        this.server = new Server();
        server.setServerNeedsInitialization(false);
    }

    public void creativeServerInterface(String serverInterfaceName, String address, String listenPort, String ethPort) throws IOException, InterruptedException {
        List<String> key = generateServerKey(serverInterfaceName);
        UUID keyUUID = UUID.randomUUID();
        String privateKey = key.get(1);
        interfaceKeyService.save(keyUUID, key.get(0), key.get(1));
        UUID serverUUID = UUID.randomUUID();

        // 创建服务端接口配置文件
        String conf = serverInterfaceService.getServerInterfaceConfig(serverInterfaceName, serverUUID, keyUUID, address, listenPort, ethPort, privateKey);
        // 保存到数据库
        serverInterfaceService.save(serverUUID, serverInterfaceName, keyUUID, address, listenPort, ethPort);
        // 写入配置文件
        this.writeNewServerConfigFile(serverInterfaceName, conf);

        server.openPort(listenPort);
        server.enableServer(serverInterfaceName);
        server.startServer(serverInterfaceName);
    }

    private void writeNewServerConfigFile(String name, String conf) throws IOException, InterruptedException {
        server.writeNewConfigFile(true, name, conf);
    }


    public int creativeClientInterface(String selectedInterfaceName, String clientName) throws Exception {

        if (clientInterfaceService.existsByClientName(clientName)) {
            return 0;
        }

        List<String> key = generateClientKey(clientName);
        UUID keyUUID = UUID.randomUUID();
        String privateKey = key.get(1);
        interfaceKeyService.save(keyUUID, key.get(0), key.get(1));
        UUID clientUUID = UUID.randomUUID();

        int newClientLastAddress = getNewClientAddress(selectedInterfaceName);

        if (newClientLastAddress == -1) {
            return -1;
        }

        String address = serverInterfaceService.getSubnetz(serverInterfaceService.findServerInterfaceByServername(selectedInterfaceName)) + "." + newClientLastAddress + "/24";

        String dns = "1.1.1.1, 1.0.0.1";
        String persistentKeepalive = "15";

        UUID serverInterfaceUUID = serverInterfaceService.findServerInterfaceUUIDByInterfaceName(selectedInterfaceName);
        String serverInterfacePublicKey = interfaceKeyService.getPublicKeyByUuid(serverInterfaceService.findServerInterfaceKeyUUIDByInterfaceName(selectedInterfaceName));
        String publicIP = server.getServerPublicIP();
        String listenPort = serverInterfaceService.findServerInterfaceByServername(selectedInterfaceName).getListenPort();

        // 创建客户端接口配置文件
        String conf = clientInterfaceService.getServerInterfaceConfig(clientUUID, clientName, keyUUID, serverInterfaceUUID, address, dns, persistentKeepalive,
                privateKey, serverInterfacePublicKey, publicIP, listenPort);

        System.out.println("New ServerInterface Conf: " + conf);
        // 保存到数据库
        clientInterfaceService.saveClientInterface(clientUUID, clientName, keyUUID, getServerInterfaceUUID(selectedInterfaceName), address, dns, persistentKeepalive);
        // 写入配置文件
        this.writeNewClientConfigFile(clientName, conf);
        this.appendServerConfigFile(selectedInterfaceName, clientName, key.get(0), address);
        this.generateQRCodeImage(conf, clientName);

//        服务重启
        enableServer(selectedInterfaceName);
        startServer(selectedInterfaceName);
        restartServer(selectedInterfaceName);

        return 1;
    }


//    private String getANewAddress(String selectedInterface) {
//        int newClientAddress = this.getNewClientAddress(selectedInterface);
//        if(newClientAddress == -1){
//            return newClientAddress;
//        }
//        return serverInterfaceService.getSubnetz(serverInterfaceService.findServerInterfaceByServername(selectedInterface)) + "." + newClientAddress + "/24";
//    }


    private int getNewClientAddress(String selectedInterface) {
        UUID serverInterfaceUUID = getServerInterfaceUUID(selectedInterface);
        List<ClientInterface> clientInterfaces = clientInterfaceService.findClientInterfacesByServerInterfaceUUID(serverInterfaceUUID);
        return clientInterfaceService.getLastAddressFromClientInterfaces(clientInterfaces);
//        return String.valueOf(lastAddress + 1);
    }

    private void writeNewClientConfigFile(String interfaceName, String conf) throws IOException, InterruptedException {
        server.writeNewConfigFile(false, interfaceName, conf);
    }

    public void deleteClientInterface(String clientName, String selectedInterfaceName) throws IOException, InterruptedException {
//        System.out.println("delete ClientInterface: "+clientInterfaceService.findClientInterfaceByClientName(clientName).getInterfaceKeyUUID());

        interfaceKeyService.deleteByUuid(clientInterfaceService.findClientInterfaceByClientName(clientName).getInterfaceKeyUUID());
        clientInterfaceService.deleteClientInterface(clientName, serverInterfaceService.findServerInterfaceUUIDByInterfaceName(selectedInterfaceName));
        removeClientInterfaceFromConfig(selectedInterfaceName, clientName);
        removeClientInterfaceFiles(clientName);
//        服务重启
        enableServer(selectedInterfaceName);
        startServer(selectedInterfaceName);
        restartServer(selectedInterfaceName);
    }

    public void deleteServerInterface(String selectedInterfaceName) throws IOException, InterruptedException {
        interfaceKeyService.deleteByUuid(serverInterfaceService.findServerInterfaceKeyUUIDByInterfaceName(selectedInterfaceName));
        deleteClientInterfaceFromServerInterface(selectedInterfaceName);
        serverInterfaceService.deleteServerInterface(selectedInterfaceName);


//        服务关闭
        stopServer(selectedInterfaceName);
        disableServer(selectedInterfaceName);
        removeServerInterfaceFile(selectedInterfaceName);
    }

    private void removeServerInterfaceFile(String selectedInterfaceName) throws IOException {
        server.removeServerInterfaceFiles(selectedInterfaceName);

    }

    private void deleteClientInterfaceFromServerInterface(String selectedInterfaceName) {
        UUID serverINterfaceUUID = serverInterfaceService.findServerInterfaceUUIDByInterfaceName(selectedInterfaceName);
        List<ClientInterface> clientInterfaces = clientInterfaceService.findClientInterfacesByServerInterfaceUUID(serverINterfaceUUID);

        for (ClientInterface clientInterface : clientInterfaces) {
            interfaceKeyService.deleteByUuid(clientInterface.getInterfaceKeyUUID());
        }

        for (ClientInterface clientInterface : clientInterfaces) {
            clientInterfaceService.deleteClientInterface(clientInterface.getClientName(), serverINterfaceUUID);
        }
    }

    private void removeClientInterfaceFromConfig(String serverName, String clientName) throws IOException, InterruptedException {
        server.removeClientFromConfig(serverName, clientName);
    }

    private void removeClientInterfaceFiles(String clientName) throws IOException {
        server.removeClientInterfaceFiles(clientName);
    }


    public boolean hasServerInterfaceByServername(String serverInterfaceName) {
        return serverInterfaceService.hasServerInterfaceByServername(serverInterfaceName);
    }

    public boolean hasServerInterfaceByAddress(String address) {
        return serverInterfaceService.hasServerInterfaceByAddress(address);
    }

    public boolean hasServerInterfaceByListenPort(String listenPort) {
        return serverInterfaceService.hasServerInterfaceByListenPort(listenPort);
    }

    public boolean hasServerInterfaceByListenPortAndEthPort(String listenPort, String ethPort) {
        return serverInterfaceService.hasServerInterfaceByListenPortAndEthPort(listenPort, ethPort);
    }

    private void generateQRCodeImage(String conf, String fileName) throws Exception {
        QRCodeGenerator.generateQRCodeImage(conf, fileName);
    }

    public void appendServerConfigFile(String serverName, String clientName, String clientPublicKey, String clientAddress) throws IOException, InterruptedException {
        server.appendServerConfigFile(serverName, clientName, clientPublicKey, clientAddress);
    }


    //    Server
    public void initializeServer() throws IOException, InterruptedException {
        server.initializeServer();
    }

    public void enableServer(String name) throws IOException, InterruptedException {
        server.enableServer(name);
    }

    public void startServer(String name) throws IOException, InterruptedException {
        server.startServer(name);
    }

    public void restartServer(String name) throws IOException, InterruptedException {
        server.restartServer(name);
    }

    public void disableServer(String name) throws IOException, InterruptedException {
        server.disableServer(name);
    }

    public void stopServer(String name) throws IOException, InterruptedException {
        server.stopServer(name);
    }

    public List<String> generateServerKey(String name) throws IOException, InterruptedException {
        return server.generateKey(true, name);
    }

    public List<String> generateClientKey(String name) throws IOException, InterruptedException {
        return server.generateKey(false, name);
    }

    public boolean hasServer() {
        return serverInterfaceService.hasServerInterface();
    }


    //    ServerInterface
    public List<String> getServerInterfaceNames() {
        return serverInterfaceService.findAllServerInterfaceNames();
    }

    public String getServerInterfacePublicKey(String selectedInterface) {
        return interfaceKeyService.findWGInterfaceKeyByUuid(serverInterfaceService.findServerInterfaceByServername(selectedInterface).getInterfaceKey()).publicKey();
    }

    public String getServerInterfaceAddress(String selectedInterface) {
        return serverInterfaceService.findServerInterfaceByServername(selectedInterface).getAddress();
    }

    public String getServerInterfaceListenPort(String selectedInterface) {
        return serverInterfaceService.findServerInterfaceByServername(selectedInterface).getListenPort();
    }

    public String getServerInterfaceEthPort(String selectedInterface) {
        return serverInterfaceService.findServerInterfaceByServername(selectedInterface).getEthPort();
    }

    private UUID getServerInterfaceUUID(String selectedInterface) {
        return serverInterfaceService.findServerInterfaceUUIDByInterfaceName(selectedInterface);
    }

    //    ClientInterface
    public List<String> findClientInterfaceNamesByServerInterfaceName(String serverInterface) {
        return clientInterfaceService.findClientInterfaceNamesByServerInterfaceUUID(serverInterfaceService.findServerInterfaceUUIDByInterfaceName(serverInterface));
    }

    public String getDefaultEth() throws IOException, InterruptedException {
        return server.getDefaultEth();
    }

    private String getDownloadLinkByClientName(String clientName) {
        return "/download/" + clientName;
    }

    // 判断字符串是否为有效的IP地址
    public boolean isValidIPAddress(String address) {
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            String ip = inetAddress.getHostAddress();
            // 验证InetAddress生成的地址与输入地址是否相同，排除域名等情况
            return ip.equals(address);
        } catch (UnknownHostException e) {
            return false; // 地址无效
        }
    }

    public boolean isPrivateIPAddress(String address) {
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            return inetAddress.isSiteLocalAddress();
        } catch (UnknownHostException e) {
            return false; // 地址无效或检查失败
        }
    }

    public boolean isCommonlyUsedPort(String listenPort) {
        int[] commonPorts = {21, 22, 23, 25, 53, 80, 110, 443, 3306, 6379, 5432, 27017, 8080, 8443};
        try {
            int port = Integer.parseInt(listenPort);
            if (port < 0 || port > 65535) {
                return false; // 端口号超出有效范围
            }
            if (port <= 1023) {
                return true; // 端口号是保留的
            }
            for (int commonPort : commonPorts) {
                if (port == commonPort) {
                    return true; // 端口号是常用且不应被使用的
                }
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}