package com.sikoramarek.gameOfLife.model;

public interface Board {

	void nextGen();

	int getGeneration();

	void initExampleBoard();

	void clearBoard();

	void changeOnPosition(int x, int y);

	boolean isBusy();

	void checkForInput();

	Dot[][] getBoard();

}
