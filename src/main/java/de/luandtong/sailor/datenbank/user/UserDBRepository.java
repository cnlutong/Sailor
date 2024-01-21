package de.luandtong.sailor.datenbank.user;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserDBRepository extends CrudRepository<UserDTO, Long> {

    UserDTO findUserDTOByUuid(UUID uuid);

    UserDTO findUserDTOByUsername(String username);

    boolean existsUserDTOByUsername(String username);

    long count();

}
