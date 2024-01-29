package de.luandtong.sailor.datenbank.wg.clientInterface;

import de.luandtong.sailor.domian.wg.ClientInterface;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("ClientInterfaces")
public class ClientInterfaceDTO {

    @Id
    private Long id;
    private UUID uuid;

    private String clientName;
    private UUID interfaceKeyUUID;
    private UUID serverInterfaceUUID;
    private String address;
    private String dns;
    private String persistentKeepalive;

    private LocalDateTime time;

    public ClientInterfaceDTO(UUID uuid, String clientName, UUID interfaceKeyUUID, UUID serverInterfaceUUID, String address, String dns, String persistentKeepalive) {
        this.uuid = uuid;
        this.clientName = clientName;
        this.interfaceKeyUUID = interfaceKeyUUID;
        this.serverInterfaceUUID = serverInterfaceUUID;
        this.address = address;
        this.dns = dns;
        this.persistentKeepalive = persistentKeepalive;
        this.time = LocalDateTime.now();
    }

    ClientInterface toClientInterface() {
        return new ClientInterface(uuid, clientName, serverInterfaceUUID, address, dns, persistentKeepalive);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getClientName() {
        return clientName;
    }

    public UUID getInterfaceKeyUUID() {
        return interfaceKeyUUID;
    }

    public UUID getServerInterfaceUUID() {
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

    public LocalDateTime getTime() {
        return time;
    }
}
