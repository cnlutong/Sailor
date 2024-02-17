package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ServerServiceTest {

    @Mock
    private Server server;

    @Mock
    private ServerInterfaceService serverInterfaceService;

    @Mock
    private ClientInterfaceService clientInterfaceService;

    @Mock
    private InterfaceKeyService interfaceKeyService;

    @InjectMocks
    private ServerService serverService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void creativeServerInterfaceShouldPerformExpectedOperations() throws IOException, InterruptedException {
        // 假设数据
        String serverInterfaceName = "wg0";
        String address = "10.10.0.1/24";
        String listenPort = "51820";
        String ethPort = "eth0";
        List<String> key = Arrays.asList("publicKey", "privateKey");
        UUID keyUUID = UUID.randomUUID();
        UUID serverUUID = UUID.randomUUID();

        // 配置模拟行为
        when(server.generateKey(true, serverInterfaceName)).thenReturn(key);
        doNothing().when(interfaceKeyService).save(any(UUID.class), eq(key.get(0)), eq(key.get(1)));
        doNothing().when(serverInterfaceService).save(any(UUID.class), eq(serverInterfaceName), any(UUID.class), eq(address), eq(listenPort), eq(ethPort));
        doNothing().when(server).writeNewConfigFile(anyBoolean(), eq(serverInterfaceName), anyString());
        doNothing().when(server).enableServer(serverInterfaceName);
        doNothing().when(server).startServer(serverInterfaceName);

        // 调用方法
        serverService.creativeServerInterface(serverInterfaceName, address, listenPort, ethPort);

        // 验证交互
        verify(interfaceKeyService, times(1)).save(any(UUID.class), eq(key.get(0)), eq(key.get(1)));
        verify(serverInterfaceService, times(1)).save(any(UUID.class), eq(serverInterfaceName), any(UUID.class), eq(address), eq(listenPort), eq(ethPort));
        verify(server, times(1)).enableServer(serverInterfaceName);
        verify(server, times(1)).startServer(serverInterfaceName);
    }

    @Test
    void deleteServerInterfaceShouldPerformExpectedOperations() throws IOException, InterruptedException {
        String selectedInterfaceName = "wg0";

        // 配置模拟行为
        doNothing().when(serverInterfaceService).deleteServerInterface(selectedInterfaceName);
        doNothing().when(server).stopServer(selectedInterfaceName);
        doNothing().when(server).disableServer(selectedInterfaceName);
        doNothing().when(server).removeServerInterfaceFiles(selectedInterfaceName);

        // 调用方法
        serverService.deleteServerInterface(selectedInterfaceName);

        // 验证交互
        verify(serverInterfaceService, times(1)).deleteServerInterface(selectedInterfaceName);
        verify(server, times(1)).stopServer(selectedInterfaceName);
        verify(server, times(1)).disableServer(selectedInterfaceName);
        verify(server, times(1)).removeServerInterfaceFiles(selectedInterfaceName);
    }


    @Test
    void isValidIPAddressShouldReturnTrueForValidIP() {
        String validIP = "192.168.1.1";
        assertTrue(serverService.isValidIPAddress(validIP), "Should return true for a valid IP address");
    }

    @Test
    void isValidIPAddressShouldReturnFalseForInvalidIP() {
        String invalidIP = "192.168.300.1";
        assertFalse(serverService.isValidIPAddress(invalidIP), "Should return false for an invalid IP address");
    }

    // 测试私有IP地址
    @Test
    void isPrivateIPAddressShouldReturnTrueForPrivateIP() {
        String privateIP = "192.168.1.1";
        assertTrue(serverService.isPrivateIPAddress(privateIP), "Should return true for a private IP address");
    }

    @Test
    void isPrivateIPAddressShouldReturnFalseForPublicIP() {
        String publicIP = "8.8.8.8";
        assertFalse(serverService.isPrivateIPAddress(publicIP), "Should return false for a public IP address");
    }

    // 测试常用端口
    @Test
    void isCommonlyUsedPortShouldIdentifyCommonPorts() {
        assertTrue(serverService.isCommonlyUsedPort("80"), "Should identify 80 as a commonly used port");
        assertFalse(serverService.isCommonlyUsedPort("12345"), "Should identify 12345 as not a commonly used port");
    }

}
