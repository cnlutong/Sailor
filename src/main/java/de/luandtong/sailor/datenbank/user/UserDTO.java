package de.luandtong.sailor.datenbank.user;

import de.luandtong.sailor.domian.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("Users")
public class UserDTO {

    @Id
    private Long id;
    private UUID uuid;

    private String username;

    private String encode;

    private LocalDateTime time;


    public UserDTO(UUID uuid, String username, String encode) {
        this.uuid = uuid;
        this.username = username;
        this.encode = encode;
        this.time = LocalDateTime.now();
    }

    public User toUser() {
        return new User(uuid, username, encode);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", encode='" + encode + '\'' +
                '}';
    }
}
