package com.sikoramarek.Controller;

import com.sikoramarek.Common.BoardTooSmallException;
import com.sikoramarek.Common.Config;
import com.sikoramarek.Common.Logger;
import com.sikoramarek.Common.SharedResources;
import com.sikoramarek.Model.Board;
import com.sikoramarek.Model.MultiThread.BoardMultithreading;
import com.sikoramarek.Model.RuleManager;
import com.sikoramarek.Model.SingleThread.BoardSingleThread;
import com.sikoramarek.View.ViewManager;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import static com.sikoramarek.Common.Config.*;

/**
 * Controller class for GameOfLife implements Observer cass for receiving updates from JavaFX
 * Have theoretical model(Board), View for drawing model and FrameControlLoop for
 * controlling speed of updates for model and statistics drawing
 */
public class Controller{

    private RuleManager ruleManager;
    private Board model;
    private ViewManager view;
    private FrameControlLoop loop;
    private boolean pause = true;

    /**
     * Initiates model with parameters specified in Config class
     * Initiates view output
     * Initiates FrameControlLoop with setting configured in Config class
     * measure ModelInit time for modules
     * <p>
     * For console view - start without pause
     * For JavaFX view - starts paused, sets loop thread as daemon
     * <p>
     * Initiates first view update for first frame draw
     *
     * @param primaryStage - primaryStage from main thread - for passing through if JavaFX View is selected
     *
     */
    public void GameInit(Stage primaryStage) {
        view = new ViewManager(primaryStage, () -> {
            try {
                ModelInit();
                view.boardLoadSuccess = true;
            } catch (BoardTooSmallException e) {
                view.boardLoadSuccess = false;
                Logger.error(e.getMessage(), this);
            }
        });
    }

    private void ModelInit() throws BoardTooSmallException {
        this.ruleManager = new RuleManager();
        int logicProcessors = Runtime.getRuntime().availableProcessors();
        if( logicProcessors > 1 && Y_SIZE >= 100 && X_SIZE >= 100){
            Logger.log("Found "+ logicProcessors +" logical processors, starting Multithreaded model", this);
            model = new BoardMultithreading(Y_SIZE, X_SIZE);
        }else{
            model = new BoardSingleThread(Y_SIZE, X_SIZE);
        }
        startLoop();
    }

    private void handleInputs(){
        ruleManager.checkForInput();
        for (KeyCode key : SharedResources.getKeyboardInput()
                ) {
            switch (key) {
                case P:
                    pause = !pause;
                    break;
                case C:
                    model.clearBoard();
                    view.refresh(model.getBoard());
                    break;
                case N:
                    model.initExampleBoard();
                    view.refresh(model.getBoard());
                    break;
                case ADD:
                    loop.increaseSpeed();
                    break;
                case SUBTRACT:
                    loop.decreaseSpeed();
                    break;
                case L:
                    view.refresh(model.getBoard());
                    break;
                case M:
                    loop.toggleLoopState();
                    break;
                default:
                    break;
            }
        }
        synchronized (SharedResources.getKeyboardInput()){
            SharedResources.clearKeyboardInput();
        }
        for (int[] position : SharedResources.positions
                ) {
            model.changeOnPosition(position[0], position[1]);
            }
        synchronized (SharedResources.positions){
            SharedResources.positions.clear();
        }
    }


    /**
     * startLoop
     * method for FrameControlLoop start in new Thread
     * called from outside class after ModelInit
     */
    private void startLoop() {
            loop = new FrameControlLoop(this::updateState);
            loop.attachStatisticTimer(this::showStatistics);
            Thread loopThread = new Thread(loop);
            loopThread.setDaemon(true);
            loopThread.start();
    }

    /**
     * updateState
     * method for trigger next generation update
     * for both model and than view
     */
    private void updateState() {
        handleInputs();
        if (!pause){
            if(!model.isBusy()){
                model.nextGen();
                view.refresh(model.getBoard());
            }
        }

    }

    /**
     * showStatistics
     * method for printing out ( once per call ) FPS statistic
     * gathered from FrameLoopControl and View(view avaible only for JavaFX)
     */
    private void showStatistics() {
        if (Config.isPrintStatistics()) {
            Logger.log("\nLoop: FPS:                       " + loop.getFPS() +
                    "\nBoard: Generations (per second): "+ model.getGeneration()
                            +
                    "\nView: rendered frames:           " + view.getRenderedFrames() +
                    "\nView: dropped frames:            "+ view.getDroppedFrames()+"\n", this);
        }
    }

    @Override
    public String toString(){
        return "Controller";
    }
}
