package de.luandtong.sailor.service.user;

import de.luandtong.sailor.domian.user.User;
import de.luandtong.sailor.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;


@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.username(), user.encode(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

    //    对用户密码进行哈希散列
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    //    检查输入的密码和存储的哈希值
    public boolean checkPassword(String rawPassword, String storedEncodedPassword) {
        return passwordEncoder.matches(rawPassword, storedEncodedPassword);
    }

    public void saveUser(String username, String password) {
        userRepository.saveUser(UUID.randomUUID(), username, passwordEncoder.encode(password));
    }

    public User findByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public boolean hasUsers() {
        return userRepository.hasUsers();
    }
}
