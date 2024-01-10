package de.luandtong.sailor.repository.user;

import de.luandtong.sailor.domian.user.User;

import java.util.UUID;

public interface UserRepository {
    void saveUser(UUID uuid, String username, String encode);

    User findByUsername(String username);

    boolean hasUsers();


}
