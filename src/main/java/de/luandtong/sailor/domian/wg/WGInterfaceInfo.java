package de.luandtong.sailor.domian.wg;

import java.util.UUID;

public record WGInterfaceInfo(UUID uuid, UUID keyuuid, String serverEth, String serverIp, String server_port,) {
}
