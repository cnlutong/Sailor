package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.wg.ClientInterface;
import de.luandtong.sailor.repository.wg.ClientInterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientInterfaceService {


    @Autowired
    private ClientInterfaceRepository clientInterfaceRepository;


    ClientInterface findClientInterfaceByClientName(String clientName) {
        return clientInterfaceRepository.findClientInterfaceByClientName(clientName);
    }

    void saveClientInterface(UUID uuid, String clientName, UUID interfacekeyuuid, UUID serverInterfaceUUID, String address, String dns, String persistentKeepalive) {
        clientInterfaceRepository.save(uuid, clientName, interfacekeyuuid, serverInterfaceUUID, address, dns, persistentKeepalive);
    }

    public String getServerInterfaceConfig(UUID uuid, String clientName, UUID serverInterfaceUUID, String address, String dns, String persistentKeepalive,
                                           String privateKey, String publicKey, String publicIP, String listenPort) {
        ClientInterface clientInterface = new ClientInterface(uuid, clientName, serverInterfaceUUID, address, dns, persistentKeepalive);
        return clientInterface.creativeInterfaceConfFile(privateKey, publicKey, publicIP, listenPort);

    }



    List<String> findClientInterfaceNamesByServerInterfaceUUID(UUID serverInterfaceUUID) {
        return clientInterfaceRepository.findClientInterfaceNamesByServerInterfaceUUID(serverInterfaceUUID);
    }

    List<ClientInterface> findClientInterfacesByServerInterfaceUUID(UUID serverInterfaceUUID) {
        return clientInterfaceRepository.findClientInterfacesByServerInterfaceUUID(serverInterfaceUUID);
    }

    // 忽略子网掩码


    public int getLastAddressFromClientInterfaces(List<ClientInterface> clientInterfaces) {
        return clientInterfaces.stream()
                .map(ClientInterface::getAddress)
                // 分割IP地址和子网掩码，然后进一步处理IP地址部分
                .map(address -> address.substring(0, address.indexOf('/')))
                .map(address -> address.split("\\."))
                .mapToInt(parts -> {
                    // 尝试解析IP地址最后一部分的数字
                    try {
                        // 仅解析最后一个部分，不考虑子网掩码
                        return Integer.parseInt(parts[parts.length - 1]);
                    } catch (NumberFormatException e) {
                        // 对于解析异常，这里可以记录日志或进行其他错误处理
                        // 此处返回0，因为已经尝试处理异常情况
                        return 0;
                    }
                })
                // 查找最大值，如果没有找到有效的最大值，则返回1
                .max()
                .orElse(1);
    }



}
