package com.sikoramarek.Model;

import com.sikoramarek.Common.BoardTooSmallException;
import com.sikoramarek.Model.MultiThread.BoardMultithreading;
import com.sikoramarek.Model.SingleThread.BoardSingleThread;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ModelPerformanceTests {
    int size;
    int timeToRun;
    BoardSingleThread boardSingleThread;
    BoardMultithreading boardMulti;
    RuleManager ruleManager;
    long multiResoult;
    long singleResoult;
    float percent;

    @Parameterized.Parameters
    public static Collection settings() {
        return Arrays.asList(new Object[][]{
                {5, 5000},
                {10, 5000},
                {100, 5000},
                {150, 5000},
                {200, 5000},
                {250, 5000},
                {500, 5000},
                {1000, 5000}
        });
    }

    public ModelPerformanceTests(int size, int timeToRun){
        this.timeToRun = timeToRun;
        this.size = size;
    }


    @Before
    public void init(){
        ruleManager = new RuleManager();
        try {
            boardSingleThread = new BoardSingleThread(size,size, ruleManager);
        } catch (BoardTooSmallException e) {
            e.printStackTrace();
        }
        boardSingleThread.initExampleBoard();

        try {
            boardMulti = new BoardMultithreading(size,size, ruleManager);
        } catch (BoardTooSmallException e) {
            e.printStackTrace();
        }
        boardMulti.initExampleBoard();

        }


    @Test
    public void performanceTest() {
        long startSingleTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        while(currentTime - startSingleTime < timeToRun){
            boardSingleThread.nextGen();
            Thread.yield();
            currentTime = System.currentTimeMillis();
        }
        singleResoult = boardSingleThread.getGeneration();

        long startMultiTime = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();

        while(currentTime - startMultiTime < timeToRun){
            if(!boardMulti.ongoingUpdate){
                boardMulti.nextGen();
            }
            Thread.currentThread().yield();
            currentTime = System.currentTimeMillis();
        }

        multiResoult = boardMulti.getGeneration();

//        System.out.println("Time taken for MultiThread: "+ multiResoult + " ms");

        percent = (float)multiResoult/(float)singleResoult*100;
//        System.out.println("MultiThreaded is "+ percent +" % of SingleThreaded");

        assertTrue("MultiThreaded is "+ percent +" % of SingleThreaded", multiResoult > singleResoult);
    }

    @After
    public void Results(){

        System.out.println();
        System.out.print("Test case: board size: "+size + " time to run: "+timeToRun+" ms\nResoults:"+
                "\nSingle thread generations in 5s: "+singleResoult+"\nMulti thread generations in 5s: "+multiResoult+
                " \nMulti thread is "+percent+" percent of Single thread speed");
        System.out.println();
        System.out.println();
    }
}