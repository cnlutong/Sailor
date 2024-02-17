package de.luandtong.sailor.domian.wg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientInterfaceTest {

    private ClientInterface clientInterface;
    private UUID uuid;
    private String clientName;
    private UUID interfaceKeyUUID;
    private UUID serverInterfaceUUID;
    private String address;
    private String dns;
    private String persistentKeepalive;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        clientName = "TestClient";
        interfaceKeyUUID = UUID.randomUUID();
        serverInterfaceUUID = UUID.randomUUID();
        address = "10.10.0.3/24";
        dns = "1.1.1.1, 1.0.0.1";
        persistentKeepalive = "15";

        clientInterface = new ClientInterface(uuid, clientName, interfaceKeyUUID, serverInterfaceUUID, address, dns, persistentKeepalive);
    }

    @Test
    void creativeInterfaceConfFileShouldReturnExpectedContent() {
        String privateKey = "YB6e40vMrTucBgeGrgUAxhb4KdyENhE4VXSPGT57vXs=";
        String publicKey = "publicKey";
        String publicIP = "192.168.1.1";
        String listenPort = "51820";

        String expectedConf = "#client1 \n" +
                "[Interface]\n" +
                "PrivateKey = " + privateKey + "\n" +
                "Address = " + address + "\n" +
                "DNS = " + dns + "\n\n" +
                "[Peer]\n" +
                "PublicKey = " + publicKey + "\n" +
                "Endpoint = " + publicIP + ":" + listenPort + "\n" +
                "AllowedIPs = 0.0.0.0/0\n" +
                "PersistentKeepalive = " + persistentKeepalive;

        String actualConf = clientInterface.creativeInterfaceConfFile(privateKey, publicKey, publicIP, listenPort);

        assertEquals(expectedConf, actualConf, "The generated WireGuard configuration file content should match the expected format.");
    }

    @Test
    void getterMethodsShouldReturnCorrectValues() {
        assertEquals(uuid, clientInterface.getUuid(), "UUID getter should return the correct value.");
        assertEquals(clientName, clientInterface.getClientName(), "ClientName getter should return the correct value.");
        assertEquals(interfaceKeyUUID, clientInterface.getInterfaceKeyUUID(), "InterfaceKeyUUID getter should return the correct value.");
        assertEquals(serverInterfaceUUID, clientInterface.getServerInterfaceUUID(), "ServerInterfaceUUID getter should return the correct value.");
        assertEquals(address, clientInterface.getAddress(), "Address getter should return the correct value.");
        assertEquals(dns, clientInterface.getDns(), "DNS getter should return the correct value.");
        assertEquals(persistentKeepalive, clientInterface.getPersistentKeepalive(), "PersistentKeepalive getter should return the correct value.");
    }
}
