package de.luandtong.sailor.controller.function;

import de.luandtong.sailor.service.server.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/")
public class MainFunctionController {

    @Autowired
    private ServerService serverService;

    @GetMapping("/init")
    public String getInitConfigPage() {
        // 返回初始化配置页面
        return "init";
    }

    @PostMapping("/init")
    public String submitConfig(
            @RequestParam String serverInterfaceName,
            @RequestParam(defaultValue = "10.0.0.1") String address,
            @RequestParam(defaultValue = "51820") String listenPort,
            @RequestParam(required = false) String ethPort, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {


        if (serverInterfaceName == null || serverInterfaceName.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "The input cannot be empty");
            return "redirect:/init";
        }
        if (isInputValid(serverInterfaceName)) {
            redirectAttributes.addFlashAttribute("errorMessage", "The input contains illegal characters. Please use only English characters, numbers, underscores or dashes.");
            return "redirect:/init";
        }

        if (serverInterfaceName.length() > 15) {
            redirectAttributes.addFlashAttribute("errorMessage", "The interface name cannot be longer than 15 characters.");
            return "redirect:/init";
        }

        // 输出接收到的数据
        System.out.println("User add new Interface");
        System.out.println("Post Server Interface Name: " + serverInterfaceName);
        System.out.println("Post Address: " + address + "/24");
        System.out.println("Post Listen Port: " + listenPort);
        System.out.println("Post Eth Port: " + (ethPort != null ? ethPort : "If not provided, the system will obtain the default address"));

        if (ethPort != null && ethPort.isEmpty()) {
            ethPort = serverService.getDefaultEth();
            System.out.println("Default ethPort: " + ethPort);
        }

        if (serverService.hasServerInterfaceByServername(serverInterfaceName)) {
            redirectAttributes.addFlashAttribute("errorMessage", "The server interface already exists.");
            return "redirect:/init";
        }

        if (!serverService.isValidIPAddress(address)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please enter a valid IP address for the server");
            return "redirect:/init";
        }
        if (!serverService.isPrivateIPAddress(address)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please use the intranet address for the server");
            return "redirect:/init";
        }

        if (serverService.hasServerInterfaceByAddress(address + "/24")) {
            redirectAttributes.addFlashAttribute("errorMessage", "The server address is already occupied");
            return "redirect:/init";
        }

        if (serverService.isCommonlyUsedPort(listenPort)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Do not use commonly used ports.");
            return "redirect:/init";
        }

        if (serverService.hasServerInterfaceByListenPortAndEthPort(listenPort, ethPort)) {
            redirectAttributes.addFlashAttribute("errorMessage", "The port of this network interface is already occupied.");
            return "redirect:/init";
        }


        serverService.creativeServerInterface(serverInterfaceName, address + "/24", listenPort, ethPort);
        return "redirect:/select";
    }

    @GetMapping("/select")
    public String selectInterface(Model model) throws IOException, InterruptedException {
        //初始化
        serverService.initializeServer();

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

    @PostMapping("/deleteInterface")
    public String deleteInterface(@RequestParam String selectedInterface) throws IOException, InterruptedException {
        System.out.println("deleteInterface: " + selectedInterface);

        serverService.deleteServerInterface(selectedInterface);
        return "redirect:/select"; // 删除后重定向回接口选择页面
    }


    @GetMapping("/home")
    public String home(@RequestParam(required = false) String selectedInterface, Model model) throws IOException, InterruptedException {
        System.out.println("User selected Interface :  " + selectedInterface);
        if (selectedInterface != null && !selectedInterface.isEmpty()) {
            // 假设 serverService 有方法来获取ServerInterface的详细信息
            model.addAttribute("selectedInterface", selectedInterface);
            model.addAttribute("serverInterfaceName", selectedInterface);
            model.addAttribute("publicKey", serverService.getServerInterfacePublicKey(selectedInterface));
            model.addAttribute("address", serverService.getServerInterfaceAddress(selectedInterface));
            model.addAttribute("listenPort", serverService.getServerInterfaceListenPort(selectedInterface));
            model.addAttribute("ethPort", serverService.getServerInterfaceEthPort(selectedInterface));

            serverService.startServer(selectedInterface);

            List<String> clientNames = serverService.findClientInterfaceNamesByServerInterfaceName(selectedInterface);
            System.out.println("Display all ClientInterfaces form this ServerInterface: " + clientNames);

            model.addAttribute("clients", clientNames);
        }
        return "home";
    }

    @PostMapping("/addClientInterface")
    public String addClientInterface(@RequestParam String clientName, @RequestParam String selectedInterface, RedirectAttributes redirectAttributes) throws Exception {

        if (clientName != null && isInputValid(clientName)) {
            // 如果输入无效，添加错误消息到重定向属性
            redirectAttributes.addFlashAttribute("errorMessage", "The input contains illegal characters. Please use only English characters, numbers, underscores or dashes.");
            return "redirect:/home?selectedInterface=" + selectedInterface;
        }
        System.out.println("User add new selectedInterface");
        System.out.println("Post selectedInterface: " + selectedInterface);
        System.out.println("Post clientName: " + clientName);
        System.out.println("client full Name: " + selectedInterface + "-" + clientName);

        int temp = serverService.creativeClientInterface(selectedInterface, selectedInterface + "-" + clientName);

        if (temp == -1) {
            redirectAttributes.addFlashAttribute("errorMessage", "No more client addresses available.");
            return "redirect:/home?selectedInterface=" + selectedInterface;
        }

        if (temp == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "A client with the same name already exists.");
            return "redirect:/home?selectedInterface=" + selectedInterface;
        }

        // 添加操作成功的反馈消息
        redirectAttributes.addFlashAttribute("successMessage", "Client has been added successfully");

        // 重定向到主页
        return "redirect:/home?selectedInterface=" + selectedInterface;
    }

    @PostMapping("/deleteClientInterface")
    public String deleteClientInterface(@RequestParam("clientName") String clientName, @RequestParam String selectedInterface) throws IOException, InterruptedException {
        System.out.println("Delete client interface: " + clientName);
        serverService.deleteClientInterface(clientName, selectedInterface);
        return "redirect:/home?selectedInterface=" + selectedInterface;
    }

    public boolean isInputValid(String input) {
        // 正则表达式，允许字母、数字、下划线和横杠
        String regex = "^[a-zA-Z0-9_-]+$";
        return !Pattern.matches(regex, input);
    }

}
