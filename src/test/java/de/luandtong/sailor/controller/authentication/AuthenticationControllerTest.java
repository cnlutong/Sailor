package de.luandtong.sailor.controller.authentication;

import de.luandtong.sailor.service.user.UserService;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthenticationControllerTest {

    @MockBean
    private UserService userService;


    @Test
    public void whenUsernameOrPasswordIsEmpty_thenRedirectWithErrorMessage() {
        // 模拟输入参数和重定向属性
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        // 实例化控制器
        AuthenticationController controller = new AuthenticationController(userService);

        // 用户名为空
        String viewName = controller.register("", "password", redirectAttributes);
        assertThat(viewName).isEqualTo("redirect:/sign-in");
        assertThat(redirectAttributes.getFlashAttributes()).containsKey("errorMessage");

        // 密码为空
        viewName = controller.register("username", "", redirectAttributes);
        assertThat(viewName).isEqualTo("redirect:/sign-in");
        assertThat(redirectAttributes.getFlashAttributes()).containsKey("errorMessage");
    }


}