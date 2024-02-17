package de.luandtong.sailor.domian.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static de.luandtong.sailor.domian.server.Command.run;

public class Server {

    boolean serverNeedsInitialization;


    //    初始化
    public void initializeServer() throws IOException, InterruptedException {
        this.serverNeedsInitialization = !this.testClients();

        if (!this.serverNeedsInitialization) {
            System.out.println("Server dont need init");
            return;
        }
        System.out.println("Server initing");

        modifySysctlConfFile();

        installSoftware();

        createConfigFileStoragePath();

        this.serverNeedsInitialization = false;
    }

    private boolean testClients() throws IOException, InterruptedException {
        String cmd = "test -d /etc/wireguard/clients/ && echo \"true\" || echo \"false\"";
        return Boolean.parseBoolean(run(cmd));
    }


    //    启用服务
    public void enableServer(String name) throws IOException, InterruptedException {
        // 启用 WireGuard 服务
        run("sudo systemctl enable wg-quick@" + name);
        System.out.println("sudo systemctl enable wg-quick@" + name);
    }

    //    启动服务
    public void startServer(String name) throws IOException, InterruptedException {
        // 启动 WireGuard 服务
        run("sudo systemctl start wg-quick@" + name);
        System.out.println("sudo systemctl start wg-quick@" + name);
    }

    public void disableServer(String name) throws IOException, InterruptedException {
        // 关闭 WireGuard 服务
        run("sudo systemctl disable wg-quick@" + name);
        System.out.println("sudo systemctl disable wg-quick@" + name);
    }

    public void stopServer(String name) throws IOException, InterruptedException {
        // 停止 WireGuard 服务
        run("sudo systemctl stop wg-quick@" + name);
        System.out.println("sudo systemctl stop wg-quick@" + name);
    }

    //    重启服务
    public void restartServer(String name) throws IOException, InterruptedException {
        // 重启 WireGuard 服务以应用更改
        run("sudo systemctl restart wg-quick@" + name + ".service");
        System.out.println("sudo systemctl restart wg-quick@" + name + ".service");
    }


    //    创建密钥
    public List<String> generateKey(boolean isServer, String name) throws IOException, InterruptedException {
        String privateKey;
        String publicKey;

        if (isServer) {
            String serverName = name;

            run("wg genkey | sudo tee /etc/wireguard/" + serverName + "_private.key | wg pubkey | sudo tee /etc/wireguard/" + serverName + "_public.key");

            // 读取生成的私钥和公钥
            privateKey = run("sudo cat /etc/wireguard/" + serverName + "_private.key");
            publicKey = run("sudo cat /etc/wireguard/" + serverName + "_public.key");

        } else {
            String clientName = name;

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
            String serverName = name;
            // 检查 /etc/wireguard/wg0.conf 文件是否存在
            Path wg0ConfPath = Paths.get("/etc/wireguard/" + serverName + ".conf");
            if (Files.exists(wg0ConfPath)) {
                // 如果存在，则删除文件
                run("sudo rm /etc/wireguard/" + serverName + ".conf");
            }
            // 写入 WireGuard 配置文件
            Files.write(Paths.get("/etc/wireguard/" + serverName + ".conf"), conf.getBytes());
        } else {
            String clientName = name;
            // 写入客户端 WireGuard 配置文件
            Files.write(Paths.get("/etc/wireguard/clients/" + clientName + ".conf"), conf.getBytes());
        }
        // 修改 WireGuard 配置文件和密钥的权限
        run("sudo chmod -R 600 /etc/wireguard/");
    }

    //    续写配置文件
    public void appendServerConfigFile(String serverName, String clientName, String clientPublicKey, String clientAddress) throws IOException {

        String serverCong = serverName + ".conf";
        // 更新服务器 WireGuard 配置文件以添加客户端信息
        String peerInfo = "\n" + "#" + clientName + "\n" + "[Peer]\n" + "PublicKey = " + clientPublicKey + "\n" + "AllowedIPs = " + clientAddress + "\n";

        Files.write(Paths.get("/etc/wireguard/" + serverCong), peerInfo.getBytes(), StandardOpenOption.APPEND);

    }

