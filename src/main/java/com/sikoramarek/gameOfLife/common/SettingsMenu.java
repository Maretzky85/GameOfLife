package com.sikoramarek.gameOfLife.common;

import java.io.IOException;
import java.util.Scanner;


/**
 * Class for console menu
 * Clear screen - check for OS version and call proper clear screen command
 * setingMenu - main menu for console
 * gameMenu - menu for game options board X and Y size, desired start speed,
 * FPS statistics and turn on example board
 * viewMenu - menu for setting console or JavaFX view, and Window size for JavaFX window
 * showHelp - print help to console - listing of all options
 */
public class SettingsMenu {
    private static Scanner scanner = new Scanner(System.in);

    private static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.print("\033[H\033[2J");
            }
        } catch (Exception ignored) {}
    }

    /**
     * handleArgs method for setting application before launch
     *
     * @param args - arguments from console call
     */
    public static void handleArgs(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case "-w":
                    Config.setConsoleView(false);
                    break;
                case "-e":
                    Config.toggleStartExampleModels();
                    break;
                case "-c":
                    Config.setConsoleView(true);
                    break;
                case "-s":
                    Config.togglePrintStatistics();
                    break;
                case "-3":
                    Config.setView3d(true);
                    break;
                case "-h":
                    SettingsMenu.showHelp();
                    System.exit(0);
                    break;
                default:
                    if(!handleComplexArg(arg)){
                        SettingsMenu.showHelp();
                        System.out.println(
                                "List of available arguments:\n\n" +
                                        "-x[] - sets board X size eg. -x200\n" +
                                        "-y[] - sets board Y size eg. -y200\n" +
                                        "-W[] - sets window width eg. -W1000\n" +
                                        "-H[] - sets window height eg. -H1000\n" +
                                        "-w - start app in window with default configuration\n" +
                                        "-3 - set JavaFX view in 3D view\n" +
                                        "-e - start app with example models\n" +
                                        "-c - start app with console output\n" +
                                        "-s - start app with print statistics on\n" +
                                        "-h - display help\n\n");

                        System.exit(0);
                    }
                    break;
            }
        }
    }

    /**
     * function for handling complex arguments
     *
     * @param argument - single argument
     * @return - true if handled properly
     */
    private static boolean handleComplexArg(String argument) {
        try{
            if (argument.startsWith("-x")){
                Config.setXsize(convertToInt(argument));
                return true;
            }else if(argument.startsWith("-y")){
                Config.setYsize(convertToInt(argument));
                return true;
            }else if(argument.startsWith("-W")){
                Config.setRequestedWindowWidth(convertToInt(argument));
                return true;
            }else if(argument.startsWith("-H")){
                Config.setRequestedWindowHeight(convertToInt(argument));
                return true;
            }
        }catch (NumberFormatException ignored){
        }
        return false;
    }

    private static int convertToInt(String arg){
        int value = Integer.valueOf(arg.substring(2));
        return value;
    }


    public static void settingMenu() {
        int choice = 9;
        while (choice > 0) {
            clearScreen();
            System.out.println("Pre-launch options menu\n\n" +
                    "Settings Menu:\n" +
                    "1: view options menu\n" +
                    "2: Game options menu\n" +
                    "3: Display Help\n" +
                    "9: Quit\n" +
                    "0: Quit menu and start game");
            switch (inputValidator()) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    gameMenu();
                    break;
                case 3:
                    showHelp();
                    break;
                case 9:
                    System.exit(0);
                    break;
                case 0:
                    choice = 0;
                    break;
                default:
                    System.out.print("Invalid option press enter to continue\n");
                    waitForEnter();
                    break;
            }
        }

    }

    private static void gameMenu() {
        int choice = 9;
        clearScreen();
        while (choice > 0) {
            System.out.println("Pre-launch options menu\n\n" +
                    "Game options menu:\n" +
                    "1: Set board X size - " + Config.getxSize() + "\n" +
                    "2: Set board Y size - " + Config.getySize() + "\n" +
                    "3: Set speed (FPS) - " + Config.getFrameRate() + "\n" +
                    "4: Display FPS Statistics - " + Config.isPrintStatistics() + "\n" +
                    "5: Set example board on start - " + Config.isStartExampleModels() + "\n" +
                    "0: Return");
            switch (inputValidator()) {
                case 1:
                    System.out.println("Please enter board X size\n");
                    int boardXsize = inputValidator();
                    Config.setXsize(boardXsize);
                    clearScreen();
                    System.out.println("Set board X size to: " + boardXsize + "\n");
                    scanner.nextLine();
                    break;
                case 2:
                    System.out.println("Please enter board Y size\n");
                    int boardYsize = inputValidator();
                    Config.setYsize(boardYsize);
                    clearScreen();
                    System.out.println("Set board Y size to: " + boardYsize + "\n");
                    scanner.nextLine();
                    break;
                case 3:
                    System.out.println("Please enter desired speed in FPS\n");
                    int requestedFPS = inputValidator();
                    Config.setFrameRate(requestedFPS);
                    clearScreen();
                    System.out.println("Desired FPS set to: " + requestedFPS + "\n");
                    scanner.nextLine();
                    break;
                case 4:
                    Config.togglePrintStatistics();
                    clearScreen();
                    break;
                case 5:
                    Config.toggleStartExampleModels();
                    clearScreen();
                    break;
                case 0:
                    choice = 0;
                    break;
                default:
                    System.out.print("Invalid option");
                    scanner.nextLine();
                    break;
            }
        }
    }

    private static void viewMenu() {
        int choice = 9;
        clearScreen();
        while (choice > 0) {
            System.out.println("Pre-launch options menu\n\n" +
                    "view Options Menu:\n" +
                    "1: Set Window Height - " + Config.getRequestedWindowHeight() + "\n" +
                    "2: Set Window Width - " + Config.getRequestedWindowWidth() + "\n" +
                    "3: Set Console view - current state - " + Config.isConsoleView() + "\n" +
                    "4: Set JavaFX view - current state - " + !Config.isConsoleView() + "\n" +
                    "5: Set JavaFX3D view (valid only if JavaFX view is True) - " + Config.isView3d() + "\n" +
                    "0: Return");
            switch (inputValidator()) {
                case 1:
                    System.out.println("Please enter requested window height");
                    int heightInput = inputValidator();
                    
                    if (!Config.setRequestedWindowHeight(heightInput)) {
                        System.out.println("Press enter to continue");
                        waitForEnter();
                    } else {
                        clearScreen();
                        System.out.println("Set Window height to " + heightInput + "\n");
                        scanner.nextLine();
                    }
                    break;
                case 2:
                    System.out.println("Please enter requested window width");
                    int widthInput = inputValidator();
                    if (!Config.setRequestedWindowWidth(widthInput)) {
                        System.out.println("Press enter to continue");
                        waitForEnter();
                    } else {
                        clearScreen();
                        System.out.println("Set Window height to " + widthInput + "\n");
                        scanner.nextLine();
                    }
                    break;
                case 3:
                    Config.setConsoleView(true);
                    clearScreen();
                    System.out.println("Set view to Console view\n");
                    scanner.nextLine();
                    break;
                case 4:
                    Config.setConsoleView(false);
                    clearScreen();
                    System.out.println("Set view to JavaFX view\n");
                    scanner.nextLine();
                    break;
                case 5:
                    Config.setView3d(!Config.isView3d());
                    clearScreen();
                    System.out.println("Set view to JavaFX3D view\n");
                    scanner.nextLine();
                    break;
                case 0:
                    choice = 0;
                    break;
            }
        }
    }

    public static void showHelp() {
        clearScreen();
        System.out.println("Usage:\n\n" +
                "Example command line usage: gameOfLife -c -e\n\n" +
                "Console view - console view is for viewing only - there is no input method - quit with ctr(cmd) + c\n\n" +
                "JavaFX view:\n" +
                "-Mouse:\n" +
                "--Left key - change dot state\n" +
                "--Right key - toggle pause\n" +
                "-Keyboard input:\n" +
                "-- Keys 1 - 9 - set different set of rules. 1 - standard Conways Game Of Life rules\n" +
                "-- c - clears board\n" +
                "-- n - places example dots on board\n" +
                "-- + - increase requested speed\n" +
                "-- - - decrease requested speed\n\n" +
                "======= below valid only in 3d view in addition to JavaFX view========\n" +
                "-Mouse drag:\n" +
                "-- with left key - rotate model\n" +
                "-Keyboard:\n" +
                "-- WSAD - move camera\n" +
                "-- UP/DOWN - move camera up or down\n" +
                "-- LEFT/RIGHT - rotate camera\n" +
                "-- v - toggle view of corner cubes\n" +
                "---- Additionally SHIFT/CTRL pressed while moving camera or rotating view will modify speed");
        System.out.println("Press enter to continue");
        waitForEnter();
    }

    private static void waitForEnter(){
        try {
            System.in.read();
        } catch (IOException ignored) {}
    }

    private static int inputValidator(){
        System.out.println("\n\nSelect number and press Enter");
        while(!scanner.hasNextInt()){
            System.out.println("Please enter a number");
            scanner.nextLine();
        }
        return scanner.nextInt();

    }
}
