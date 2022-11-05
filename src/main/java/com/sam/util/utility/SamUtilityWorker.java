package com.sam.util.utility;

import com.sam.util.service.SamPasswordDropper;

import java.io.IOException;
import java.util.Scanner;

public final class SamUtilityWorker {

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

                dropper.refreshCurrentUserPassword(username2, newPassword);
                break;
            case 4:
                break;
            default:
                System.out.print("Wrong content inputted. Try again.");
                break;
        }
    }

}
