package de.luandtong.sailor.datenbank.wg.serverInterface;

import de.luandtong.sailor.domian.wg.ServerInterface;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("ServerInterfaces")
public class ServerInterfaceDTO {

    @Id
    private Long id;
    private UUID uuid;
    private String serverInterfaceName;
    private UUID interfaceKeyUUID;
    private String address;
    private String listenPort;
    private String ethPort;

    private LocalDateTime time;

    public ServerInterfaceDTO(UUID uuid, String serverInterfaceName, UUID interfaceKeyUUID, String address, String listenPort, String ethPort) {
        this.uuid = uuid;
        this.serverInterfaceName = serverInterfaceName;
        this.interfaceKeyUUID = interfaceKeyUUID;
        this.address = address;
        this.listenPort = listenPort;
        this.ethPort = ethPort;
        this.time = LocalDateTime.now();
    }

    ServerInterface toServerInterface() {
        return new ServerInterface(this.uuid, interfaceKeyUUID, this.address, this.listenPort, this.ethPort);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getServerInterfaceName() {
        return serverInterfaceName;
    }

    public UUID getInterfaceKeyUUID() {
        return interfaceKeyUUID;
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

    public LocalDateTime getTime() {
        return time;
    }
}
