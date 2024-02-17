package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.wg.InterfaceKey;
import de.luandtong.sailor.repository.wg.InterfaceKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class InterfaceKeyServiceTest {

    @Mock
    private InterfaceKeyRepository interfaceKeyRepository;

    @InjectMocks
    private InterfaceKeyService interfaceKeyService;

    private UUID uuid;
    private String publicKey;
    private String privateKey;
    private InterfaceKey interfaceKey;

    @BeforeEach
    void setUp() {
        openMocks(this);
        uuid = UUID.randomUUID();
        publicKey = "publicKey";
        privateKey = "privateKey";
        interfaceKey = new InterfaceKey(uuid, publicKey, privateKey);
    }

    @Test
    void saveShouldInvokeRepositorySave() {
        doNothing().when(interfaceKeyRepository).save(uuid, publicKey, privateKey);

        interfaceKeyService.save(uuid, publicKey, privateKey);

        verify(interfaceKeyRepository, times(1)).save(uuid, publicKey, privateKey);
    }

    @Test
    void findWGInterfaceKeyByUuidShouldReturnInterfaceKey() {
        when(interfaceKeyRepository.findWGInterfaceKeyByUuid(uuid)).thenReturn(interfaceKey);

        InterfaceKey result = interfaceKeyService.findWGInterfaceKeyByUuid(uuid);

        assertEquals(interfaceKey, result);
        verify(interfaceKeyRepository, times(1)).findWGInterfaceKeyByUuid(uuid);
    }

    @Test
    void getPublicKeyByUuidShouldReturnPublicKey() {
        when(interfaceKeyRepository.findWGInterfaceKeyByUuid(uuid)).thenReturn(interfaceKey);

        String result = interfaceKeyService.getPublicKeyByUuid(uuid);

        assertEquals(publicKey, result);
        verify(interfaceKeyRepository, times(1)).findWGInterfaceKeyByUuid(uuid);
    }

    @Test
    void deleteByUuidShouldInvokeRepositoryDelete() {
        doNothing().when(interfaceKeyRepository).deleteByUuid(uuid);

        interfaceKeyService.deleteByUuid(uuid);

        verify(interfaceKeyRepository, times(1)).deleteByUuid(uuid);
    }
}
