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
                case "-h":
                    SettingsMenu.showHelp();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Illegal argument\n" +
                            "List of available arguments:\n" +
                            "-w - start app in window with default configuration" +
                            "-e - start app with example models" +
                            "-c - start app with console output" +
                            "-h - display help");
                    System.exit(0);
                    break;
            }
        }
        launch();
    }
}
