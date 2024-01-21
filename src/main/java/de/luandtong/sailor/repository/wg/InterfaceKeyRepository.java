package de.luandtong.sailor.repository.wg;

import de.luandtong.sailor.domian.wg.InterfaceKey;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface InterfaceKeyRepository {

    void save(UUID uuid, String publicKey, String privateKey);

    InterfaceKey findWGInterfaceKeyByUuid(UUID uuid);
}
