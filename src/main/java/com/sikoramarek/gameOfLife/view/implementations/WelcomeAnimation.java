package com.sikoramarek.gameOfLife.view.implementations;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.sikoramarek.gameOfLife.common.Config.X_SIZE;

public class WelcomeAnimation {
	private final Runnable toggle;
	private boolean started = false;
	private Timeline timeline;
	private Color toggleColor;
	public WelcomeAnimation(Runnable rowToggler) {
		this.toggle = rowToggler;
	}

	public void startAnimation() {
		if (!started) {
			started = true;
			toggleColor = Color.WHITE;
			timeline = new Timeline(getKeyframes());
			timeline.setOnFinished((event) -> {
				timeline.stop();
				toggleColor = Color.BLACK;
				timeline = new Timeline(getKeyframes());
				timeline.setOnFinished(event1 -> {
					timeline.stop();
					started = false;
				});

				timeline.playFromStart();
			});
			timeline.playFromStart();
		}

	}

	private KeyFrame[] getKeyframes() {
		KeyFrame[] keyframes = new KeyFrame[X_SIZE];
		for (int i = 0; i < keyframes.length; i++) {
			keyframes[i] = new KeyFrame(Duration.millis(i * ((float) 500 / X_SIZE)), event -> toggle.run());
		}
		return keyframes;
	}

	public Color getToggleColor() {
		return toggleColor;
	}

	public boolean started() {
		return !started;
	}
}
