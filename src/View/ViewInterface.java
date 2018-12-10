package View;

import Controller.Controller;
import Model.Dot;


/**
 * View interface for GameOfLife
 */
public interface ViewInterface {

    void viewInit();

    void refresh(Dot[][] board);

    void attachObserver(Controller controller);

    int getDroppedFrames();

    int getRenderedFrames();
}
