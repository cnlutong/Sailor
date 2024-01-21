package de.luandtong.sailor.datenbank.user;

import de.luandtong.sailor.domian.user.User;
import de.luandtong.sailor.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserDBRepository userDBRepository;

    @Override
    public boolean hasUsers() {
        System.out.println(userDBRepository.count());
        return userDBRepository.count() > 0;
    }

    @Override
    public void saveUser(UUID uuid, String username, String encode) {
        userDBRepository.save(new UserDTO(uuid, username, encode));
    }

    @Override
    public User getUserByUsername(String username) {
        return userDBRepository.findUserDTOByUsername(username).toUser();
    }
}
