package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.wg.ServerInterface;
import de.luandtong.sailor.repository.wg.ServerInterfaceRepository;
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

class ServerInterfaceServiceTest {

    @Mock
    private ServerInterfaceRepository serverInterfaceRepository;

    @InjectMocks
    private ServerInterfaceService serverInterfaceService;

    private ServerInterface serverInterface;
    private UUID uuid;
    private String serverInterfaceName;
    private UUID interfaceKeyUUID;
    private String address;
    private String listenPort;
    private String ethPort;

    @BeforeEach
    void setUp() {
        openMocks(this);
        uuid = UUID.randomUUID();
        serverInterfaceName = "wg0";
        interfaceKeyUUID = UUID.randomUUID();
        address = "10.10.0.1/24";
        listenPort = "51820";
        ethPort = "eth0";
        serverInterface = new ServerInterface(uuid, interfaceKeyUUID, address, listenPort, ethPort);
    }

    @Test
    void findServerInterfaceByServernameShouldReturnServerInterface() {
        when(serverInterfaceRepository.findServerInterfaceByServername(serverInterfaceName)).thenReturn(serverInterface);

        ServerInterface result = serverInterfaceService.findServerInterfaceByServername(serverInterfaceName);

        assertEquals(serverInterface, result);
        verify(serverInterfaceRepository, times(1)).findServerInterfaceByServername(serverInterfaceName);
    }

    @Test
    void hasServerInterfaceShouldReturnTrueWhenExist() {
        when(serverInterfaceRepository.hasServerInterface()).thenReturn(true);

        boolean result = serverInterfaceService.hasServerInterface();

        assertTrue(result);
        verify(serverInterfaceRepository, times(1)).hasServerInterface();
    }

    // 其他方法的测试用例可以类似地实现
    @Test
    void getSubnetzShouldReturnSubnet() {
        String expectedSubnet = "10.10.0";

        String result = serverInterfaceService.getSubnetz(serverInterface);

        assertEquals(expectedSubnet, result, "Subnet should match expected value.");
    }
    @Test
    void findAllServerInterfaceShouldReturnListOfInterfaces() {
        List<ServerInterface> expectedList = Arrays.asList(serverInterface);
        when(serverInterfaceRepository.findAllServerInterfaceName()).thenReturn(Arrays.asList(serverInterfaceName));
        when(serverInterfaceRepository.findServerInterfaceByServername(serverInterfaceName)).thenReturn(serverInterface);

        List<ServerInterface> resultList = serverInterfaceService.findAllServerInterface();

        assertNotNull(resultList);
        assertEquals(expectedList.size(), resultList.size());
        assertEquals(expectedList.get(0), resultList.get(0));
        verify(serverInterfaceRepository, times(1)).findAllServerInterfaceName();
    }

    @Test
    void findAllServerInterfaceNamesShouldReturnListOfNames() {
        List<String> expectedNames = Arrays.asList(serverInterfaceName);
        when(serverInterfaceRepository.findAllServerInterfaceName()).thenReturn(expectedNames);

        List<String> resultNames = serverInterfaceService.findAllServerInterfaceNames();

        assertNotNull(resultNames);
        assertEquals(expectedNames.size(), resultNames.size());
        assertEquals(expectedNames.get(0), resultNames.get(0));
        verify(serverInterfaceRepository, times(1)).findAllServerInterfaceName();
    }

    @Test
    void findServerInterfaceUUIDByInterfaceNameShouldReturnUUID() {
        when(serverInterfaceRepository.findServerInterfaceUUIDByInterfaceName(serverInterfaceName)).thenReturn(uuid);

        UUID resultUuid = serverInterfaceService.findServerInterfaceUUIDByInterfaceName(serverInterfaceName);

        assertNotNull(resultUuid);
        assertEquals(uuid, resultUuid);
        verify(serverInterfaceRepository, times(1)).findServerInterfaceUUIDByInterfaceName(serverInterfaceName);
    }

    @Test
    void findServerInterfaceKeyUUIDByInterfaceNameShouldReturnKeyUUID() {
        when(serverInterfaceRepository.findServerInterfaceKeyUUIDByInterfaceName(serverInterfaceName)).thenReturn(interfaceKeyUUID);

        UUID resultKeyUUID = serverInterfaceService.findServerInterfaceKeyUUIDByInterfaceName(serverInterfaceName);

        assertNotNull(resultKeyUUID);
        assertEquals(interfaceKeyUUID, resultKeyUUID);
        verify(serverInterfaceRepository, times(1)).findServerInterfaceKeyUUIDByInterfaceName(serverInterfaceName);
    }

    @Test
    void hasServerInterfaceByServernameShouldReturnTrueWhenExist() {
        when(serverInterfaceRepository.hasServerInterfaceByServername(serverInterfaceName)).thenReturn(true);

        boolean result = serverInterfaceService.hasServerInterfaceByServername(serverInterfaceName);

        assertTrue(result);
        verify(serverInterfaceRepository, times(1)).hasServerInterfaceByServername(serverInterfaceName);
    }

    @Test
    void hasServerInterfaceByAddressShouldReturnTrueWhenExist() {
        when(serverInterfaceRepository.hasServerInterfaceByAddress(address)).thenReturn(true);

        boolean result = serverInterfaceService.hasServerInterfaceByAddress(address);

        assertTrue(result);
        verify(serverInterfaceRepository, times(1)).hasServerInterfaceByAddress(address);
    }

    @Test
    void hasServerInterfaceByListenPortShouldReturnTrueWhenExist() {
        when(serverInterfaceRepository.hasServerInterfaceByListenPort(listenPort)).thenReturn(true);

        boolean result = serverInterfaceService.hasServerInterfaceByListenPort(listenPort);

        assertTrue(result);
        verify(serverInterfaceRepository, times(1)).hasServerInterfaceByListenPort(listenPort);
    }

    @Test
    void hasServerInterfaceByListenPortAndEthPortShouldReturnTrueWhenExist() {
        when(serverInterfaceRepository.hasServerInterfaceByListenPortAndEthPort(listenPort, ethPort)).thenReturn(true);

        boolean result = serverInterfaceService.hasServerInterfaceByListenPortAndEthPort(listenPort, ethPort);

        assertTrue(result);
        verify(serverInterfaceRepository, times(1)).hasServerInterfaceByListenPortAndEthPort(listenPort, ethPort);
    }


}
