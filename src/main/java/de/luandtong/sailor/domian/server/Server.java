package de.luandtong.sailor.domian.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static de.luandtong.sailor.domian.server.Command.run;

public class Server {

    boolean serverNeedsInitialization;


    public void initializeServer() throws IOException, InterruptedException {

        if(!this.serverNeedsInitialization){
            return;
        }

        createConfigFileStoragePath();

        modifySysctlConfFile();

        installSoftware();

        this.serverNeedsInitialization = false;
    }

    private void modifySysctlConfFile() throws IOException, InterruptedException {
        // 启用 IP 转发
        // 读取 /etc/sysctl.conf 文件的内容
        List<String> lines = Files.readAllLines(Paths.get("/etc/sysctl.conf"));

        // 检查 "#net.ipv4.ip_forward=1" 是否存在
        boolean commentedIpForwardExists = lines.stream()
                .map(String::trim)
                .anyMatch(line -> line.equals("#net.ipv4.ip_forward=1"));

        // 检查 "net.ipv4.ip_forward=1" 是否存在并且没有被注释掉
        boolean ipForwardExists = lines.stream()
                .map(String::trim)
                .anyMatch(line -> line.equals("net.ipv4.ip_forward=1"));

        // 根据条件来决定是否添加 "net.ipv4.ip_forward=1"
        if (commentedIpForwardExists || !ipForwardExists) {
            Files.write(Paths.get("/etc/sysctl.conf"), "net.ipv4.ip_forward=1\n".getBytes(), StandardOpenOption.APPEND);
        }

        // 应用 IP 转发设置
        run("sudo sysctl -p");
    }

    private void createConfigFileStoragePath() throws IOException, InterruptedException {
        //创建路径
        Path path = Paths.get("/etc/wireguard/clients/");
        if (!Files.exists(path)) {
            // 如果不存在，则创建目录
            run("sudo mkdir /etc/wireguard/clients/");
        }
    }

    private void installSoftware() throws IOException, InterruptedException {
        String packageManager = getPackageManager();

        // 先更新包管理器
        run("sudo " + packageManager + " update");
        // 安装 WireGuard
        run("sudo " + packageManager + " install -y wireguard");
        // 安装 qrencode
        run("sudo " + packageManager + " install -y qrencode");
    }

    private String getPackageManager() throws IOException, InterruptedException {
        String osInfo = run("cat /etc/os-release");
        return switch (osInfo) {
            case String info when info.contains("Ubuntu") -> "apt";
            case String info when info.contains("CentOS") -> "dnf";
            case String info when info.contains("Fedora") -> "dnf";
            case String info when info.contains("openSUSE") -> "zypper";
            default -> "apt";
//                 抛出异常或记录错误
//                throw new IllegalStateException("Unsupported Linux distribution");
        };
    }


}
