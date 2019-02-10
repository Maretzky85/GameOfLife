package com.sikoramarek.View;

import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Controller.Controller;
import com.sikoramarek.Model.Dot;
import javafx.scene.Scene;


/**
 * View interface for GameOfLife
 */
public interface ViewInterface {

    Scene getScene();

    void viewInit() throws SystemConfigTooWeekException;

    void refresh(Dot[][] board);

    void attachObserver(Controller controller);

    int getDroppedFrames();

    int getRenderedFrames();
}
