package com.sikoramarek.View.Implementations.View3D;

import javafx.scene.shape.Box;

public class BoxB extends Box {

    BoxB(int width, int height, int depth, int x, int y){
        super(width, height, depth);
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
