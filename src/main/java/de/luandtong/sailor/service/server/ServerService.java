package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.server.Server;
import de.luandtong.sailor.domian.wg.ClientInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        String conf = serverInterfaceService.getServerInterfaceConfig(serverUUID, keyUUID, address, listenPort, ethPort, privateKey);
        // 保存到数据库
        serverInterfaceService.save(serverUUID, serverInterfaceName, keyUUID, address, listenPort, ethPort);
        // 写入配置文件
        this.writeNewServerConfigFile(serverInterfaceName, conf);

        server.enableServer(serverInterfaceName);
        server.startServer(serverInterfaceName);
    }

    private void writeNewServerConfigFile(String name, String conf) throws IOException, InterruptedException {
        server.writeNewConfigFile(true, name, conf);
    }


    public void creativeClientInterface(String selectedInterface, String clientName) throws IOException, InterruptedException {
        List<String> key = generateClientKey(clientName);
        UUID keyUUID = UUID.randomUUID();
        String privateKey = key.get(1);
        interfaceKeyService.save(keyUUID, key.get(0), key.get(1));
        UUID clientUUID = UUID.randomUUID();

        String address = getANewAddress(selectedInterface) + "/24";
        System.out.println("address" + ": " + address);
        String dns = "1.1.1.1, 1.0.0.1";
        String persistentKeepalive = "15";

        UUID serverInterfaceUUID = serverInterfaceService.findServerInterfaceUUIDByInterfaceName(selectedInterface);
        String serverInterfacePublicKey = interfaceKeyService.getPublicKeyByUuid(serverInterfaceService.findServerInterfaceKeyUUIDByInterfaceName(selectedInterface));
        String publicIP = server.getServerPublicIP();
        String listenPort = serverInterfaceService.findServerInterfaceByServername(selectedInterface).getListenPort();

        // 创建客户端接口配置文件
        String conf = clientInterfaceService.getServerInterfaceConfig(clientUUID, clientName, serverInterfaceUUID, address, dns, persistentKeepalive,
                privateKey, serverInterfacePublicKey, publicIP, listenPort);
        // 保存到数据库
        clientInterfaceService.saveClientInterface(clientUUID, clientName, keyUUID, getServerInterfaceUUID(selectedInterface), address, dns, persistentKeepalive);
        // 写入配置文件
        this.writeNewClientConfigFile(clientName, conf);
        this.appendServerConfigFile(selectedInterface, clientName, key.get(0), address);

        server.restartServer(selectedInterface);
    }

    private void writeNewClientConfigFile(String interfaceName, String conf) throws IOException, InterruptedException {
        server.writeNewConfigFile(false, interfaceName, conf);
    }

    public void appendServerConfigFile(String serverName, String clientName, String clientPublicKey, String clientAddress) throws IOException, InterruptedException {
        server.appendServerConfigFile(serverName, clientName, clientPublicKey, clientAddress);
    }


    private String getANewAddress(String selectedInterface) {
        return serverInterfaceService.getSubnetz(serverInterfaceService.findServerInterfaceByServername(selectedInterface)) + "." + this.getNewClientAddress(selectedInterface);
    }

    private String getNewClientAddress(String selectedInterface) {
        UUID serverInterfaceUUID = getServerInterfaceUUID(selectedInterface);
        List<ClientInterface> clientInterfaces = clientInterfaceService.findClientInterfacesByServerInterfaceUUID(serverInterfaceUUID);
        int lastAddress = clientInterfaceService.getLastAddressFromClientInterfaces(clientInterfaces);
        return String.valueOf(lastAddress + 1);
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

    public UUID getServerInterfaceUUID(String selectedInterface) {
        return serverInterfaceService.findServerInterfaceUUIDByInterfaceName(selectedInterface);
    }

    //    ClientInterface
    public List<String> findClientInterfaceNamesByServerInterfaceName(String serverInterface) {
        return clientInterfaceService.findClientInterfaceNamesByServerInterfaceUUID(serverInterfaceService.findServerInterfaceUUIDByInterfaceName(serverInterface));
    }

    public String getDefaultEth() throws IOException, InterruptedException {
        return server.getDefaultEth();
    }

    public String getDownloadLinkByClientName(String clientName) {
        return "/download/" + clientName;
    }
}