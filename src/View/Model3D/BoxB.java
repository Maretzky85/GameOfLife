package View.Model3D;

import javafx.scene.shape.Box;

public class BoxB extends Box {

    public BoxB(int width, int heigth, int depth, int x, int y){
        super(width, heigth, depth);
        this.boardX = x;
        this.boardY = y;
    }

    public int getBoardX() {
        return boardX;
    }

    public int getBoardY() {
        return boardY;
    }

    private int boardX;
    private int boardY;


}
