package com.sikoramarek.gameOfLife.common.errors;

public class BoardTooSmallException extends Exception {
	public BoardTooSmallException(String errorMessage) {
		super(errorMessage);
	}
}
