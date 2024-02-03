package de.luandtong.sailor.controller.function;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("")
public class FileDownloadController {

    private final Path baseDir = Paths.get("/etc/wireguard/clients/");
    private final Path qrBaseDir = Paths.get("/etc/wireguard/clients/qr/");


    @GetMapping("/download/{clientName}")
    public ResponseEntity<Resource> downloadConfigFile(@PathVariable String clientName) {
        try {
            Path filePath = baseDir.resolve("client_" + clientName + ".conf").normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                System.out.println("resource is exist : " + resource);
                String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/download/qr/{clientName}")
    public ResponseEntity<Resource> downloadQRFile(@PathVariable String clientName) {
        try {
            Path filePath = qrBaseDir.resolve("client_" + clientName + ".png").normalize();
            System.out.println("QR: " +filePath);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}