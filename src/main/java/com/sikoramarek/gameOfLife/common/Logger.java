package com.sikoramarek.gameOfLife.common;

public class Logger {

	public static void log(String message, Object source) {
		System.out.println(source.toString() + " - " + message);
	}

	public static void error(String message, Object source) {
		System.out.println(source.toString() + " Error: " + message);
	}
}
