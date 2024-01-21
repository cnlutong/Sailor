package de.luandtong.sailor.datenbank.wg.clientInterface;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ClientInterfaceDBRepository extends CrudRepository<ClientInterfaceDTO, Long> {

    ClientInterfaceDTO findClientInterfaceDTOByUuid(UUID uuid);

    List<ClientInterfaceDTO> findAll();

    ClientInterfaceDTO findClientInterfaceDTOByClientName(String clientName);
}
