package Model.MultiThread;

import Model.Dot;

import java.util.Arrays;

class WorkerThread implements Runnable{
    int linesLimit = 50;
    int[] lineNumber = new int[linesLimit];
    BoardMultithreading model;
    boolean alive = true;
    int indexNumber = 0;

    WorkerThread(BoardMultithreading board){
        this.model = board;
    }

    boolean addCheckLineNr(int lineNr){
        if(indexNumber<linesLimit){
            lineNumber[indexNumber] = lineNr;
            indexNumber++;
            return true;
        }else{
            return false;
        }

    }

    void trim(){
        int[] temp = new int[indexNumber-1];
        System.arraycopy(lineNumber, 0, temp, 0, indexNumber-1);
        lineNumber = temp;
    }

    private void lineNextGen(){
        for (int line : lineNumber
                ) {
            for (int i = 0; i < model.board[line].length; i++) {
                int aliveNeighbors = getNeighbors(i, line);
                Dot currentSourceDot = model.board[line][i];
                if (currentSourceDot != null && Arrays.stream(model.ruleToLive).anyMatch(value -> value == aliveNeighbors)) {
                    model.nextGenBoard[line][i] = currentSourceDot;
                } else if ((currentSourceDot == null && Arrays.stream(model.ruleToGetAlive).anyMatch(value -> value == aliveNeighbors))) {
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
                try {
                    if (model.board[boardTargetYposition + i][boardTargetXposition + j] != null && !(i == thisPosition && j == thisPosition)) {
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
//                            System.out.println("Thread " + currentThread().getId() + " put to sleep");
                        model.wait();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    synchronized (model){
//                            System.out.println("Thread " + currentThread().getId() + " put to sleep");
                        model.wait();
                    }
                } catch (InterruptedException e) {
//                        System.out.println(currentThread().getId() + " Thread get interrupted");
                    e.printStackTrace();
                }
            }

        }
    }

}
