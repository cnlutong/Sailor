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

// 文件下载控制器，处理配置文件和QR码文件的下载请求
// File download controller to handle requests for downloading configuration files and QR codes
@RestController
@RequestMapping("")
public class FileDownloadController {

    // 基础目录路径，用于存放配置文件
    // Base directory path for storing configuration files
    private final Path baseDir = Paths.get("/etc/wireguard/clients/");
    // QR码文件的基础目录路径
    // Base directory path for storing QR code files
    private final Path qrBaseDir = Paths.get("/etc/wireguard/clients/qr/");

    // 下载配置文件的GET请求处理方法
    // GET request handler for downloading configuration files
    @GetMapping("/download/{clientName}")
    public ResponseEntity<Resource> downloadConfigFile(@PathVariable String clientName) {
        try {
            // 构造文件路径并标准化
            // Construct and normalize the file path
            Path filePath = baseDir.resolve(clientName + ".conf").normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                // 文件存在且可读时，设置下载头信息并返回文件
                // If the file exists and is readable, set the download header and return the file
                String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                // 文件不存在时返回404
                // Return 404 if the file does not exist
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // 处理文件访问异常，返回500内部服务器错误
            // Handle file access exceptions, return 500 Internal Server Error
            return ResponseEntity.internalServerError().build();
        }
    }

    // 下载QR码文件的GET请求处理方法
    // GET request handler for downloading QR code files
    @GetMapping("/download/qr/{clientName}")
    public ResponseEntity<Resource> downloadQRFile(@PathVariable String clientName) {
        try {
            // 构造QR码文件路径并标准化
            // Construct and normalize the QR code file path
            Path filePath = qrBaseDir.resolve(clientName + ".png").normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // QR码文件存在且可读时，设置下载头信息并返回文件
                // If the QR code file exists and is readable, set the download header and return the file
                String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                // QR码文件不存在时返回404
                // Return 404 if the QR code file does not exist
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // 处理文件访问异常，返回500内部服务器错误
            // Handle file access exceptions, return 500 Internal Server Error
            return ResponseEntity.internalServerError().build();
        }
    }
}
