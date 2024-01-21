package de.luandtong.sailor.datenbank.wg.interfaceKey;

import de.luandtong.sailor.domian.wg.InterfaceKey;
import de.luandtong.sailor.repository.wg.InterfaceKeyRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class InterfaceKeyRepositoryImpl implements InterfaceKeyRepository {

    private InterfaceKeyDBRepository interfaceKeyDBRepository;

    @Override
    public void save(UUID uuid, String publicKey, String privateKey) {
        interfaceKeyDBRepository.save(new InterfaceKeyDTO(uuid, publicKey, privateKey));

    }

    @Override
    public InterfaceKey findWGInterfaceKeyByUuid(UUID uuid) {
        return interfaceKeyDBRepository.findInterfaceKeyDTOByUuid(uuid).toInterfaceKey();
    }
}
