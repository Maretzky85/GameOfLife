package com.sikoramarek.gameOfLife;

import com.sikoramarek.gameOfLife.common.Logger;
import com.sikoramarek.gameOfLife.common.SettingsMenu;
import com.sikoramarek.gameOfLife.common.errors.BoardTooSmallException;
import com.sikoramarek.gameOfLife.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Arrays;

public class GameOfLife extends Application {

	/**
	 * Entry point of application
	 * if no arguments is passed than views menu
	 *
	 * @param args - console arguments
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			if (Arrays.asList(args).contains("-m")) {
				SettingsMenu.settingMenu();
			} else {
				SettingsMenu.handleArgs(args);
				launch();
			}
		} else {
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
		Logger.log("Starting game...", this);
		Controller controller = new Controller();
		controller.GameInit(primaryStage);
	}

	@Override
	public String toString() {
		return "Game Of Life";
	}

}

