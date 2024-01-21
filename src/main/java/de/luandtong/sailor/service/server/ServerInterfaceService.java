package de.luandtong.sailor.service.server;

import de.luandtong.sailor.datenbank.wg.serverInterface.ServerInterfaceRepositoryImpl;
import de.luandtong.sailor.domian.wg.ServerInterface;
import de.luandtong.sailor.repository.wg.ServerInterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean hasServerInterface(){
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


    public List<String> findAllServerInterfaceName() {
        return serverInterfaceRepository.findAllServerInterfaceName();
    }
}
