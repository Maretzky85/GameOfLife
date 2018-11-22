package View;

import Controller.Controller;
import Model.Dot;

/**
 * View class for viewing GameOfLife board in console
 * mostly for debugging and testing purposes
 */
public class ConsoleView implements ViewInterface {
    private int frameDropped = 0;
    private boolean printingInProgress = false;
    /**
     * clearScreen - method for clearing screen
     *              -called once per update firstly before drawing board
     *              -called in viewInit
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        //TODO check in Windows
    }

    /**
     * viewInit - calls clearScreen for preparing for board drawing
     */
    @Override
    public void viewInit() {
        clearScreen();

    }

    /**
     *refresh method
     * prints given board to console
     *
     * @param board - 2d array of dots
     */
    @Override
    public void refresh(Dot[][] board) {
        if(!printingInProgress){
            printingInProgress = true;
            clearScreen();

            for (Dot[] RowInBoard : board) {
                System.out.println();
                for(Dot DotInRow : RowInBoard){
                    if(DotInRow == null){
                        System.out.print(" ");
                    }else{
                        System.out.print(DotInRow);
                    }
                }
            }
            System.out.println();
            System.out.println();
            printingInProgress = false;
        }else{
            frameDropped++;
        }
    }

    @Override
    public int getDroppedFrames() {
        frameDropped = 0;
        return frameDropped;
    }

    @Override
    public void attachObserver(Controller ignore) {
    }

}
