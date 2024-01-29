package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.wg.ClientInterface;
import de.luandtong.sailor.repository.wg.ClientInterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientInterfaceService {


    @Autowired
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

    List<String> findAllClientInterfaceNames() {
        return clientInterfaceRepository.findAllClientInterfaceName();
    }

    List<ClientInterface> findClientInterfacesByServerInterfaceUUID(UUID serverInterfaceUUID) {
        return clientInterfaceRepository.findClientInterfacesByServerInterfaceUUID(serverInterfaceUUID);
    }

    int getLastAddressFromClientInterfaces(List<ClientInterface> clientInterfaces) {
        return clientInterfaces.stream()
                .map(ClientInterface::getAddress)
                .map(address -> address.split("\\."))
                .map(parts -> Integer.parseInt(parts[parts.length - 1]))
                .max(Comparator.naturalOrder())
                .orElse(1);
        // 如果列表为空或没有找到最大值，则返回1
    }

}
