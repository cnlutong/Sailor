package de.luandtong.sailor.datenbank.wg.serverInterface;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ServerInterfaceDBRepository extends CrudRepository<ServerInterfaceDTO, Long> {

    ServerInterfaceDTO findByServerInterfaceName(String serverInterfaceName);

    ServerInterfaceDTO findServerInterfaceDTOByUuid(UUID uuid);

    List<ServerInterfaceDTO> findAll();


    long count();

    boolean existsByServerInterfaceName(String serverInterfaceName);

    boolean existsByAddress(String address);

    boolean existsByListenPort(String listenPort);


    boolean existsByListenPortAndEthPort(String listenPort, String ethPort);

    @Query("DELETE FROM ServerInterfaces WHERE server_Interface_Name=:serverInterfaceName")
    void deleteByServerInterfaceName(@Param("serverInterfaceName") String serverInterfaceName);


}