    public void removeClientFromConfig(String serverName, String clientName) throws IOException, InterruptedException {
        Path configPath = Paths.get("/etc/wireguard", serverName + ".conf");
        if (!Files.exists(configPath)) {
            throw new IOException("Config file does not exist: " + configPath);
        }

        List<String> lines = Files.readAllLines(configPath);
        List<String> modifiedLines = new ArrayList<>();
        int skipLines = 0; // 跳过行数计数器

        for (String line : lines) {
            if (skipLines > 0) {
                // 如果当前处于跳过行的状态，则减少计数器并继续循环
                skipLines--;
                continue;
            }
            if (line.trim().startsWith("#" + clientName)) {
                // 如果找到以客户端名称注释开头的行，则设置跳过接下来的3行
                skipLines = 3;
                continue;
            }
            // 将不需要跳过的行添加到modifiedLines
            modifiedLines.add(line);
        }

        // 将更新后的配置内容回写到配置文件
        writeNewConfigFile(true, serverName, String.join(System.lineSeparator(), modifiedLines));

        // 可能需要更新文件权限的命令
        run("sudo chmod 600 " + configPath);
    }

    public void removeClientInterfaceFiles(String clientInterfaceName) throws IOException {
        Stream.of(
                "/etc/wireguard/clients/" + clientInterfaceName + ".conf",
                "/etc/wireguard/clients/" + clientInterfaceName + "_public.key",
                "/etc/wireguard/clients/" + clientInterfaceName + "_private.key",
                "/etc/wireguard/clients/qr/" + clientInterfaceName + ".png"
        ).forEach(filePath -> {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException ignored) {
            }
        });
    }

    public void removeServerInterfaceFiles(String serverInterfaceName) throws IOException {
        // 删除服务器接口的配置文件
        Stream.of(
                "/etc/wireguard/" + serverInterfaceName + ".conf",
                "/etc/wireguard/" + serverInterfaceName + "_public.key",
                "/etc/wireguard/" + serverInterfaceName + "_private.key"
        ).forEach(filePath -> {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException ignored) {
            }
        });

        // 删除客户端配置文件和QR码图片
        Path clientsPath = Paths.get("/etc/wireguard/clients");
        Path qrPath = Paths.get("/etc/wireguard/clients/qr");
        try (Stream<Path> paths = Files.walk(clientsPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains(serverInterfaceName + "-"))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        }
        try (Stream<Path> paths = Files.walk(qrPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains(serverInterfaceName + "-") && path.toString().endsWith(".png"))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        }
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

    public String getDefaultEth() throws IOException, InterruptedException {
        // 获取默认网卡接口
        return run("ip -o -4 route show to default | awk '{print $5}'");
    }

    private void createConfigFileStoragePath() throws IOException, InterruptedException {
        run("sudo mkdir /etc/wireguard/clients/");
        run("sudo mkdir /etc/wireguard/clients/qr");
    }

    private void installSoftware() throws IOException, InterruptedException {
        String packageManager = getPackageManager();

        // 先更新包管理器
        run("sudo " + packageManager + " update");
        // 安装 WireGuard
        run("sudo " + packageManager + " install -y wireguard");
        // 安装 qrencode
//        run("sudo " + packageManager + " install -y qrencode");
        // 安装 curl
        run("sudo " + packageManager + " install -y curl");
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

    public void setServerNeedsInitialization(boolean serverNeedsInitialization) {
        this.serverNeedsInitialization = serverNeedsInitialization;
    }

    public String getServerPublicIP() throws IOException, InterruptedException {
        return run("curl -s ifconfig.me");
    }


}
