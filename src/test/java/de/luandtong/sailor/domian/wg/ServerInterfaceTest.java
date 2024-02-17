package de.luandtong.sailor.domian.wg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerInterfaceTest {

    private ServerInterface serverInterface;
    private UUID uuid;
    private UUID interfaceKey;
    private String address;
    private String listenPort;
    private String ethPort;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        interfaceKey = UUID.randomUUID();
        address = "10.10.0.1/24";
        listenPort = "51820";
        ethPort = "eth0";

        serverInterface = new ServerInterface(uuid, interfaceKey, address, listenPort, ethPort);
    }

    @Test
    void creativeInterfaceConfFileShouldReturnExpectedContent() {
        String serverInterfaceName = "wg0";
        String privateKey = "0D8Uq5HLIJMLmZZ9lBdS1dsmEkdZSjrxuikDXaWF01U=";

        String expectedConf = "[Interface]\n" +
                "Address = " + address + "\n" +
                "ListenPort = " + listenPort + "\n" +
                "PrivateKey = " + privateKey + "\n" +
                "PostUp = iptables -A FORWARD -i " + serverInterfaceName + " -j ACCEPT; iptables -t nat -A POSTROUTING -o " + ethPort + " -j MASQUERADE" + "\n" +
                "PostDown = iptables -D FORWARD -i " + serverInterfaceName + " -j ACCEPT; iptables -t nat -D POSTROUTING -o " + ethPort + " -j MASQUERADE" + "\n\n";

        String actualConf = serverInterface.creativeInterfaceConfFile(serverInterfaceName, privateKey);

        assertEquals(expectedConf, actualConf, "The generated WireGuard configuration file content should match the expected format.");
    }

    @Test
    void getterMethodsShouldReturnCorrectValues() {
        assertEquals(uuid, serverInterface.getUuid(), "UUID getter should return the correct value.");
        assertEquals(interfaceKey, serverInterface.getInterfaceKey(), "InterfaceKey getter should return the correct value.");
        assertEquals(address, serverInterface.getAddress(), "Address getter should return the correct value.");
        assertEquals(listenPort, serverInterface.getListenPort(), "ListenPort getter should return the correct value.");
        assertEquals(ethPort, serverInterface.getEthPort(), "EthPort getter should return the correct value.");
    }
}
