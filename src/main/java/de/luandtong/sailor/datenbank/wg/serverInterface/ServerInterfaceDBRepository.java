package de.luandtong.sailor.datenbank.wg.serverInterface;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ServerInterfaceDBRepository extends CrudRepository<ServerInterfaceDTO, Long> {

    ServerInterfaceDTO findByServerInterfaceName(String serverInterfaceName);

    ServerInterfaceDTO findServerInterfaceDTOByUuid(UUID uuid);

    List<ServerInterfaceDTO> findAll();


    long count();


}
