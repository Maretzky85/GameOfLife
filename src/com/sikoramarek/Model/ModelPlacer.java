package com.sikoramarek.Model;

import com.sikoramarek.Common.SharedResources;
import javafx.scene.input.KeyCode;

import java.util.LinkedList;

public class ModelPlacer {

    public LinkedList<Runnable> runWhileNotBusy = new LinkedList<>();

    private Runnable waitingForCoordinates;

    private int xOffset = 0;
    private int yOffset = 0;
    private Board board;

    public ModelPlacer(Board model){
        this.board = model;
    }

    public void checkForInputs() {
        for (KeyCode key : SharedResources.getKeyboardInput()
        ) {
            switch (key) {
                case NUMPAD1:
                    waitingForCoordinates = this::placeGlider;
                    break;
                case NUMPAD2:
                    waitingForCoordinates =this::placeGliderGun;
                    break;
                case NUMPAD3:
                    waitingForCoordinates = this::placeSomething;
                    break;
                default:
                    break;
            }
        }
        if(waitingForCoordinates != null && SharedResources.positions.size() > 0){
            int[] positions = SharedResources.positions.get(0);
            synchronized (SharedResources.positions) {
                SharedResources.positions.clear();
            }
            xOffset = positions[0];
            yOffset = positions[1];
            waitingForCoordinates.run();
            SharedResources.addKeyboardInput(KeyCode.R);
            waitingForCoordinates = null;
        }
    }

    private void placeGlider(){
        placeGlider(xOffset, yOffset);
    }
    public void placeGlider(int x,int y){
        if(board.isBusy()){
            runWhileNotBusy.add(() -> placeGlider(x,y));
        }

        xOffset = x;
        yOffset = y;

        try {
            board.getBoard()[2 + yOffset][2 + xOffset] = new Dot();
            board.getBoard()[2 + yOffset][3 + xOffset] = new Dot();
            board.getBoard()[2 + yOffset][1 + xOffset] = new Dot();
            board.getBoard()[1 + yOffset][3 + xOffset] = new Dot();
            board.getBoard()[yOffset][2 + xOffset] = new Dot();
        }catch (IndexOutOfBoundsException ignored){}
    }

    private void placeSomething(){
        placeSomething(xOffset, yOffset);
    }
    public void placeSomething(int x, int y){
        xOffset = x;
        yOffset = y;

        if(board.isBusy()){
            runWhileNotBusy.add(() -> placeSomething(x,y));
        }

        try{
            board.getBoard()[8 + yOffset][1 + xOffset] = new Dot();
            board.getBoard()[8 + yOffset][3 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][3 + xOffset] = new Dot();
            board.getBoard()[6 + yOffset][5 + xOffset] = new Dot();
            board.getBoard()[5 + yOffset][5 + xOffset] = new Dot();
            board.getBoard()[4 + yOffset][5 + xOffset] = new Dot();
            board.getBoard()[5 + yOffset][7 + xOffset] = new Dot();
            board.getBoard()[4 + yOffset][7 + xOffset] = new Dot();
            board.getBoard()[3 + yOffset][7 + xOffset] = new Dot();
            board.getBoard()[4 + yOffset][8 + xOffset] = new Dot();
        }catch (ArrayIndexOutOfBoundsException ignored){}
    }

    private void placeGliderGun(){
        placeGliderGun(xOffset, yOffset);
    }

    public void placeGliderGun(int x, int y){
        xOffset = x;
        yOffset = y;

        if(board.isBusy()){
            runWhileNotBusy.add(() -> placeGliderGun(x,y));
        }

        try{
            board.getBoard()[8 + yOffset][1 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][1 + xOffset] = new Dot();
            board.getBoard()[8 + yOffset][2 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][2 + xOffset] = new Dot();

            board.getBoard()[8 + yOffset][12 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][12 + xOffset] = new Dot();
            board.getBoard()[6 + yOffset][12 + xOffset] = new Dot();

            board.getBoard()[9 + yOffset][13 + xOffset] = new Dot();
            board.getBoard()[5 + yOffset][13 + xOffset] = new Dot();

            board.getBoard()[4 + yOffset][14 + xOffset] = new Dot();
            board.getBoard()[10 + yOffset][14 + xOffset] = new Dot();

            board.getBoard()[5 + yOffset][15 + xOffset] = new Dot();
            board.getBoard()[9 + yOffset][15 + xOffset] = new Dot();

            board.getBoard()[8 + yOffset][16 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][16 + xOffset] = new Dot();
            board.getBoard()[6 + yOffset][16 + xOffset] = new Dot();

            board.getBoard()[8 + yOffset][17 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][17 + xOffset] = new Dot();
            board.getBoard()[6 + yOffset][17 + xOffset] = new Dot();

            board.getBoard()[6 + yOffset][22 + xOffset] = new Dot();
            board.getBoard()[5 + yOffset][22 + xOffset] = new Dot();
            board.getBoard()[4 + yOffset][22 + xOffset] = new Dot();

            board.getBoard()[3 + yOffset][23 + xOffset] = new Dot();
            board.getBoard()[4 + yOffset][23 + xOffset] = new Dot();
            board.getBoard()[6 + yOffset][23 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][23 + xOffset] = new Dot();

            board.getBoard()[3 + yOffset][24 + xOffset] = new Dot();
            board.getBoard()[4 + yOffset][24 + xOffset] = new Dot();
            board.getBoard()[6 + yOffset][24 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][24 + xOffset] = new Dot();

            board.getBoard()[3 + yOffset][25 + xOffset] = new Dot();
            board.getBoard()[4 + yOffset][25 + xOffset] = new Dot();
            board.getBoard()[5 + yOffset][25 + xOffset] = new Dot();
            board.getBoard()[6 + yOffset][25 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][25 + xOffset] = new Dot();
            board.getBoard()[2 + yOffset][26 + xOffset] = new Dot();
            board.getBoard()[3 + yOffset][26 + xOffset] = new Dot();
            board.getBoard()[7 + yOffset][26 + xOffset] = new Dot();
            board.getBoard()[8 + yOffset][26 + xOffset] = new Dot();

            board.getBoard()[3 + yOffset][31 + xOffset] = new Dot();
            board.getBoard()[4 + yOffset][31 + xOffset] = new Dot();
            board.getBoard()[5 + yOffset][35 + xOffset] = new Dot();
            board.getBoard()[6 + yOffset][35 + xOffset] = new Dot();
            board.getBoard()[5 + yOffset][36 + xOffset] = new Dot();
            board.getBoard()[6 + yOffset][36 + xOffset] = new Dot();
        }catch (ArrayIndexOutOfBoundsException ignored){}
    }
}