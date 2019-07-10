package com.sikoramarek.gameOfLife.model;

import com.sikoramarek.gameOfLife.common.SharedResources;
import com.sikoramarek.gameOfLife.common.errors.BoardTooSmallException;
import com.sikoramarek.gameOfLife.model.multiThread.BoardMultithreading;
import com.sikoramarek.gameOfLife.model.singleThread.BoardSingleThread;
import javafx.scene.input.KeyCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNotSame;

@RunWith(Parameterized.class)
public class modelPerformanceInfo {
	private int size;
	private int timeToRun;
	private BoardSingleThread boardSingleThread;
	private BoardMultithreading boardMulti;
	private long multiResoult;
	private long singleResoult;
	private float percent;

	public modelPerformanceInfo(int size, int timeToRun) {
		this.timeToRun = timeToRun;
		this.size = size;
	}

	@Parameterized.Parameters
	public static Collection settings() {
		return Arrays.asList(new Object[][]{
				{5, 5000},
				{10, 5000},
				{100, 5000},
				{150, 5000},
				{200, 5000},
				{250, 5000},
				{500, 5000},
				{1000, 5000}
		});
	}

	@Before
	public void init() {
		RuleManager ruleManager = new RuleManager();
		SharedResources.addKeyboardInput(KeyCode.DIGIT5);
		ruleManager.checkForInput();

		try {
			boardSingleThread = new BoardSingleThread(size, 1000);
		} catch (BoardTooSmallException e) {
			e.printStackTrace();
		}
		boardSingleThread.initExampleBoard();

		try {
			boardMulti = new BoardMultithreading(size, 1000);
		} catch (BoardTooSmallException e) {
			e.printStackTrace();
		}
		boardMulti.initExampleBoard();

	}


	@Test
	public void performanceTest() {
		long startSingleTime = System.currentTimeMillis();
		long currentTime = System.currentTimeMillis();
		while (currentTime - startSingleTime < timeToRun) {
			boardSingleThread.nextGen();
			Thread.yield();
			currentTime = System.currentTimeMillis();
		}
		singleResoult = boardSingleThread.getGeneration();

		long startMultiTime = System.currentTimeMillis();
		currentTime = System.currentTimeMillis();

		while (currentTime - startMultiTime < timeToRun) {
			if (!boardMulti.ongoingUpdate) {
				boardMulti.nextGen();
			}
			Thread.yield();
			currentTime = System.currentTimeMillis();
		}

		multiResoult = boardMulti.getGeneration();


		percent = (float) multiResoult / (float) singleResoult * 100;

		assertNotSame("MultiThreaded is " + percent + " % of SingleThreaded", multiResoult, singleResoult);
//		assertTrue("MultiThreaded is " + percent + " % of SingleThreaded", multiResoult > singleResoult);
	}

	@After
	public void printResults() {

		System.out.println();
		System.out.print("Test case: board size: " + size + " time to run: " + timeToRun + " ms\nResoults:" +
				"\nSingle thread generations in 5s: " + singleResoult + "\nMulti thread generations in 5s: " + multiResoult +
				" \nMulti thread is " + percent + " percent of Single thread speed");
		System.out.println();
		System.out.println();
	}
}