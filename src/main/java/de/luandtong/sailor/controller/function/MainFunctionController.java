package de.luandtong.sailor.controller.function;

import de.luandtong.sailor.service.server.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainFunctionController {

    @Autowired
    private ServerService serverService;


    @GetMapping("/home")
    public String getHome() {
        if (!serverService.hasServer()) {
            return "redirect:/init";
        }
        return "home";
    }

    @GetMapping("/init")
    public String getInitConfigPage() {
        // 返回初始化配置页面
        return "init";
    }

    @PostMapping("/init")
    public String submitConfig(
            @RequestParam String serverName,
            @RequestParam String serverInterfaceName,
            @RequestParam(defaultValue = "10.0.0.1") String address,
            @RequestParam(defaultValue = "51820") String listenPort,
            @RequestParam(required = false) String ethPort) throws IOException, InterruptedException {
        // 输出接收到的数据
        System.out.println("Server Name: " + serverName);
        System.out.println("Server Interface Name: " + serverInterfaceName);
        System.out.println("Address: " + address);
        System.out.println("Listen Port: " + listenPort);
        System.out.println("Eth Port: " + (ethPort != null ? ethPort : "未提供"));
//        服务器初始化
//        serverService.initializeServer();
        serverService.creativeServerInterface(serverName, serverInterfaceName, address, listenPort, ethPort);
        // 配置保存后，重定向到另一个页面或返回信息
        return "redirect:/select"; // 修改为您的目标页面
    }

    @GetMapping("/select")
    public String selectInterface(Model model) {
        List<String> interfaces = serverService.getServerInterfaceName();
        model.addAttribute("interfaces", interfaces);
        return "select";
    }
//    @GetMapping("/home")
//    public String home(Model model) {
//        model.addAttribute("serverName", "My Server");
//        model.addAttribute("serverInterfaceName", serverService.getServerInterfaceName());
//        return "home";
//    }


}
