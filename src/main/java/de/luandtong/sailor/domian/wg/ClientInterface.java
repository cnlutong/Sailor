package de.luandtong.sailor.domian.wg;

import java.util.UUID;

//#client1
//[Interface]
//PrivateKey = YB6e40vMrTucBgeGrgUAxhb4KdyENhE4VXSPGT57vXs=
//Address = 10.10.0.3/24
//DNS = 1.1.1.1, 1.0.0.1
//
//        [Peer]
//PublicKey = null
//Endpoint = null:51820
//AllowedIPs = 0.0.0.0/0
//PersistentKeepalive = 15
public class ClientInterface implements WGInterface {

    private UUID uuid;
    private String clientName;
    private UUID serverInterfaceUUID;
    private String address;
    private String dns;
    private String persistentKeepalive;


    public ClientInterface(UUID uuid, String clientName, UUID serverInterfaceUUID, String address, String dns, String persistentKeepalive) {
        this.uuid = uuid;
        this.clientName = clientName;
        this.serverInterfaceUUID = serverInterfaceUUID;
        this.address = address;
        this.dns = dns;
        this.persistentKeepalive = persistentKeepalive;
    }

    public String creativeInterfaceConfFile(String privateKey, String publicKey, String publicIP, String listenPort) {
        // 创建 WireGuard 配置文件内容
        return "#client1 \n" +
                "[Interface]\n" +
                "PrivateKey = " + privateKey + "\n" +
                "Address = " + this.address + "\n" +
                "DNS = " + this.dns + "\n" +

                "[Peer]\n" +
                "PublicKey = " + publicKey + "\n" +
                "Endpoint = " + publicIP + listenPort + "\n" +
                "AllowedIPs = 0.0.0.0/0\n" +
                "PersistentKeepalive = " + this.persistentKeepalive;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getClientName() {
        return clientName;
    }

    public UUID getInterfaceKey() {
        return serverInterfaceUUID;
    }

    public String getAddress() {
        return address;
    }

    public String getDns() {
        return dns;
    }

    public String getPersistentKeepalive() {
        return persistentKeepalive;
    }


}
