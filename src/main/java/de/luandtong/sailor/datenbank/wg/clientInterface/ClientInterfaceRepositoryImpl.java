package de.luandtong.sailor.datenbank.wg.clientInterface;

import de.luandtong.sailor.domian.wg.ClientInterface;
import de.luandtong.sailor.repository.wg.ClientInterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ClientInterfaceRepositoryImpl implements ClientInterfaceRepository {

    @Autowired
    private ClientInterfaceDBRepository clientInterfaceDBRepository;


    @Override
    public void save(UUID uuid, String clientName, UUID interfacekeyuuid, UUID serverInterfaceUUID, String address, String dns, String persistentKeepalive) {
        clientInterfaceDBRepository.save(new ClientInterfaceDTO(uuid, clientName, interfacekeyuuid, serverInterfaceUUID, address, dns, persistentKeepalive));
    }

    @Override
    public ClientInterface findClientInterfaceByClientName(String clientName) {
        return clientInterfaceDBRepository.findClientInterfaceDTOByClientName(clientName).toClientInterface();
    }

    @Override
    public List<String> findAllClientInterfaceName() {
        return clientInterfaceDBRepository.findAll().stream()
                .map(ClientInterfaceDTO::getClientName)
                .collect(Collectors.toList());
    }

}
