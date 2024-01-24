package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.wg.ClientInterface;
import de.luandtong.sailor.repository.wg.ClientInterfaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientInterfaceService {


    private ClientInterfaceRepository clientInterfaceRepository;


    ClientInterface findClientInterfaceByClientName(String clientName) {
        return clientInterfaceRepository.findClientInterfaceByClientName(clientName);
    }

    void saveClientInterface(UUID uuid, String clientName, UUID interfacekeyuuid, UUID serverInterfaceUUID, String address, String dns, String persistentKeepalive) {
        clientInterfaceRepository.save(uuid, clientName, interfacekeyuuid, serverInterfaceUUID, address, dns, persistentKeepalive);
    }

    List<ClientInterface> findAllClientInterface() {
        return clientInterfaceRepository.findAllClientInterfaceName().stream()
                .map(this::findClientInterfaceByClientName)
                .collect(Collectors.toList());
    }

}
