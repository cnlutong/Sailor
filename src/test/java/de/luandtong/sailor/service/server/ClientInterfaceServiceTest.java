package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.wg.ClientInterface;
import de.luandtong.sailor.repository.wg.ClientInterfaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientInterfaceServiceTest {

    @Mock
    private ClientInterfaceRepository clientInterfaceRepository;

    @InjectMocks
    private ClientInterfaceService clientInterfaceService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void findClientInterfaceByClientNameShouldReturnClientInterface() {
        String clientName = "testClient";
        UUID uuid = UUID.randomUUID();
        UUID interfaceKeyUUID = UUID.randomUUID();
        UUID serverInterfaceUUID = UUID.randomUUID();
        String address = "10.10.0.2/24";
        String dns = "1.1.1.1";
        String persistentKeepalive = "25";
        ClientInterface expectedClientInterface = new ClientInterface(uuid, clientName, interfaceKeyUUID, serverInterfaceUUID, address, dns, persistentKeepalive);

        when(clientInterfaceRepository.findClientInterfaceByClientName(clientName)).thenReturn(expectedClientInterface);

        ClientInterface result = clientInterfaceService.findClientInterfaceByClientName(clientName);

        assertEquals(expectedClientInterface, result);
        verify(clientInterfaceRepository, times(1)).findClientInterfaceByClientName(clientName);
    }

    @Test
    void saveClientInterfaceShouldInvokeRepositorySave() {
        UUID uuid = UUID.randomUUID();
        String clientName = "newClient";
        UUID interfaceKeyUUID = UUID.randomUUID();
        UUID serverInterfaceUUID = UUID.randomUUID();
        String address = "10.10.0.3/24";
        String dns = "8.8.8.8";
        String persistentKeepalive = "15";

        clientInterfaceService.saveClientInterface(uuid, clientName, interfaceKeyUUID, serverInterfaceUUID, address, dns, persistentKeepalive);

        verify(clientInterfaceRepository, times(1)).save(uuid, clientName, interfaceKeyUUID, serverInterfaceUUID, address, dns, persistentKeepalive);
    }

    @Test
    void getServerInterfaceConfigShouldReturnCorrectConfig() {
        UUID uuid = UUID.randomUUID();
        String clientName = "client";
        UUID interfaceKeyUUID = UUID.randomUUID();
        UUID serverInterfaceUUID = UUID.randomUUID();
        String address = "10.10.0.4/24";
        String dns = "8.8.4.4";
        String persistentKeepalive = "20";
        String privateKey = "privateKey";
        String publicKey = "publicKey";
        String publicIP = "192.168.1.1";
        String listenPort = "51820";

        String expectedConfig = clientInterfaceService.getServerInterfaceConfig(uuid, clientName, interfaceKeyUUID, serverInterfaceUUID, address, dns, persistentKeepalive, privateKey, publicKey, publicIP, listenPort);

        assertNotNull(expectedConfig);
        assertTrue(expectedConfig.contains(privateKey));
        assertTrue(expectedConfig.contains(publicIP));
    }


    @Test
    void findClientInterfaceNamesByServerInterfaceUUIDShouldReturnNames() {
        UUID serverInterfaceUUID = UUID.randomUUID();
        List<String> expectedNames = Arrays.asList("client1", "client2");
        when(clientInterfaceRepository.findClientInterfaceNamesByServerInterfaceUUID(serverInterfaceUUID)).thenReturn(expectedNames);

        List<String> result = clientInterfaceService.findClientInterfaceNamesByServerInterfaceUUID(serverInterfaceUUID);

        assertEquals(expectedNames, result);
        verify(clientInterfaceRepository, times(1)).findClientInterfaceNamesByServerInterfaceUUID(serverInterfaceUUID);
    }

    @Test
    void findClientInterfacesByServerInterfaceUUIDShouldReturnInterfaces() {
        UUID serverInterfaceUUID = UUID.randomUUID();
        List<ClientInterface> expectedInterfaces = Arrays.asList(
                new ClientInterface(UUID.randomUUID(), "client1", UUID.randomUUID(), serverInterfaceUUID, "10.10.0.2/24", "8.8.8.8", "25"),
                new ClientInterface(UUID.randomUUID(), "client2", UUID.randomUUID(), serverInterfaceUUID, "10.10.0.3/24", "8.8.4.4", "25")
        );
        when(clientInterfaceRepository.findClientInterfacesByServerInterfaceUUID(serverInterfaceUUID)).thenReturn(expectedInterfaces);

        List<ClientInterface> result = clientInterfaceService.findClientInterfacesByServerInterfaceUUID(serverInterfaceUUID);

        assertEquals(expectedInterfaces, result);
        verify(clientInterfaceRepository, times(1)).findClientInterfacesByServerInterfaceUUID(serverInterfaceUUID);
    }

    @Test
    void getLastAddressFromClientInterfacesShouldReturnNextAvailable() {
        List<ClientInterface> clientInterfaces = Arrays.asList(
                new ClientInterface(UUID.randomUUID(), "client1", UUID.randomUUID(), UUID.randomUUID(), "10.10.0.2/24", "8.8.8.8", "25"),
                new ClientInterface(UUID.randomUUID(), "client2", UUID.randomUUID(), UUID.randomUUID(), "10.10.0.3/24", "8.8.4.4", "25")
        );
        int lastAddress = clientInterfaceService.getLastAddressFromClientInterfaces(clientInterfaces);

        assertEquals(4, lastAddress, "Should return the next available address segment.");
    }

    @Test
    void deleteClientInterfaceShouldInvokeRepositoryDelete() {
        String clientName = "clientToDelete";
        UUID serverInterfaceUUID = UUID.randomUUID();
        doNothing().when(clientInterfaceRepository).deleteClientInterfaceByClientNameAndServerInterfaceUUID(clientName, serverInterfaceUUID);

        clientInterfaceService.deleteClientInterface(clientName, serverInterfaceUUID);

        verify(clientInterfaceRepository, times(1)).deleteClientInterfaceByClientNameAndServerInterfaceUUID(clientName, serverInterfaceUUID);
    }

    @Test
    void existsByClientNameShouldReturnTrueWhenClientExists() {
        String clientName = "existingClient";
        when(clientInterfaceRepository.existsByClientName(clientName)).thenReturn(true);

        boolean exists = clientInterfaceService.existsByClientName(clientName);

        assertTrue(exists, "Should return true when the client exists.");
    }

    @Test
    void existsByClientNameShouldReturnFalseWhenClientDoesNotExist() {
        String clientName = "nonExistingClient";
        when(clientInterfaceRepository.existsByClientName(clientName)).thenReturn(false);

        boolean exists = clientInterfaceService.existsByClientName(clientName);

        assertFalse(exists, "Should return false when the client does not exist.");
    }
}
