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
