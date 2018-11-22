package Controller;

import Common.BoardTooSmallException;
import Common.Config;
import Model.Board;
import View.ConsoleView;
import View.JavaFXView;
import View.ViewInterface;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

import static Common.Config.*;

/**
 * Controller class for GameOfLife implements Observer cass for receiving updates from JavaFX
 * Have theoretical model(Board), View for drawing model and FrameControlLoop for
 * controlling speed of updates for model and statistics drawing
 */
public class Controller implements Observer {

    private Board model;
    private ViewInterface view;
    private FrameControlLoop loop;

    /**
     * Initiates model with parameters specified in Config class
     * Initiates view output
     * Initiates FrameControlLoop with setting configured in Config class
     * measure init time for modules
     * <p>
     * For console view - start without pause
     * For JavaFX view - starts paused, sets loop thread as daemon
     * <p>
     * Initiates first view update for first frame draw
     *
     * @param primaryStage - primaryStage from main thread - for passing through if JavaFX View is selected
     *
     * @throws BoardTooSmallException - if board in config is too small, forces app to exit
     */
    public void controllerInit(Stage primaryStage) throws BoardTooSmallException {


        long startTime = System.currentTimeMillis();

        System.out.print("Initialising model");
        model = new Board(Y_SIZE, X_SIZE);

        if (Config.isStartExampleModels()) {
            model.initExampleBoard();
        }
        System.out.print(" ...done. Took " + (System.currentTimeMillis() - startTime) + " ms\n");

        System.out.print("Initialising View...");
        startTime = System.currentTimeMillis();
        if (CONSOLE_VIEW) {
            view = new ConsoleView();
        } else {
            view = new JavaFXView(primaryStage);
        }

        System.out.print(" ...done. Took " + (System.currentTimeMillis() - startTime) + " ms\n");

        loop = new FrameControlLoop(this::updateState);
        loop.attachStatisticTimer(this::showStatistics);

        view.viewInit();

        if (CONSOLE_VIEW) {
            loop.togglePause();
        } else {
            loop.setDaemon(true);
            view.attachObserver(this);
            view.refresh(model.getBoard());
        }
    }


    /**
     * ***Only for JavaFX view
     * Overriden method for updating model if input is received
     *
     * @param o - observable object( in this case InputHandler Class )
     * @param arg - arguments for update:
     *            int[] for board position update
     *            int - for board rules update
     *            String for speed control, pause, clear and insert example elements on board
     */
    @Override
    public void update(Observable o, Object arg) {
        boolean argIsPosition = true;
        boolean argIsint = true;
        try {
            int[] position = (int[]) arg;
            model.changeOnPosition(position[0], position[1]);
        } catch (ClassCastException ignored) {
            argIsPosition = false;
        }

        try {
            int option = Integer.valueOf(arg.toString());
            model.setRules(option);
        } catch (NumberFormatException ignored) {
            argIsint = false;
        }

        if (!argIsPosition && !argIsint) try {
            String key = (String) arg;
            switch (key) {
                case "p":
                    loop.togglePause();
                    break;
                case "c":
                    model.clearBoard();
                    view.refresh(model.getBoard());
                    break;
                case "n":
                    model.initExampleBoard();
                    view.refresh(model.getBoard());
                    break;
                case "+":
                    loop.increaseSpeed();
                    break;
                case "-":
                    loop.decreaseSpeed();
                    break;
                default:
                    break;
            }
        } catch (ClassCastException ignored) {
        }
    }

    /**
     * startLoop
     * method for FrameControlLoop start in new Thread
     * called from outside class after init
     */
    public void startLoop() {
        loop.start();
    }

    /**
     * updateState
     * method for trigger next generation update
     * for both model and than view
     */
    private void updateState() {
        model.nextGen();
        view.refresh(model.getBoard());
    }

    /**
     * showStatistics
     * method for printing out ( once per call ) FPS statistic
     * gathered from FrameLoopControl and View(view avaible only for JavaFX)
     */
    private void showStatistics() {
        if (Config.isPrintStatistics()) {
            System.out.println("Current model FPS: " + loop.getFPS() + "\nDropped View frames: " + view.getDroppedFrames() + "\n");
        }
    }
}
