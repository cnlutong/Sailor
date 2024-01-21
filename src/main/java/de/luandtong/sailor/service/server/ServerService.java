package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.server.Server;
import de.luandtong.sailor.domian.wg.ServerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    public void creativeServerInterface(String serverName, String serverInterfaceName, String address, String listenPort, String ethPort) throws IOException, InterruptedException {
        List<String> key = this.generateServerKey(serverName);
        UUID keyUUID = UUID.randomUUID();
        interfaceKeyService.save(keyUUID, key.get(0), key.get(1));
        UUID serverUUID = UUID.randomUUID();
        serverInterfaceService.save(serverUUID, serverInterfaceName, keyUUID, address ,listenPort, ethPort);

    }

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

    public boolean hasServer() {
        return serverInterfaceService.hasServerInterface();
    }

    public List<String> getServerInterfaceName(){
        return null;
//        return serverInterfaceService.findAllServerInterfaceName();
    }


}
