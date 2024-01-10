package de.luandtong.sailor.service.server;

import de.luandtong.sailor.domian.server.Server;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

//服务器初始化相关
@Service
public class ServerService {

    private Server server;

    private void initializeServer() throws IOException, InterruptedException {
        server.initializeServer();
    }

    public void enableServer(String name) throws IOException, InterruptedException {
        server.enableServer(name);
    }

    public void startServer(String name) throws IOException, InterruptedException {
        server.startServer(name);
    }

    public void restartServer(String name) throws IOException, InterruptedException {
        server.restartServer(name);
    }

    public List<String> generateServerKey(String name) throws IOException, InterruptedException {
        return server.generateKey(true, name);

    }

//    public List<String> generateClientKey(String name) throws IOException, InterruptedException {
//        return server.generateKey(false, name);
//    }


}
