package de.luandtong.sailor.service.user;

import de.luandtong.sailor.domian.user.User;
import de.luandtong.sailor.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    // 已提供的测试方法...

    @Test
    void findByUsernameShouldReturnUserWhenExists() {
        String username = "existingUser";
        User expectedUser = new User(UUID.randomUUID(), username, "hashedPassword");
        when(userRepository.getUserByUsername(username)).thenReturn(expectedUser);

        User result = userService.findByUsername(username);

        assertEquals(expectedUser, result, "findByUsername should return the correct user when exists");
    }

    @Test
    void findByUsernameShouldReturnNullWhenUserDoesNotExist() {
        String username = "nonExistingUser";
        when(userRepository.getUserByUsername(username)).thenReturn(null);

        User result = userService.findByUsername(username);

        assertNull(result, "findByUsername should return null when user does not exist");
    }

    @Test
    void hasUsersShouldReturnTrueWhenUsersExist() {
        when(userRepository.hasUsers()).thenReturn(true);

        boolean result = userService.hasUsers();

        assertTrue(result, "hasUsers should return true when users exist");
    }

    @Test
    void hasUsersShouldReturnFalseWhenNoUsersExist() {
        when(userRepository.hasUsers()).thenReturn(false);

        boolean result = userService.hasUsers();

        assertFalse(result, "hasUsers should return false when no users exist");
    }
}
