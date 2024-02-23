package de.luandtong.sailor.controller.authentication;

import de.luandtong.sailor.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.regex.Pattern;

// 定义控制器，处理认证相关的请求
// Define a controller to handle authentication-related requests
@Controller
@RequestMapping("/")
public class AuthenticationController {

    private final UserService userService;

    // 通过构造器注入UserService
    // Inject UserService via the constructor
    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    // 处理根URL的GET请求，返回首页
    // Handle GET requests for the root URL, return the index page
    @GetMapping("")
    public String getIndex() {
        return "index";
    }

    // 处理/sign-in的GET请求，如果已存在用户，则重定向到登录页
    // Handle GET requests to /sign-in. If users exist, redirect to the login page
    @GetMapping("/sign-in")
    public String signIn() {
        if (userService.hasUsers()) {
            return "redirect:/log-in";
        }
        return "sign-in";
    }

    // 处理/sign-in的POST请求，用于注册新用户
    // Handle POST requests to /sign-in for registering new users
    @PostMapping("/sign-in")
    public String register(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        System.out.println("User Login");
        System.out.println("username " + username);
        System.out.println("password " + password);

        // 验证用户名和密码不为空
        // Validate username and password are not empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username and password cannot be empty");
            return "redirect:/sign-in";
        }

        // 验证输入是否合法
        // Validate if the input is valid
        if (isInputValid(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "The input contains illegal characters. Please use only English characters, numbers, point, underscores or dashes");
            return "redirect:/sign-in";
        }

        // 验证用户名长度
        // Validate the length of the username
        if (username.length() < 5 || username.length() > 20) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username length must be greater than 5 and less than 20");
            return "redirect:/sign-in";
        }

        // 验证密码长度
        // Validate the length of the password
        if (password.length() < 5 || password.length() > 20) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password length must be greater than 5 and less than 20");
            return "redirect:/sign-in";
        }

        // 保存用户信息
        // Save user information
        userService.saveUser(username, password);
        return "redirect:/log-in";
    }

    // 处理/log-in的GET请求，返回登录页
    // Handle GET requests to /log-in, return the login page
    @GetMapping("/log-in")
    public String logIn() {
        if (!userService.hasUsers()) {
            return "redirect:/sign-in";
        }
        return "log-in"; // 登录页面 Login page
    }

    // 验证输入是否含有非法字符
    // Validate if the input contains illegal characters
    boolean isInputValid(String input) {
        // 正则表达式，允许字母、数字、下划线，点和横杠
        // Regex for allowed characters: letters, numbers, underscores, dots, and dashes
        String regex = "^[a-zA-Z0-9._-]+$";
        return !Pattern.matches(regex, input);
    }
}
