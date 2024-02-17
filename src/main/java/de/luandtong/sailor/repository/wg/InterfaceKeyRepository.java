package de.luandtong.sailor.repository.wg;

import de.luandtong.sailor.domian.wg.InterfaceKey;

import java.util.UUID;

public interface InterfaceKeyRepository {

    void save(java.util.UUID uuid, String publicKey, String privateKey);

    InterfaceKey findWGInterfaceKeyByUuid(java.util.UUID uuid);

    void deleteByUuid(UUID uuid);
}
