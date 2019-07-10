package com.sikoramarek.gameOfLife.controller;

import com.sikoramarek.gameOfLife.common.Config;
import com.sikoramarek.gameOfLife.common.Logger;

import static com.sikoramarek.gameOfLife.common.Config.CONSOLE_VIEW;
import static com.sikoramarek.gameOfLife.common.Config.FRAME_RATE;

/**
 * This class is for controlling how many frames is generated for second
 * This is clock for theoretical model, that run selected functions in requested frequency
 * Additional sends call one per second (for FPS generation)
 */

public class FrameControlLoop implements Runnable {

	private Runnable updater;
	private Runnable statTimer;

	private boolean isRunning = false;

	private int tics = 0; //For FPS Debugging

	private long initialTime = System.currentTimeMillis(); //time for Loop Control
	private long startTime = System.currentTimeMillis(); //initial time for FPS console logging
	private long timeFrame = 1000 / FRAME_RATE; //time in milliseconds for one loop;
	private long timeCounterMs = 0; //milliseconds counter
	private int FPS = 0;
	private int frame = 0;


	FrameControlLoop(Runnable updater) {
		this.updater = updater;
	}

	/**
	 * Run function for starting loop control
	 * checks time between current time and start time, waits for rest ms,
	 * if time between current and start time is greater than time frame, than runs required command.
	 */
	@Override
	public void run() {
		Logger.log("FrameControlLoop started", this);
		isRunning = true;
		while (isRunning) {
			long currentTime = System.currentTimeMillis();
			timeCounterMs += (currentTime - initialTime);
			initialTime = currentTime;

			if (timeCounterMs >= timeFrame) {
				updater.run();
				Thread.yield();

				if (CONSOLE_VIEW) {
					Logger.log("Frame: " + frame, this);
					Logger.log("FPS: " + FPS, this);
				}
				tics += 1;
				frame++;
				timeCounterMs = 0;
			}
			if (timeFrame - timeCounterMs > 0) {
				try {
					Thread.sleep(timeFrame - timeCounterMs);
				} catch (InterruptedException ignored) {
				}
			}

			//FPS logging ======================
			if (currentTime - startTime > 1000) {
				statTimer.run();
				startTime = System.currentTimeMillis();
				FPS = tics;
				tics = 0;
			}

			//===============================================
		}
	}

	/**
	 * function for killing FrameControlLoop
	 */
	void toggleLoopState() {
		isRunning = !isRunning;
	}

	/**
	 * decrease/increase speed
	 * decrease or increase update speed by altering timeframe value
	 */
	void decreaseSpeed() {
		if (timeFrame < 1000) {
			timeFrame = 1000 / Math.max((1000 / timeFrame - 1), 1);
		} else {
			Logger.log("Limit FPS", this);
		}
		if (timeFrame < 30) {
			timeFrame++;
		}
		if (Config.isPrintStatistics()) {
			Logger.log("New requested FPS: " + 1000 / timeFrame, this);
		}
	}

	void increaseSpeed() {
		if (timeFrame > 1) {
			timeFrame = 1000 / (1000 / timeFrame + 1);
		} else {
			Logger.log("Limit FPS", this);
		}

		if (Config.isPrintStatistics()) {
			Logger.log("new requested FPS: " + 1000 / timeFrame, this);

		}

	}

	/**
	 * Additional timed function - called once per second
	 *
	 * @param showStatistics - runnable function called once per second
	 */
	void attachStatisticTimer(Runnable showStatistics) {
		statTimer = showStatistics;
	}

	int getFPS() {
		return FPS;
	}

	@Override
	public String toString() {
		return "Control Loop";
	}
}
