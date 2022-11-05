package com.sam.util.service;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class SamPasswordDropper implements Droppable {

    private List<String> users;

    private static ProcessBuilder commandsExecutor;

    @Override
    public List<String> getAllUsers() throws IOException {

        commandsExecutor = new ProcessBuilder(
                "cmd.exe", "/c", "chcp 861 && net user");

        Process netUserProcess = commandsExecutor.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(netUserProcess.getInputStream()));

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while (true) {
            line = r.readLine();

            if (line == null) {
                break;
            }

            if (line.contains("Administrator")) {
                String clearedString = StringUtils.normalizeSpace(line);

                stringBuilder.append(clearedString);
            }

        }

        users = Arrays.asList(
                stringBuilder.toString().split(" ")
        );

        return users;
    }

    @Override
    public void getCurrentUserInfo(String userName) throws IOException {

        commandsExecutor = new ProcessBuilder(
                "cmd.exe", "/c", "chcp 861 && net user " + userName);

        Process netUserProcess = commandsExecutor.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(netUserProcess.getInputStream()));

        String line;

        while (true) {
            line = r.readLine();

            if (line == null || line.contains("Memberships")) {
                break;
            }

            System.out.println(line);

        }
    }

    @Override
    public void refreshCurrentUserPassword(String userName, String newPassword) throws IOException {
        commandsExecutor = new ProcessBuilder(
                "cmd.exe", "/c", "chcp 861 && net user " + userName + " " + newPassword);

        Process netUserProcess = commandsExecutor.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(netUserProcess.getInputStream()));

        String line;

        while (true) {
            line = r.readLine();

            if (line == null || line.contains("Memberships")) {
                break;
            }

            System.out.println(line);

        }
    }
}
