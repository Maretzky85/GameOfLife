package View;

import Common.SystemConfigTooWeekException;
import Controller.Controller;
import Model.Dot;


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
