package de.luandtong.sailor.domian.server;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QRCodeGenerator {

    public static String filePath = "/etc/wireguard/clients/qr/";
    public static int width = 512;
    public static int height = 512;


    public static void generateQRCodeImage(String conf, String fileName) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(conf, BarcodeFormat.QR_CODE, width, height);

        Path baseDir = Paths.get(filePath);
        Path path = baseDir.resolve(fileName + ".png");

        // 确保父目录存在
        Files.createDirectories(baseDir);

        // 保存二维码图片
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

}
