package de.luandtong.sailor.domian.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Command {

    public static String run(String command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("/bin/bash", "-c", command).start();
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString().trim();
    }
}
