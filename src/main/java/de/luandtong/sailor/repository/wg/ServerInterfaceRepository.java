package de.luandtong.sailor.repository.wg;

import de.luandtong.sailor.domian.wg.ServerInterface;

import java.util.List;
import java.util.UUID;

public interface ServerInterfaceRepository {

    void save(UUID uuid, String ServerInterfaceName, UUID WGInterfaceKeyUUID, String address, String listenPort, String ethPort);

    ServerInterface findServerInterfaceByServername(String serverInterfaceName);

    List<String> findAllServerInterfaceName();

    boolean hasServerInterface();

    UUID findServerInterfaceUUIDByInterfaceName(String serverInterfaceName);
}