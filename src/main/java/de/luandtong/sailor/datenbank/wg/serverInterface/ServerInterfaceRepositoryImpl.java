package de.luandtong.sailor.datenbank.wg.serverInterface;

import de.luandtong.sailor.domian.wg.ServerInterface;
import de.luandtong.sailor.repository.wg.ServerInterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ServerInterfaceRepositoryImpl implements ServerInterfaceRepository {

    @Autowired
    private ServerInterfaceDBRepository serverInterfaceDBRepository;

    @Override
    public void save(UUID uuid, String serverInterfaceName, UUID wGInterfaceKeyUUID, String address, String listenPort, String ethPort) {
        serverInterfaceDBRepository.save(new ServerInterfaceDTO(uuid, serverInterfaceName, wGInterfaceKeyUUID, address, listenPort, ethPort));
    }

    @Override
    public ServerInterface findServerInterfaceByServername(String serverInterfaceName) {
        return serverInterfaceDBRepository.findByServerInterfaceName(serverInterfaceName).toServerInterface();
    }

    @Override
    public List<String> findAllServerInterfaceName() {
        return serverInterfaceDBRepository.findAll().stream()
                .map(ServerInterfaceDTO::getServerInterfaceName)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasServerInterface() {
        System.out.println(serverInterfaceDBRepository.count());
        return serverInterfaceDBRepository.count() > 0;
    }
}
