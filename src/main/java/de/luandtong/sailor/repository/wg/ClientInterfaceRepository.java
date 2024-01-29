package de.luandtong.sailor.repository.wg;

import de.luandtong.sailor.domian.wg.ClientInterface;

import java.util.List;
import java.util.UUID;


public interface ClientInterfaceRepository {

    void save(UUID uuid, String clientName, UUID interfacekeyuuid, UUID serverInterfaceUUID, String address, String dns, String persistentKeepalive);

    ClientInterface findClientInterfaceByClientName(String clientName);

    List<String> findAllClientInterfaceName();

    List<ClientInterface> findClientInterfacesByServerInterfaceUUID(UUID serverInterfaceUUID);
}

