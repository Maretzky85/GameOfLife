package com.sikoramarek.gameOfLife.model.multiThread;

import com.sikoramarek.gameOfLife.common.BoardTooSmallException;
import com.sikoramarek.gameOfLife.model.Board;
import com.sikoramarek.gameOfLife.model.Dot;
import com.sikoramarek.gameOfLife.model.ModelPlacer;

import java.util.ArrayList;

/**
 * Theoretical model for gameOfLife
 * holds board that is 2d table that holds Dot or null
 * holds rule to live to next generation and rule to get alive in net generation
 */
public class BoardMultithreading implements Board {
    Dot[][] board;
    Dot[][] nextGenBoard;
    private ArrayList<WorkerThread> runnableArray = new ArrayList<>();
    public Boolean ongoingUpdate = false;
    private int workersDoneCount = 0;
    private int generation = 0;
    private ModelPlacer modelPlacer;

    /**
     * Board constructor
     *
     * @param y - y size (height) of board (int)
     * @param x - x size (width) of board (int)
     * @throws BoardTooSmallException - forces app to exit if board size is too small
     */
    public BoardMultithreading(int y, int x) throws BoardTooSmallException {
        if (x < 5 || y < 5) {
            throw new BoardTooSmallException("Board must be at least 5 x 5");
        }
        this.board = new Dot[y][x];
        this.modelPlacer = new ModelPlacer(this);
        this.nextGenBoard = newEmptyBoard();
        this.runnableArray.add(new WorkerThread(this));

        for (int i = 0; i < board.length; i++) {
            if(!runnableArray.get(runnableArray.size()-1).addLineAndCheckCapacity(i)){
                this.runnableArray.add(new WorkerThread(this));
                runnableArray.get(runnableArray.size()-1).addLineAndCheckCapacity(i);
            }
            if(i == board.length){
                runnableArray.get(runnableArray.size()-1).trim();
            }
        }
        for (WorkerThread worker : runnableArray
                ) {
            Thread runner = new Thread(worker);
            runner.setDaemon(true);
            runner.start();
        }
//TODO workers division by dot numbers not line count

    }

    /**
     * nextGen
     * <p>
     * Method for calculating new generation - depends on rule to live and rule to get alive
     */
    public void nextGen() {
        modelPlacer.checkForInputs();
        if(!ongoingUpdate){
            while(modelPlacer.runWhileNotBusy.size() > 0){
                modelPlacer.runWhileNotBusy.get(0).run();
                modelPlacer.runWhileNotBusy.remove(0);
            }

            nextGenBoard = newEmptyBoard();
            ongoingUpdate = true;
            synchronized (this){
                this.notifyAll();
            }
        }
    }

    synchronized void updater(){
        workersDoneCount++;
        if(workersDoneCount == runnableArray.size()){
            board = nextGenBoard;
            workersDoneCount = 0;
            generation++;
            ongoingUpdate = false;
        }
    }

    public int getGeneration(){
        int gen = generation;
        generation = 0;
        return gen;
    }


    /**
     * newEmptyBoard
     * <p>
     * helper function for nextGen and clearBoard
     *
     * @return - returns empty board of size given in Config class
     */
    private Dot[][] newEmptyBoard() {
        int xLength = board[0].length;
        int yLength = board.length;
        return new Dot[yLength][xLength];
    }

    /**
     * changeOnPosition
     *
     * toggle state on boards given position
     * if null - creates new Dot on given position
     * if Dot - insert null instead
     *
     * @param x - x position on board
     * @param y - y position on board
     */
    public void changeOnPosition(int x, int y) {
        if (board[y][x] == null) {
            board[y][x] = new Dot();
        } else {
            board[y][x] = null;
        }
    }

    @Override
    public boolean isBusy() {
        return ongoingUpdate;
    }

    @Override
    public void checkForInput() {
        modelPlacer.checkForInputs();
    }

    /**
     * clearBoard
     *
     * calls new EmptyBoard function and assign result to board
     */
    public void clearBoard() {
        board = newEmptyBoard();
    }

    /**
     * initExampleBoard
     *
     * function for placing example Dots on board defined in function
     * if Dot position is out of board bounds - ignore ( does not place )
     */
    public void initExampleBoard() {
        modelPlacer.placeGlider(30,20);
        modelPlacer.placeSomething(50,80);
        modelPlacer.placeGliderGun(1,1);
    }

    public Dot[][] getBoard() {
        return board;
    }
}
