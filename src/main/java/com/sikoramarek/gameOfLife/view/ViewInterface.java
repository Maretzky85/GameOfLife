package com.sikoramarek.gameOfLife.view;

import com.sikoramarek.gameOfLife.common.errors.SystemConfigTooWeekException;
import com.sikoramarek.gameOfLife.model.Dot;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


/**
 * view interface for gameOfLife
 */
public interface ViewInterface {

	Scene getScene();

	void viewInit() throws SystemConfigTooWeekException;

	void refresh(Dot[][] board);

	int getDroppedFrames();

	int getRenderedFrames();

	void handleKeyboard(KeyEvent event);

	void handleMouse(MouseEvent me);

	Text getTutorialPlaceholder();
}
