package de.luandtong.sailor.domian.wg;

//[Interface]
//Address = 10.10.0.1/24
//ListenPort = 51820
//PrivateKey = 0D8Uq5HLIJMLmZZ9lBdS1dsmEkdZSjrxuikDXaWF01U=
//PostUp = iptables -A FORWARD -i wg0 -j ACCEPT; iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE
//PostDown = iptables -D FORWARD -i wg0 -j ACCEPT; iptables -t nat -D POSTROUTING -o eth0 -j MASQUERADE
//
//[Peer]
//PublicKey = JYW3KLFxox4L3LoT2HnMnv7Elyttt9DDk76YufOaRkQ=
//AllowedIPs = 10.10.0.3/24

import java.util.UUID;

public class ServerInterface implements WGInterface {

    private UUID uuid;

    private UUID interfaceKey;
    private String address;
    private String listenPort;
    private String ethPort;

    public ServerInterface(UUID uuid, UUID interfaceKey, String address, String listenPort, String ethPort) {
        this.uuid = uuid;
        this.interfaceKey = interfaceKey;
        this.address = address;
        this.listenPort = listenPort;
        this.ethPort = ethPort;
    }

    public String creativeInterfaceConfFile(String serverInterfaceName, String privateKey) {
        // 创建 WireGuard 配置文件内容
        return "[Interface]\n" +
                "Address = " + this.address + "\n" +
                "ListenPort = " + this.listenPort + "\n" +
                "PrivateKey = " + privateKey + "\n" +
                "PostUp = iptables -A FORWARD -i " + serverInterfaceName + " -j ACCEPT; iptables -t nat -A POSTROUTING -o " + this.ethPort + " -j MASQUERADE" + "\n" +
                "PostDown = iptables -D FORWARD -i " + serverInterfaceName + " -j ACCEPT; iptables -t nat -D POSTROUTING -o " + this.ethPort + " -j MASQUERADE" + "\n\n";
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getInterfaceKey() {
        return interfaceKey;
    }

    public String getAddress() {
        return address;
    }

    public String getListenPort() {
        return listenPort;
    }

    public String getEthPort() {
        return ethPort;
    }
}
