package com.sikoramarek.gameOfLife.common;

import javafx.scene.paint.Color;

public class Config {
    public static double VERSION = 0.99;

    private static boolean worldWrapping = true;

    /**
     * Print statistics
     * Set true for printing FPS statistics in 1 sec interval
     * prints FPS for model calculations and view dropped frames
     */
    private static boolean printStatistics = true;

    /**
     * CONSOLE_VIEW - console printing if true, JavaFX Window if false
     * for console view limit x and y for console size
     */
    public static boolean CONSOLE_VIEW = false;

    public static boolean JAVAFX_VIEW = true;

    /**
     * VIEW_3D - 3D representation of board
     * can be showed if JavaFX view is selected
     */
    public static boolean VIEW_3D = true;

    /**
     * X_SIZE and Y_SIZE - size of grid for gameOfLife board
     * FRAME_RATE - how many generations to render in one second
     */
    public static int X_SIZE = 80;

    public static int Y_SIZE = 40;

    public static int FRAME_RATE = 20;

    /**
     * JavaFX window size configuration
     * DEAD_COLOR - color for inactive Dot
     */
    private static int REQUESTED_WINDOW_WIDTH = 400;

    private static int REQUESTED_WINDOW_HEIGHT = 300;
    /**
     * DEAD_COLOR - color for inactive Dot
     */
    public static Color DEAD_COLOR = Color.BLACK;

    /**
     * put example Dots in model on game start
     */
    private static boolean startExampleModels = false;

    /**
     * DO NOT EDIT!!!!
     * configuration for matching JavaFX Window for board size
     * if window width and height are not dividable by board size
     * window size cannot be smaller than board size
     */
    public static int RECTANGLE_WIDTH = Math.max(1, Math.floorDiv(REQUESTED_WINDOW_WIDTH, X_SIZE));

    public static int RECTANGLE_HEIGHT = Math.max(1, Math.floorDiv(REQUESTED_WINDOW_HEIGHT, Y_SIZE));
    public static int WIDTH = RECTANGLE_WIDTH * X_SIZE;
    public static int HEIGHT = RECTANGLE_HEIGHT * Y_SIZE;
    public static int RECTANGLE_ARC_HEIGHT = RECTANGLE_HEIGHT / 3;
    public static int RECTANGLE_ARC_WIDTH = RECTANGLE_WIDTH / 3;

    /*
    =============================================================================
    setters and getters for console menu and in-game modifications from JavaFX
     */
    public static boolean isWorldWrapping() {
        return worldWrapping;
    }

    public static void setWorldWrapping(boolean worldWrapping) {
        Config.worldWrapping = worldWrapping;
    }

    static void togglePrintStatistics() {
        printStatistics = !printStatistics;
    }

    public static void setConsoleView(boolean consoleView) {
        CONSOLE_VIEW = consoleView;
    }

    public static void setView3d(boolean view3d) {
        VIEW_3D = view3d;
    }

    public static void setJavafxView(boolean javafxView){ JAVAFX_VIEW = javafxView;}

    public static void setXsize(int xSize) {
        X_SIZE = xSize;
        if (REQUESTED_WINDOW_WIDTH < X_SIZE) {
            REQUESTED_WINDOW_WIDTH = X_SIZE;
        }
        resize();
    }

    public static void setYsize(int ySize) {
        Y_SIZE = ySize;
        if (REQUESTED_WINDOW_HEIGHT < Y_SIZE) {
            REQUESTED_WINDOW_HEIGHT = Y_SIZE;
        }
        resize();
    }

    public static void setFrameRate(int frameRate) {
        FRAME_RATE = frameRate;
    }

    public static boolean setRequestedWindowWidth(int requestedWindowWidth) {
        boolean ok = false;
        if (requestedWindowWidth < X_SIZE) {
            REQUESTED_WINDOW_WIDTH = X_SIZE;
            System.out.println("Requested window size ("+requestedWindowWidth+") is smaller than board\nWindow X size set to: " + X_SIZE);
        } else {
            REQUESTED_WINDOW_WIDTH = requestedWindowWidth;
            ok = true;
        }
        resize();
        return ok;
    }

    public static boolean setRequestedWindowHeight(int requestedWindowHeight) {
        boolean ok = false;
        if (requestedWindowHeight < Y_SIZE) {
            REQUESTED_WINDOW_HEIGHT = Y_SIZE;
            System.out.println("Requested window size ("+requestedWindowHeight+") is smaller than board\nWindow Y size set to: " + Y_SIZE);
        } else {
            REQUESTED_WINDOW_HEIGHT = requestedWindowHeight;
            ok = true;
        }
        resize();
        return ok;
    }

    static void toggleStartExampleModels() {
        Config.startExampleModels = !Config.startExampleModels;
    }

    private static void resize() {
        RECTANGLE_WIDTH = Math.max(1, Math.floorDiv(REQUESTED_WINDOW_WIDTH, X_SIZE));
        WIDTH = RECTANGLE_WIDTH * X_SIZE;
        RECTANGLE_HEIGHT = Math.max(1, Math.floorDiv(REQUESTED_WINDOW_HEIGHT, Y_SIZE));
        HEIGHT = RECTANGLE_HEIGHT * Y_SIZE;
        RECTANGLE_ARC_HEIGHT = RECTANGLE_HEIGHT / 3;
        RECTANGLE_ARC_WIDTH = RECTANGLE_WIDTH / 3;
    }

    public static boolean isPrintStatistics() {
        return printStatistics;
    }

    static boolean isConsoleView() {
        return CONSOLE_VIEW;
    }

    static boolean isView3d() {
        return VIEW_3D;
    }

    public static int getxSize() {
        return X_SIZE;
    }

    public static int getySize() {
        return Y_SIZE;
    }

    public static int getFrameRate() {
        return FRAME_RATE;
    }

    public static int getRequestedWindowWidth() {
        return REQUESTED_WINDOW_WIDTH;
    }

    public static int getRequestedWindowHeight() {
        return REQUESTED_WINDOW_HEIGHT;
    }

    static boolean isStartExampleModels() {
        return startExampleModels;
    }
}
