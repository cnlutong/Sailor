package de.luandtong.sailor.datenbank.user;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserDBRepository extends CrudRepository<UserDTO, UUID> {

    UserDTO findUserDTOByUuid(UUID uuid);
    UserDTO findUserDTOByUsername(String username);

    boolean existsUserDTOByUsername(String username);

    long count();

}
