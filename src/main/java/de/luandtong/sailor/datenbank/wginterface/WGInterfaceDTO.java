package de.luandtong.sailor.datenbank.wginterface;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("WGInterfaces")
public class WGInterfaceDTO {
    @Id
    private Long id;
    private UUID uuid;

    private String username;

    private String encode;

    private LocalDateTime time;
}
