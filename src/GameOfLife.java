import Common.BoardTooSmallException;
import Common.Config;
import Common.SettingsMenu;
import Controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameOfLife extends Application {

    /**
     *Entry point of application
     * if no arguments is passed than views menu
     *
     * @param args - console arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            handleArgs(args);
        } else {
            SettingsMenu.settingMenu();
            launch();
        }

    }

    /**
     * start method for JavaFX Applications
     * creates and init controller
     *
     * @param primaryStage - standard required for JavaFX App
     * @throws BoardTooSmallException - if board is less than 5x5
     */
    @Override
    public void start(Stage primaryStage) throws BoardTooSmallException {
        System.out.println("Starting Game...");
        Controller controller = new Controller();
        controller.controllerInit(primaryStage);
        controller.startLoop();
    }

    /**
     * handleArgs method for setting application before launch
     *
     * @param args - arguments from console call
     */
    private static void handleArgs(String[] args) {
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
                case "-h":
                    SettingsMenu.showHelp();
                    System.exit(0);
                    break;
                default:
                    if(!handleComplexArg(arg)){
                        System.out.println("Illegal argument\n" +
                                "List of available arguments:\n\n" +
                                "-x[] - sets board X size eg. -x200\n" +
                                "-y[] - sets board Y size eg. -y200\n" +
                                "-W[] - sets window width eg. -W1000\n" +
                                "-H[] - sets window height eg. -H1000\n" +
                                "-w - start app in window with default configuration\n" +
                                "-e - start app with example models\n" +
                                "-c - start app with console output\n" +
                                "-s - start app with print statistics on\n" +
                                "-h - display help");
                        System.exit(0);
                    }
                    break;
            }
        }
        launch();
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

}

