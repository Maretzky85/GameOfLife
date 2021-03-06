package com.sikoramarek.gameOfLife.view.implementations;

import com.sikoramarek.gameOfLife.model.Dot;


/**
 * view class for viewing gameOfLife board in console
 * mostly for debugging and testing purposes
 */
public class ConsoleOutput {
	private int frameDropped = 0;
	private int renderedFrames = 0;
	private int renderedFramesSum = 0;
	private boolean printingInProgress = false;

	/**
	 * clearScreen - method for clearing screen
	 * -called once per update firstly before drawing board
	 * -called in viewInit
	 */
	private void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
		//TODO check in Windows
	}

	/**
	 * viewInit - calls clearScreen for preparing for board drawing
	 */

	public void viewInit() {
		clearScreen();
	}

	/**
	 * refresh method
	 * prints given board to console
	 *
	 * @param board - 2d array of dots
	 */
	public void refresh(Dot[][] board) {
		if (!printingInProgress) {
			printingInProgress = true;
			clearScreen();

			for (Dot[] RowInBoard : board) {
				System.out.println();
				for (Dot DotInRow : RowInBoard) {
					if (DotInRow == null) {
						System.out.print(".");
					} else {
						System.out.print(DotInRow);
					}
				}
			}
			renderedFrames++;
			printingInProgress = false;
		} else {
			frameDropped++;
		}
	}


	public int getDroppedFrames() {
		int currentDroppedFrames = frameDropped;
		frameDropped = 0;
		return currentDroppedFrames;
	}


	public int getRenderedFrames() {
		renderedFramesSum = renderedFrames;
		int currentRenderedFrames = renderedFrames;
		renderedFrames = 0;
		return currentRenderedFrames;
	}

}
