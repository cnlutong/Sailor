package de.luandtong.sailor.domian.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static de.luandtong.sailor.domian.server.Command.run;

public class Server {

    boolean serverNeedsInitialization;


    //    初始化
    public void initializeServer() throws IOException, InterruptedException {

        if (!this.serverNeedsInitialization) {
            return;
        }

        createConfigFileStoragePath();

        modifySysctlConfFile();

        installSoftware();

        this.serverNeedsInitialization = false;
    }


    //    启用服务
    public void enableServer(String name) throws IOException, InterruptedException {
        String serverCong = "server_" + name + ".conf";
        // 启用 WireGuard 服务
        run("sudo systemctl enable wg-quick@" + serverCong);
    }

    //    启动服务
    public void startServer(String name) throws IOException, InterruptedException {
        String serverCong = "server_" + name + ".conf";
        // 启动 WireGuard 服务
        run("sudo systemctl start wg-quick@" + serverCong);
    }

    //    重启服务
    public void restartServer(String name) throws IOException, InterruptedException {
        String serverCong = "server_" + name;
        // 重启 WireGuard 服务以应用更改
        run("sudo systemctl restart wg-quick@" + serverCong + ".service");
    }

    //    创建密钥
    public List<String> generateKey(boolean isServer, String name) throws IOException, InterruptedException {
        String privateKey;
        String publicKey;

        if (isServer) {
            String serverName = "server_" + name;

            run("wg genkey | sudo tee /etc/wireguard/" + serverName + "_private.key | wg pubkey | sudo tee /etc/wireguard/" + serverName + "_public.key");

            // 读取生成的私钥和公钥
            privateKey = run("sudo cat /etc/wireguard/" + serverName + "_private.key");
            publicKey = run("sudo cat /etc/wireguard/" + serverName + "_public.key");

        } else {
            String clientName = "client_" + name;

            // 生成客户端私钥和公钥
            run("wg genkey | sudo tee /etc/wireguard/clients/" + clientName + "_private.key | wg pubkey | sudo tee /etc/wireguard/clients/" + clientName + "_public.key");

            // 读取客户端生成的私钥和公钥
            privateKey = run("sudo cat /etc/wireguard/clients/" + clientName + "_private.key");
            publicKey = run("sudo cat /etc/wireguard/clients/" + clientName + "_public.key");
        }

        ArrayList<String> key = new ArrayList<>();
        key.add(publicKey);
        key.add(privateKey);
        return key;
    }

    //    写入新的接口配置文件
    public void writeNewConfigFile(boolean isServer, String name, String conf) throws IOException, InterruptedException {
        if (isServer) {
            String serverName = "server_" + name;
            // 检查 /etc/wireguard/wg0.conf 文件是否存在
            Path wg0ConfPath = Paths.get("/etc/wireguard/" + serverName + ".conf");
            if (Files.exists(wg0ConfPath)) {
                // 如果存在，则删除文件
                run("sudo rm /etc/wireguard/" + serverName + ".conf");
            }
            // 写入 WireGuard 配置文件
            Files.write(Paths.get("/etc/wireguard/" + serverName + ".conf"), conf.getBytes());
        } else {
            String clientName = "client_" + name;
            // 写入客户端 WireGuard 配置文件
            Files.write(Paths.get("/etc/wireguard/clients/" + clientName + "_wg0.conf"), conf.getBytes());
        }
        // 修改 WireGuard 配置文件和密钥的权限
        run("sudo chmod -R 600 /etc/wireguard/");
    }

    //    续写配置文件
    public void appendServerConfigFile(String serverName, String clientName, String clientPublicKey, String clientAddress) throws IOException, InterruptedException {

        String serverCong = "server_" + serverName + ".conf";
        // 更新服务器 WireGuard 配置文件以添加客户端信息
        String peerInfo = "\n" + "[Peer]\n" + "PublicKey = " + clientPublicKey + "\n" + "AllowedIPs = " + clientAddress + "\n";

        Files.write(Paths.get("/etc/wireguard/" + serverCong), peerInfo.getBytes(), StandardOpenOption.APPEND);

    }

    private void modifySysctlConfFile() throws IOException, InterruptedException {
        // 启用 IP 转发
        // 读取 /etc/sysctl.conf 文件的内容
        List<String> lines = Files.readAllLines(Paths.get("/etc/sysctl.conf"));

        // 检查 "#net.ipv4.ip_forward=1" 是否存在
        boolean commentedIpForwardExists = lines.stream().map(String::trim).anyMatch(line -> line.equals("#net.ipv4.ip_forward=1"));

        // 检查 "net.ipv4.ip_forward=1" 是否存在并且没有被注释掉
        boolean ipForwardExists = lines.stream().map(String::trim).anyMatch(line -> line.equals("net.ipv4.ip_forward=1"));

        // 根据条件来决定是否添加 "net.ipv4.ip_forward=1"
        if (commentedIpForwardExists || !ipForwardExists) {
            Files.write(Paths.get("/etc/sysctl.conf"), "net.ipv4.ip_forward=1\n".getBytes(), StandardOpenOption.APPEND);
        }

        // 应用 IP 转发设置
        run("sudo sysctl -p");
    }

    private String getDefaultEth() throws IOException, InterruptedException {
        // 获取默认网卡接口
        return run("ip -o -4 route show to default | awk '{print $5}'");
    }

    private void createConfigFileStoragePath() throws IOException, InterruptedException {
        //创建路径
//        Path path = Paths.get("/etc/wireguard/clients/");
//        if (!Files.exists(path)) {
//            // 如果不存在，则创建目录
//            run("sudo mkdir /etc/wireguard/clients/");
//        }

        run("sudo mkdir /etc/wireguard/clients/");
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
