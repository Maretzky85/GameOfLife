package com.sikoramarek.View.Implementations;

import com.sikoramarek.Common.Config;
import com.sikoramarek.Common.Logger;
import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Model.Dot;
import com.sikoramarek.View.ViewInterface;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.DecimalFormat;

import static com.sikoramarek.Common.Config.*;
import static javafx.scene.input.MouseEvent.MOUSE_RELEASED;

/**
 * JavaFX View Class implements ViewInterface class
 * For viewing passed board model and passing input from user to Observable Class
 */
public class JavaFXView implements ViewInterface {

    private Group viewBoard = new Group();
    private Scene gameScene = new Scene(viewBoard, WIDTH, HEIGHT, Color.BLACK);
    private Rectangle[][] viewRectangleTable;
    private boolean ongoingUpdateFromModel = false;
    private boolean ongoingUpdateFromView = false;
    private int droppedFrames = 0;
    private int renderedFrames = 0;

    private int iterator = 0;
    private WelcomeAnimation welcomeAnimation = new WelcomeAnimation(this::toggleRow);

    private Text tutorialPlaceholder;

    public JavaFXView() {
    }

    public Scene getScene() {
        if(welcomeAnimation.started()){
            iterator = 0;
            welcomeAnimation.startAnimation();
        }
        return gameScene;
    }

    private void toggleRow() {
        for (Rectangle[] rectangles : viewRectangleTable) {
            rectangles[iterator].setFill(welcomeAnimation.getToggleColor());
        }
        if (iterator < X_SIZE-1){
            iterator++;
        }else{
            iterator = 0;
        }
    }

    /**
     * viewInit method
     * Sets stages title, creates and initialises reflecting rectangle table for holding view`s side rectangles
     * Defines rectangle appearance
     * Calls gameScene set and view methods for showing window.
     * Calls attachResizeListeners function to attach proper listeners to stage and gameScene
     */
    @Override
    public void viewInit() throws SystemConfigTooWeekException {
        Logger.log("Initialising Scene.", this);

        long startTime = System.currentTimeMillis();

        Logger.log("Initialising grid", this);

        initGrid();

        Logger.log("Done. Initialising grid took " + (System.currentTimeMillis() - startTime) + " ms", this);

        gameScene.setCursor(Cursor.CROSSHAIR);

        tutorialPlaceholder = new Text(100, 50, "");
        tutorialPlaceholder.setFill(Color.WHITE);
        tutorialPlaceholder.setFont(new Font(30));
        viewBoard.getChildren().add(tutorialPlaceholder);

        attachResizeListeners();
        
        Logger.log("Initialising took " + (System.currentTimeMillis() - startTime) + " ms", this);

    }

    /**
     * Attaches listeners for stage width and height and calls resizeGrid if needed
     */
    private void attachResizeListeners(){
        final int WINDOW_UPPER_BAR_THRESHOLD = -30;
        
        gameScene.widthProperty().addListener((observable, oldValue, newValue) -> {
            Config.setRequestedWindowWidth(newValue.intValue());
            resizeGrid();
        });

        gameScene.heightProperty().addListener((observable, oldValue, newValue) -> {
            Config.setRequestedWindowHeight(newValue.intValue()+WINDOW_UPPER_BAR_THRESHOLD);
            resizeGrid();
        });
    }

    private void resizeGrid(){
        for (int boardYposition = 0; boardYposition < Y_SIZE; boardYposition++) {
            for (int boardXposition = 0; boardXposition < X_SIZE; boardXposition++) {
                viewRectangleTable[boardYposition][boardXposition].setHeight(RECTANGLE_HEIGHT);
                viewRectangleTable[boardYposition][boardXposition].setWidth(RECTANGLE_WIDTH);
                viewRectangleTable[boardYposition][boardXposition].setX(RECTANGLE_WIDTH * boardXposition);
                viewRectangleTable[boardYposition][boardXposition].setY(RECTANGLE_HEIGHT * boardYposition);
                viewRectangleTable[boardYposition][boardXposition].setArcHeight(RECTANGLE_ARC_HEIGHT);
                viewRectangleTable[boardYposition][boardXposition].setArcWidth(RECTANGLE_ARC_WIDTH);
            }
        }
    }

