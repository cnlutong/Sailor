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
    public List<String> findClientInterfaceNamesByServerInterfaceUUID(UUID serverInterfaceUUID) {
        return clientInterfaceDBRepository.findClientInterfaceDTOSByServerInterfaceUUID(serverInterfaceUUID).stream()
                .map(ClientInterfaceDTO::getClientName)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientInterface> findClientInterfacesByServerInterfaceUUID(UUID serverInterfaceUUID) {
        return clientInterfaceDBRepository.findClientInterfaceDTOSByServerInterfaceUUID(serverInterfaceUUID).stream()
                .map(ClientInterfaceDTO::toClientInterface)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteClientInterfaceByClientNameAndServerInterfaceUUID(String clientName, UUID serverInterfaceUUID) {
        System.out.println("clientName: " + clientName);
        System.out.println("serverInterfaceKeyUUID: "+serverInterfaceUUID);
        clientInterfaceDBRepository.deleteByClientNameAndServerInterfaceUUID(clientName, serverInterfaceUUID);
    }

    public boolean existsByClientName(String clientName){
        return clientInterfaceDBRepository.existsByClientName(clientName);
    }

}
