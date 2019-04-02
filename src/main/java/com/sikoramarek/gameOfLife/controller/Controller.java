package com.sikoramarek.gameOfLife.controller;


import com.sikoramarek.gameOfLife.common.Config;
import com.sikoramarek.gameOfLife.common.Logger;
import com.sikoramarek.gameOfLife.common.SharedResources;
import com.sikoramarek.gameOfLife.common.errors.BoardTooSmallException;
import com.sikoramarek.gameOfLife.model.Board;
import com.sikoramarek.gameOfLife.model.RuleManager;
import com.sikoramarek.gameOfLife.model.multiThread.BoardMultithreading;
import com.sikoramarek.gameOfLife.model.singleThread.BoardSingleThread;
import com.sikoramarek.gameOfLife.view.ViewManager;
import com.sikoramarek.gameOfLife.client.Client;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

import static com.sikoramarek.gameOfLife.common.Config.*;
import static com.sikoramarek.gameOfLife.common.Config.X_SIZE;
import static com.sikoramarek.gameOfLife.common.Config.Y_SIZE;

/**
 * controller class for gameOfLife implements Observer cass for receiving updates from JavaFX
 * Have theoretical model(Board), view for drawing model and FrameControlLoop for
 * controlling speed of updates for model and statistics drawing
 */
public class Controller {

	private RuleManager ruleManager;
	private Board model;
	private ViewManager view;
	private FrameControlLoop loop;
	private boolean pause = true;

    private Client client;

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
     * @param primaryStage - primaryStage from main thread - for passing through if JavaFX view is selected
     *
     */
    public void GameInit(Stage primaryStage) {
        client = Client.getClient();
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
		if (logicProcessors > 1 && Y_SIZE >= 100 && X_SIZE >= 100) {
			Logger.log("Found " + logicProcessors + " logical processors, starting Multithreaded model", this);
			model = new BoardMultithreading(Y_SIZE, X_SIZE);
		} else {
			model = new BoardSingleThread(Y_SIZE, X_SIZE);
		}
		startLoop();
	}

	private void handleInputs() {
		model.checkForInput();
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
				case R:
					view.refresh(model.getBoard());
					break;
				default:
					break;
			}
			synchronized (SharedResources.getKeyboardInput()) {
				SharedResources.clearKeyboardInput();
			}
		}
		for (int[] position : SharedResources.positions
		) {
			model.changeOnPosition(position[0], position[1]);
		}
		synchronized (SharedResources.positions) {
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
                if(multiplayer){
//                    view.refreshSecond(client.getSecondBoard());
                }
                view.refresh(model.getBoard());
            }
            if (multiplayer){
//                client.sendBoard(model.getBoard());

            }

        }

	}

	/**
	 * showStatistics
	 * method for printing out ( once per call ) FPS statistic
	 * gathered from FrameLoopControl and view(view available only for JavaFX)
	 */
	private void showStatistics() {
		if (Config.isPrintStatistics()) {
			Logger.log("\nLoop: FPS:                       " + loop.getFPS() +
					"\nBoard: Generations (per second): " + model.getGeneration()
					+
					"\nview: rendered frames:           " + view.getRenderedFrames() +
					"\nview: dropped frames:            " + view.getDroppedFrames() + "\n", this);
		}
	}

	@Override
	public String toString() {
		return "controller";
	}
}
