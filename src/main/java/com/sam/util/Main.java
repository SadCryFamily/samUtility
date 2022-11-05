package com.sam.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        SamPasswordDropper dropper = new SamPasswordDropper();

        SamUtilityWorker worker =
                SamUtilityWorker.initializeComponent(dropper);

        worker.start();

    }
}

interface Droppable {

    List<String> getAllUsers() throws IOException;

    void getCurrentUserInfo(String userName) throws IOException;

    void refreshCurrentUserPassword(String userName, String newPassword) throws IOException;
}

final class SamUtilityWorker {

    private static SamPasswordDropper dropper;

    private Scanner scanner = new Scanner(System.in);

    private Scanner usernameScanner = new Scanner(System.in);

    private Scanner passwordScanner = new Scanner(System.in);

    private Scanner agreeScanner = new Scanner(System.in);

    private SamUtilityWorker() {

    }

    private SamUtilityWorker(SamPasswordDropper dropper) {
        this.dropper = dropper;
    }

    public static SamUtilityWorker initializeComponent(SamPasswordDropper dropper) {
        return new SamUtilityWorker(dropper);
    }

    public void start() throws IOException {
        String logo = "\n" +
                "   ____             __  ____  _ __  _      __         __          \n" +
                "  / __/__ ___ _    / / / / /_(_) / | | /| / /__  ____/ /_____ ____\n" +
                " _\\ \\/ _ `/  ' \\  / /_/ / __/ / /  | |/ |/ / _ \\/ __/  '_/ -_) __/\n" +
                "/___/\\_,_/_/_/_/  \\____/\\__/_/_/   |__/|__/\\___/_/ /_/\\_\\\\__/_/   \n" +
                "                                                                  \n";

        System.out.println(logo);

        System.out.println("1| Get all available users list\n" +
                "2| Get current user info (Username mandatory)\n" +
                "3| Drop/Update password (Username mandatory)\n" +
                "4| Exit\n");

        System.out.print("TYPE: ");
        int categorySelector = scanner.nextInt();

        switch (categorySelector) {
            case 1:
                System.out.print("Available users:\n\n");
                dropper.getAllUsers().forEach(System.out::println);

                System.out.print("Want to view specific user information? (y/n)?: ");
                String usernameChecker = agreeScanner.nextLine();

                switch (usernameChecker) {
                    case "y":

                        System.out.print("ENTER USERNAME: ");
                        String additionalUsername = usernameScanner.nextLine();

                        dropper.getCurrentUserInfo(additionalUsername);

                        System.out.print("Do you want to DROP/UPDATE password for this user? (y/n)?: ");
                        String agreeChecker = agreeScanner.nextLine();

                        switch (agreeChecker) {
                            case "y":
                                System.out.print("ENTER NEW PASSWORD: ");
                                String newPassword = passwordScanner.nextLine();

                                dropper.refreshCurrentUserPassword(additionalUsername, newPassword);
                                break;
                            case "n":
                                System.out.println("Logging out...");
                                break;
                            default:
                                System.out.println("Wrong content inputted. Try again.");
                                break;

                        }

                        break;
                    case "n":
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Wrong content inputted. Try again.");
                        break;
                }

                break;

            case 2:
                System.out.print("ENTER USERNAME: ");
                String username = usernameScanner.nextLine();

                dropper.getCurrentUserInfo(username);

                System.out.print("Do you want to DROP/UPDATE password for this user? (y/n)?: ");

                String agreeChecker = agreeScanner.nextLine();

                switch (agreeChecker) {
                    case "y":
                        System.out.print("ENTER NEW PASSWORD: ");
                        String password = passwordScanner.nextLine();

                        dropper.refreshCurrentUserPassword(username, password);
                        break;
                    case "n":
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Wrong content inputted. Try again.");
                        break;
                }

                break;
            case 3:
                System.out.print("ENTER USERNAME: ");
                String username2 = usernameScanner.nextLine();

                System.out.print("ENTER NEW PASSWORD: ");
                String newPassword = passwordScanner.nextLine();

                dropper.refreshCurrentUserPassword(username2,newPassword);
                break;
            case 4:
                break;
            default:
                System.out.print("Wrong content inputted. Try again.");
                break;
        }
    }

}

class SamPasswordDropper implements Droppable {

    private List<String> users;

    private static ProcessBuilder commandsExecutor;

    @Override
    public List<String> getAllUsers() throws IOException {

        commandsExecutor = new ProcessBuilder(
                "cmd.exe", "/c", "chcp 861 && net user");

        commandsExecutor.redirectErrorStream(true);

        Process p = commandsExecutor.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

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

        commandsExecutor.redirectErrorStream(true);

        Process p = commandsExecutor.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

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

        commandsExecutor.redirectErrorStream(true);

        Process p = commandsExecutor.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

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