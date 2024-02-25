package de.luandtong.sailor.controller.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import de.luandtong.sailor.service.server.ServerService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class MainFunctionControllerTest {

    @Mock
    private ServerService serverService;

    @InjectMocks
    private MainFunctionController controller;

    @BeforeEach
    void setUp() {
        // 初始化Mocks
        serverService = mock(ServerService.class);
        controller = new MainFunctionController(serverService);
    }
    @Test
    void testGetInitConfigPage() {
        MainFunctionController controller = new MainFunctionController(null); // 由于这个方法不使用serverService，可以传入null
        String viewName = controller.getInitConfigPage();
        assertEquals("init", viewName, "Expected view name did not match");
    }
    @Test
    void testSubmitConfigWithEmptyInterfaceName() throws IOException, InterruptedException {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String result = controller.submitConfig("", "10.0.0.1", "51820", null, redirectAttributes);
        assertEquals("redirect:/init", result);
        verify(redirectAttributes).addFlashAttribute(eq("errorMessage"), anyString());
    }


}
