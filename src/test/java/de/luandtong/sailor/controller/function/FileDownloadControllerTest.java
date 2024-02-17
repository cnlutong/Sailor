package de.luandtong.sailor.controller.function;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FileDownloadControllerTest {

    @MockBean
    private FileDownloadController fileDownloadController;

    @Test
    public void testDownloadConfigFileHappyPath() throws MalformedURLException {
        String clientName = "clientName1";

        Path filePath = Paths.get("/etc/wireguard/clients/" + clientName + ".conf").normalize();
        Resource resource = new UrlResource(filePath.toUri());

        when(fileDownloadController.downloadConfigFile(any(String.class))).thenReturn(ResponseEntity.ok().body(resource));

        ResponseEntity<Resource> response = fileDownloadController.downloadConfigFile(clientName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resource, response.getBody());
    }

    @Test
    public void testDownloadConfigFileUnhappyPath() {
        String clientName = "clientName2";

        when(fileDownloadController.downloadConfigFile(any(String.class))).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<Resource> response = fileDownloadController.downloadConfigFile(clientName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDownloadQRFileHappyPath() throws MalformedURLException {
        String clientName = "clientName1";

        Path filePath = Paths.get("/etc/wireguard/clients/qr/" + clientName + ".png").normalize();
        Resource resource = new UrlResource(filePath.toUri());

        when(fileDownloadController.downloadQRFile(any(String.class))).thenReturn(ResponseEntity.ok().body(resource));

        ResponseEntity<Resource> response = fileDownloadController.downloadQRFile(clientName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resource, response.getBody());
    }

    @Test
    public void testDownloadQRFileUnhappyPath() {
        String clientName = "clientName2";

        when(fileDownloadController.downloadQRFile(any(String.class))).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<Resource> response = fileDownloadController.downloadQRFile(clientName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}