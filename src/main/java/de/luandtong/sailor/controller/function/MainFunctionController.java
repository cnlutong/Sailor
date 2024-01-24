package de.luandtong.sailor.controller.function;

import de.luandtong.sailor.service.server.ClientInterfaceService;
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
    @Autowired
    private ClientInterfaceService clientInterfaceService;


//    @GetMapping("/home")
//    public String home(@RequestParam String selectedInterface, Model model) {
//        // 这里处理selectedInterface
//        // 例如，将它添加到模型中，以便在视图中显示
//        model.addAttribute("selectedInterface", selectedInterface);
//
//        // 返回home视图
//        return "home";
//    }

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
        serverService.initializeServer();
        serverService.creativeServerInterface(serverName, serverInterfaceName, address, listenPort, ethPort);
        // 配置保存后，重定向到另一个页面或返回信息
        return "redirect:/select"; // 修改为您的目标页面
    }

    @GetMapping("/select")
    public String selectInterface(Model model) {
        if (!serverService.hasServer()) {
            return "redirect:/init";
        }

        List<String> interfaces = serverService.getServerInterfaceNames();
        model.addAttribute("interfaces", interfaces);
        return "select";
    }

    @PostMapping("/select")
    public String processSelectedInterface(@RequestParam String selectedInterface) {
        return "redirect:/home?selectedInterface=" + selectedInterface;
    }

    @GetMapping("/home")
    public String home(@RequestParam(required = false) String selectedInterface, Model model) {
        if (selectedInterface != null && !selectedInterface.isEmpty()) {
            // 假设 serverService 有方法来获取ServerInterface的详细信息
            model.addAttribute("serverInterfaceName", selectedInterface);
            model.addAttribute("publicKey", serverService.getPublicKey(selectedInterface));
            model.addAttribute("address", serverService.getAddress(selectedInterface));
            model.addAttribute("listenPort", serverService.getListenPort(selectedInterface));
            model.addAttribute("ethPort", serverService.getEthPort(selectedInterface));
            List<String> clientNames = ;
            model.addAttribute("clients", clientNames);
        }
        return "home";
    }
}
