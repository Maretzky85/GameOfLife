package com.sikoramarek.gameOfLife.model.singleThread;

import com.sikoramarek.gameOfLife.common.Config;
import com.sikoramarek.gameOfLife.common.errors.BoardTooSmallException;
import com.sikoramarek.gameOfLife.model.Board;
import com.sikoramarek.gameOfLife.model.Dot;
import com.sikoramarek.gameOfLife.model.ModelPlacer;
import com.sikoramarek.gameOfLife.model.RuleManager;

import java.util.Arrays;

/**
 * Theoretical model for gameOfLife
 * holds board that is 2d table that holds Dot or null
 * holds rule to live to next generation and rule to get alive in net generation
 */
public class BoardSingleThread implements Board {
	private Dot[][] board;
	private ModelPlacer modelPlacer;

	private int generation = 0;
	private boolean busy = false;

	/**
	 * Board constructor
	 *
	 * @param y - y size (height) of board (int)
	 * @param x - x size (width) of board (int)
	 * @throws BoardTooSmallException - forces app to exit if board size is too small
	 */
	public BoardSingleThread(int y, int x) throws BoardTooSmallException {
		if (x < 5 || y < 5) {
			throw new BoardTooSmallException("Board must be at least 5 x 5");
		}
		this.board = new Dot[y][x];
		this.modelPlacer = new ModelPlacer(this);
	}

	/**
	 * nextGen
	 * <p>
	 * Method for calculating new generation - depends on rule to live and rule to get alive
	 */
	@Override
	public void nextGen() {
		if (!busy) {
			busy = true;
			Dot[][] tempBoard = newEmptyBoard();

			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {

					int aliveNeighbors = getNeighbors(j, i);
					Dot currentSourceDot = board[i][j];

					if (currentSourceDot != null &&
							Arrays.stream(RuleManager.ruleToLive).anyMatch(value -> value == aliveNeighbors)) {
						tempBoard[i][j] = currentSourceDot;
					} else if ((currentSourceDot == null &&
							Arrays.stream(RuleManager.ruleToGetAlive).anyMatch(value -> value == aliveNeighbors))) {
						tempBoard[i][j] = new Dot();
					}
				}
			}
			generation++;
			this.board = tempBoard;
			busy = false;
		}
	}

	@Override
	public int getGeneration() {
		int gen = generation;
		generation = 0;
		return gen;
	}

	/**
	 * getNeighbors
	 * <p>
	 * helper function for nextGen
	 *
	 * @param boardTargetXposition - boardYposition position on board
	 * @param boardTargetYposition - x position on board
	 * @return - returns existing ( not null) dots count around given x,boardYposition position
	 * out of array bounds does not count as neighbor ( no-wrapping )
	 */
	private int getNeighbors(int boardTargetXposition, int boardTargetYposition) {
		int neighbors = 0;
		int leftOfDownThreshold = -1;
		int rightOrUpThreshold = 1;
		int thisPosition = 0;
		for (int i = leftOfDownThreshold; i <= rightOrUpThreshold; i++) {
			for (int j = leftOfDownThreshold; j <= rightOrUpThreshold; j++) {

				int checkYposition = boardTargetYposition + i;
				int checkXposition = boardTargetXposition + j;

				if (Config.isWorldWrapping()) {

					if (checkXposition == board[0].length) {
						checkXposition = 0;
					}
					if (checkXposition < 0) {
						checkXposition = board[0].length - 1;
					}
					if (checkYposition == board.length) {
						checkYposition = 0;
					}
					if (checkYposition < 0) {
						checkYposition = board.length - 1;
					}
				}
				try {
					if (board[checkYposition][checkXposition] != null && !(i == thisPosition && j == thisPosition)) {
						neighbors++;
					}
				} catch (ArrayIndexOutOfBoundsException ignored) {

				}
			}
		}
		return neighbors;
	}

	/**
	 * newEmptyBoard
	 * <p>
	 * helper function for nextGen and clearBoard
	 *
	 * @return - returns empty board of size given in Config class
	 */
	private Dot[][] newEmptyBoard() {
		int xLength = board[0].length;
		int yLength = board.length;
		return new Dot[yLength][xLength];
	}

	/**
	 * changeOnPosition
	 * <p>
	 * toggle state on boards given position
	 * if null - creates new Dot on given position
	 * if Dot - insert null instead
	 *
	 * @param x - x position on board
	 * @param y - y position on board
	 */
	@Override
	public void changeOnPosition(int x, int y) {
		if (board[y][x] == null) {
			board[y][x] = new Dot();
		} else {
			board[y][x] = null;
		}
	}

	@Override
	public boolean isBusy() {
		return busy;
	}

	@Override
	public void checkForInput() {
		modelPlacer.checkForInputs();
	}

	/**
	 * clearBoard
	 * <p>
	 * calls new EmptyBoard function and assign result to board
	 */
	@Override
	public void clearBoard() {
		board = newEmptyBoard();
	}

	/**
	 * initExampleBoard
	 * <p>
	 * function for placing example Dots on board defined in function
	 * if Dot position is out of board bounds - ignore ( does not place )
	 */
	@Override
	public void initExampleBoard() {
		modelPlacer.placeGlider(1, 1);
	}

	@Override
	public Dot[][] getBoard() {
		return board;
	}

}
