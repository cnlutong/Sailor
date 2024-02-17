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
        if (userService.hasUsers()) {
            return "redirect:/log-in";
        }
        return "sign-in";
    }


    @PostMapping("/sign-in")
    public String register(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        System.out.println("username " + username);
        System.out.println("password " + password);


        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username and password cannot be empty");
            return "redirect:/sign-in";
        }

        if (isInputValid(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "The input contains illegal characters. Please use only English characters, numbers, point, underscores or dashes");
            return "redirect:/sign-in";

        }

        if (username.length() < 5 || username.length() > 20) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username length must be greater than 5 and less than 20");
            return "redirect:/sign-in";

        }

        if (password.length() < 5 || password.length() > 20) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password length must be greater than 5 and less than 20");
            return "redirect:/sign-in";

        }

        userService.saveUser(username, password);
        return "redirect:/log-in";

    }

    @GetMapping("/log-in")
    public String logIn() {
        if (!userService.hasUsers()) {
            return "redirect:/sign-in";
        }
        return "log-in"; // 登录页面
    }

    boolean isInputValid(String input) {
        // 正则表达式，允许字母、数字、下划线，点和横杠
        String regex = "^[a-zA-Z0-9._-]+$";
        return !Pattern.matches(regex, input);
    }

}
