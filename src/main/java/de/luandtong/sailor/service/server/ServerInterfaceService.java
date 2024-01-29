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

    public String getSubnetz(ServerInterface serverInterface) {
        String address = serverInterface.getAddress();

        String[] parts = address.split("\\.");
        return String.join(".", Arrays.copyOf(parts, 3));
    }


}
