package de.luandtong.sailor.datenbank.wg.interfaceKey;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface InterfaceKeyDBRepository extends CrudRepository<InterfaceKeyDTO, Long> {

    InterfaceKeyDTO findInterfaceKeyDTOByUuid(UUID uuid);

}
