package de.luandtong.sailor.domian.user;

import java.util.UUID;

public record User(UUID uuid,
                   String username,

                   String encode) {
}
