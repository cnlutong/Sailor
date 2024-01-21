package de.luandtong.sailor.controller.authentication;

import de.luandtong.sailor.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String getIndex() {
        return "index";
    }


    @GetMapping("/sign-in")
    public String signIn() {
        // 如果已存在用户，则重定向到登录页面
        if (userService.hasUsers()) {
            return "redirect:/log-in";
        }
        return "sign-in";
    }


    @PostMapping("/sign-in")
    public String register(@RequestParam String username, @RequestParam String password) {
        System.out.println("username " + username);
        System.out.println("password " + password);
        userService.saveUser(username, password);
        return "redirect:/log-in"; // 注册成功后重定向到登录页面
    }

    @GetMapping("/log-in")
    public String logIn() {
        return "log-in"; // 登录页面
    }


}
