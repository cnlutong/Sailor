package de.luandtong.sailor.datenbank.wg.clientInterface;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClientInterfaceDBRepository extends CrudRepository<ClientInterfaceDTO, Long> {

    ClientInterfaceDTO findClientInterfaceDTOByUuid(UUID uuid);

    List<ClientInterfaceDTO> findAll();

    ClientInterfaceDTO findClientInterfaceDTOByClientName(String clientName);

    List<ClientInterfaceDTO> findClientInterfaceDTOSByServerInterfaceUUID(UUID serverInterfaceUUID);

    ClientInterfaceDTO getClientInterfaceDTOByClientNameAndServerInterfaceUUID(String clientName, UUID serverInterfaceUUID);

    @Query("DELETE FROM ClientInterfaces WHERE client_Name=:clientName AND Server_Interface_UUID=:serverInterfaceUUID")
    void deleteByClientNameAndServerInterfaceUUID(@Param("clientName") String clientName, @Param("serverInterfaceUUID") UUID serverInterfaceUUID);

    @Query("DELETE FROM ClientInterfaces WHERE uuid=:uuid")
    void deleteByUuid(@Param("uuid") UUID uuid);

    boolean existsByClientName(String clientName);

}
