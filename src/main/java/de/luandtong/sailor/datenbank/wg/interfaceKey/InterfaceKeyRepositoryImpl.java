package de.luandtong.sailor.datenbank.wg.interfaceKey;

import de.luandtong.sailor.domian.wg.InterfaceKey;
import de.luandtong.sailor.repository.wg.InterfaceKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterfaceKeyRepositoryImpl implements InterfaceKeyRepository {
    @Autowired
    private InterfaceKeyDBRepository interfaceKeyDBRepository;

    @Override
    public void save(java.util.UUID uuid, String publicKey, String privateKey) {
        interfaceKeyDBRepository.save(new InterfaceKeyDTO(uuid, publicKey, privateKey));

    }

    @Override
    public InterfaceKey findWGInterfaceKeyByUuid(java.util.UUID uuid) {
        return interfaceKeyDBRepository.findInterfaceKeyDTOByUuid(uuid).toInterfaceKey();
    }
}
