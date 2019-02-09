package com.sikoramarek.View.Implementations.View3D;

import javafx.scene.paint.Color;
import javafx.scene.shape.Box;

public class BoxB extends Box{

    private boolean descColor = false;
    private int green = 0;
    private Color color = new Color(1,0,0, 1);

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
