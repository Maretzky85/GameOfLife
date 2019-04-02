package com.sikoramarek.gameOfLife.model;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Dot Class
 * holds number of generations lived
 * overrides toString method for console output printing
 */
public class Dot implements Serializable {

	private boolean descColor = false;
	private int green = 0;

	@Override
	public String toString() {
		return "*";
	}


	public Color getColor() {
		if (descColor) {
			green -= 10;
			if (green < 50) {
				descColor = false;
			}
		} else {
			green += 10;
			if (green > 245) {
				descColor = true;
			}
		}
		return Color.rgb(250, green, 0);
	}
}
