package de.luandtong.sailor.datenbank.wg.interfaceKey;

import de.luandtong.sailor.domian.wg.InterfaceKey;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("InterfaceKeys")
public class InterfaceKeyDTO {

    @Id
    Long id;

    java.util.UUID uuid;
    String publicKey;
    String privateKey;
    private LocalDateTime time;

    public InterfaceKeyDTO(java.util.UUID uuid, String publicKey, String privateKey) {
        this.uuid = uuid;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.time = LocalDateTime.now();
    }

    InterfaceKey toInterfaceKey() {
        return new InterfaceKey(this.uuid, this.publicKey, this.privateKey);
    }

    public java.util.UUID getUuid() {
        return uuid;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
