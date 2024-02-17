package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.wg.ServerInterface;
import de.luandtong.sailor.repository.wg.ServerInterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServerInterfaceService {

    @Autowired
    private ServerInterfaceRepository serverInterfaceRepository;

    public ServerInterface findServerInterfaceByServername(String serverInterfaceName) {
        return serverInterfaceRepository.findServerInterfaceByServername(serverInterfaceName);
    }

    public boolean hasServerInterface() {
        return serverInterfaceRepository.hasServerInterface();
    }

    public boolean hasServerInterfaceByServername(String serverInterfaceName) {
        return serverInterfaceRepository.hasServerInterfaceByServername(serverInterfaceName);
    }

    public boolean hasServerInterfaceByAddress(String address) {
        return serverInterfaceRepository.hasServerInterfaceByAddress(address);
    }

    public boolean hasServerInterfaceByListenPort(String listenPort) {
        return serverInterfaceRepository.hasServerInterfaceByListenPort(listenPort);
    }

    public boolean hasServerInterfaceByListenPortAndEthPort(String listenPort, String ethPort) {
        return serverInterfaceRepository.hasServerInterfaceByListenPortAndEthPort(listenPort, ethPort);
    }

    public String getServerInterfaceConfig(String serverInterfaceName, UUID uuid, UUID interfaceKey, String address, String listenPort, String ethPort, String privateKey) {
        ServerInterface serverInterface = new ServerInterface(uuid, interfaceKey, address, listenPort, ethPort);
        return serverInterface.creativeInterfaceConfFile(serverInterfaceName, privateKey);
    }

    public void save(UUID uuid, String ServerInterfaceName, UUID WGInterfaceKeyUUID, String address, String listenPort, String ethPort) {
        serverInterfaceRepository.save(uuid, ServerInterfaceName, WGInterfaceKeyUUID, address, listenPort, ethPort);
    }

    public List<ServerInterface> findAllServerInterface() {
        return serverInterfaceRepository.findAllServerInterfaceName().stream()
                .map(serverInterfaceRepository::findServerInterfaceByServername)
                .collect(Collectors.toList());
    }


    public List<String> findAllServerInterfaceNames() {
        return serverInterfaceRepository.findAllServerInterfaceName();
    }

    public UUID findServerInterfaceUUIDByInterfaceName(String serverInterfaceName) {
        return serverInterfaceRepository.findServerInterfaceUUIDByInterfaceName(serverInterfaceName);
    }

    public UUID findServerInterfaceKeyUUIDByInterfaceName(String serverInterfaceName) {
        return serverInterfaceRepository.findServerInterfaceKeyUUIDByInterfaceName(serverInterfaceName);
    }


    public String getSubnetz(ServerInterface serverInterface) {
        String address = serverInterface.getAddress();

        String[] parts = address.split("\\.");
        return String.join(".", Arrays.copyOf(parts, 3));
    }


    public void deleteServerInterface(String selectedInterfaceName) {
        serverInterfaceRepository.deleteServerInterface(selectedInterfaceName);
    }
}
