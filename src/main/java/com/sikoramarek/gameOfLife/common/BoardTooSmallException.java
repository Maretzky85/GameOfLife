package com.sikoramarek.gameOfLife.common;

public class BoardTooSmallException extends Exception {
    public BoardTooSmallException(String errorMessage) {
        super(errorMessage);
    }
}
