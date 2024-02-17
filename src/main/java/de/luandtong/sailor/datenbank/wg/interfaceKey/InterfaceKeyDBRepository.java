package de.luandtong.sailor.datenbank.wg.interfaceKey;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface InterfaceKeyDBRepository extends CrudRepository<InterfaceKeyDTO, Long> {

    InterfaceKeyDTO findInterfaceKeyDTOByUuid(UUID uuid);

    @Query("DELETE FROM InterfaceKeys WHERE uuid=:uuid")
    void deleteByUuid(@Param("uuid") UUID uuid);

}
