package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.wg.InterfaceKey;
import de.luandtong.sailor.repository.wg.InterfaceKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterfaceKeyService {

    @Autowired
    private InterfaceKeyRepository interfaceKeyRepository;


    public void save(java.util.UUID uuid, String publicKey, String privateKey) {
        interfaceKeyRepository.save(uuid, publicKey, privateKey);
    }

    public InterfaceKey findWGInterfaceKeyByUuid(java.util.UUID uuid) {
        return interfaceKeyRepository.findWGInterfaceKeyByUuid(uuid);
    }
}