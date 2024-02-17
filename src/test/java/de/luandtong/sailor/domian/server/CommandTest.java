package de.luandtong.sailor.domian.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

//该测试只能在Linux下运行
public class CommandTest {

//    @Test
//    public void testRunEchoCommand() throws IOException, InterruptedException {
//        // 测试 echo 命令，应返回输入的字符串
//        String expectedOutput = "Hello, World!";
//        String command = "echo " + expectedOutput;
//        String output = Command.run(command);
//
//        assertEquals(expectedOutput, output, "The output of the echo command should match the expected output.");
//    }

//    @Test
//    public void testRunInvalidCommand() {
//        // 测试一个无效的命令，应抛出 IOException
//        String command = "someInvalidCommandThatDoesNotExist";
//
//        assertThrows(IOException.class, () -> {
//            Command.run(command);
//        }, "Running an invalid command should throw IOException.");
//    }

//    @Test
//    public void testRunCommandThatExitsWithError() throws IOException, InterruptedException {
//        // 测试一个会以错误码退出的命令
//        String command = "exit 1";
//        // 由于 `Process.waitFor()` 只是等待进程结束并返回退出码，并不直接抛出异常，
//        // 因此此处我们不期待抛出异常，而是通过检查输出来验证命令是否执行。
//        String output = Command.run(command);
//        assertEquals("", output, "The output of a command that exits with error should be empty.");
//    }
}
