package com.sikoramarek.Model.MultiThread;

import com.sikoramarek.Common.Config;
import com.sikoramarek.Common.Logger;
import com.sikoramarek.Model.Dot;
import com.sikoramarek.Model.RuleManager;

import java.util.Arrays;

class WorkerThread implements Runnable{
    private int linesLimit = 50;
    private int[] lineNumbers = new int[linesLimit];
    private final BoardMultithreading model;
    private boolean alive = true;
    private int indexNumber = 0;

    WorkerThread(BoardMultithreading board){
        this.model = board;
    }

    boolean addLineAndCheckCapacity(int lineNr){
        if(indexNumber<linesLimit){
            lineNumbers[indexNumber] = lineNr;
            indexNumber++;
            return true;
        }else{
            return false;
        }
    }

    void trim(){
        int[] temp = new int[indexNumber-1];
        System.arraycopy(lineNumbers, 0, temp, 0, indexNumber-1);
        lineNumbers = temp;
    }

    private void lineNextGen(){
        for (int line : lineNumbers
                ) {
            for (int i = 0; i < model.board[line].length; i++) {
                int aliveNeighbors = getNeighbors(i, line);
                Dot currentSourceDot = model.board[line][i];
                if (currentSourceDot != null && Arrays.stream(RuleManager.ruleToLive).anyMatch(value -> value == aliveNeighbors)) {
                    model.nextGenBoard[line][i] = currentSourceDot;
                } else if ((currentSourceDot == null && Arrays.stream(RuleManager.ruleToGetAlive).anyMatch(value -> value == aliveNeighbors))) {
                    model.nextGenBoard[line][i] = new Dot();
                }
            }
        }
    }

    private int getNeighbors(int boardTargetXposition, int boardTargetYposition) {
        int neighbors = 0;
        int leftOfDownThreshold = -1;
        int rightOrUpTreshold = 1;
        int thisPosition = 0;
        for (int i = leftOfDownThreshold; i <= rightOrUpTreshold; i++) {
            for (int j = leftOfDownThreshold; j <= rightOrUpTreshold; j++) {

                int checkYposition = boardTargetYposition + i;
                int checkXposition = boardTargetXposition + j;

                if(Config.isWorldWrapping()){
                    if(checkXposition == model.board[0].length){
                        checkXposition = 0;
                    }
                    if(checkXposition < 0){
                        checkXposition = model.board[0].length-1;
                    }
                    if(checkYposition == model.board.length-1){
                        checkYposition = 0;
                    }
                    if(checkYposition < 0){
                        checkYposition = model.board.length-1;
                    }
                }

                try {
                    if (model.board[checkYposition][checkXposition] != null && !(i == thisPosition && j == thisPosition)) {
                        neighbors++;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
        return neighbors;
    }

    @Override
    public void run(){
        while(alive){
            if(model.ongoingUpdate){
                lineNextGen();
                model.updater();
                try {
                    synchronized (model){
                        model.wait();
                    }

                } catch (InterruptedException e) {
                    Logger.error(e.getMessage(), this);
                }
            }
            else{
                try {
                    synchronized (model){
                        model.wait();
                    }
                } catch (InterruptedException e) {
                    Logger.error(e.getMessage(), this);
                }
            }

        }
    }

}