    private void initGrid() throws SystemConfigTooWeekException {
        long initStartTime = System.currentTimeMillis();
        long counter = System.currentTimeMillis();

        viewRectangleTable = new Rectangle[Y_SIZE][X_SIZE];
        for (int boardYposition = 0; boardYposition < Y_SIZE; boardYposition++) {

            long timeTaken = System.currentTimeMillis() - initStartTime;

            for (int boardXposition = 0; boardXposition < X_SIZE; boardXposition++) {

                Rectangle rectangleToAdd = new Rectangle
                        (boardXposition * RECTANGLE_WIDTH,
                                boardYposition * RECTANGLE_HEIGHT,
                                RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
                rectangleToAdd.setArcHeight(RECTANGLE_ARC_HEIGHT);
                rectangleToAdd.setArcWidth(RECTANGLE_ARC_WIDTH);
                viewRectangleTable[boardYposition][boardXposition] = rectangleToAdd;
                viewBoard.getChildren().add(viewRectangleTable[boardYposition][boardXposition]);

                if(timeTaken > 500){
                    long currentTime = System.currentTimeMillis();
                    if(counter - currentTime < -500){
                        counter = System.currentTimeMillis();
                        Logger.log( ""+
                                new DecimalFormat("#0.0")
                                        .format((double)
                                                (X_SIZE*boardYposition+boardXposition)/(Y_SIZE*X_SIZE)*100) +" % ", this);
                    }
                }

                if(timeTaken > 5000){
                    throw new SystemConfigTooWeekException("Creating 2d View takes too long, impossible to run");
                }
            }
        }
    }


    /**
     * updateViewOnPos function
     * updates view side of model on-the-fly (before sending to model)
     * for immediate reaction to clicks
     *
     * ongoingUpdateFromView - variable for synchronising update rates - prevents flooding updates to view
     *
     * @param event - any input event acceptable, mouse or key event are supported here, else is discarded
     */
    private void updateViewOnPos(MouseEvent event) {
        ongoingUpdateFromView = true;
        Platform.runLater(() -> {
            if (event.getPickResult().getIntersectedNode() != null
                    && event.getPickResult().getIntersectedNode().getClass().equals(Rectangle.class)) {
                Rectangle rectangle = (Rectangle) event.
                        getPickResult().
                        getIntersectedNode();
                if (rectangle.getFill().equals(Color.WHITE)) {
                    rectangle.
                            setFill(DEAD_COLOR);
                } else {
                    rectangle.
                            setFill(Color.WHITE);
                }
            }
            ongoingUpdateFromView = false;
        });

    }


    /**
     * refresh method takes board as argument, scans model board and updates representing view board accordingly.
     *
     * ongoingUpdateFromModel and ongoingUpdateFromView must be set to false for update to take place
     * if not, function add a drop frame and discard this view update
     *
     * @param board - 2D board from model
     */
    @Override
    public void refresh(Dot[][] board) {
        if (!ongoingUpdateFromModel && !ongoingUpdateFromView) {
            ongoingUpdateFromModel = true;
            Platform.runLater(() -> {
                for (int i = 0; i < Y_SIZE; i++) {
                    for (int j = 0; j < X_SIZE; j++) {
                        Rectangle rectangle = viewRectangleTable[i][j];
                        if (board[i][j] != null) {
                            rectangle.setFill(board[i][j].getColor());
                        } else {
                            rectangle.setFill(DEAD_COLOR);
                        }
                    }
                }
                renderedFrames++;
                ongoingUpdateFromModel = false;
            });
        } else {
            droppedFrames++;
        }
    }


    /**
     * getDroppedFrames function returns dropped frames count from last call
     * when called set droppedFrames to 0, and return count from call moment
     *
     * @return int count of dropped frames in time from last call to now
     */
    public int getDroppedFrames() {
        int droppedFramesCurrent = droppedFrames;
        droppedFrames = 0;
        return droppedFramesCurrent;
    }

    @Override
    public int getRenderedFrames() {
        int currentRenderedFrames = renderedFrames;
        renderedFrames = 0;
        return currentRenderedFrames;
    }

    @Override
    public void handleKeyboard(KeyEvent event) {

    }

    @Override
    public void handleMouse(MouseEvent me) {
        if (me.getEventType().equals(MOUSE_RELEASED)) {
            if (me.getButton() == MouseButton.PRIMARY) {
                updateViewOnPos(me);
            }
        }
    }

    @Override
    public Text getTutorialPlaceholder() {
        return tutorialPlaceholder;
    }


    @Override
    public String toString(){
        return "JavaFX";
    }


}
