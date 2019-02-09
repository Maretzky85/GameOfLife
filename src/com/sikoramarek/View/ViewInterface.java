package com.sikoramarek.View;

import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Controller.Controller;
import com.sikoramarek.Model.Dot;


/**
 * View interface for GameOfLife
 */
public interface ViewInterface {

    void viewInit() throws SystemConfigTooWeekException;

    void refresh(Dot[][] board);

    void attachObserver(Controller controller);

    int getDroppedFrames();

    int getRenderedFrames();
}
