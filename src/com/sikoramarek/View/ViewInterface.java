package com.sikoramarek.View;

import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Model.Dot;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


/**
 * View interface for GameOfLife
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
