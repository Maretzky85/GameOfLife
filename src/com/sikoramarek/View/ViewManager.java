package com.sikoramarek.View;

import com.sikoramarek.Common.Config;
import com.sikoramarek.Common.Logger;
import com.sikoramarek.Common.SharedResources;
import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Model.Dot;
import com.sikoramarek.View.Implementations.ConsoleOutput;
import com.sikoramarek.View.Implementations.JavaFXView;
import com.sikoramarek.View.Implementations.View3D.BoxB;
import com.sikoramarek.View.Implementations.View3D.JavaFX3DView;

import javafx.scene.Scene;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.sikoramarek.Common.Config.*;
import static javafx.scene.input.KeyEvent.KEY_RELEASED;
import static javafx.scene.input.MouseEvent.MOUSE_RELEASED;

public class ViewManager{

    private Runnable initializer;
    private Stage primaryStage;
    private ArrayList<ViewInterface> views = new ArrayList<>();
    private ConsoleOutput consoleOutput;
    private Tutorial tutorial = new Tutorial();
    private WindowedMenu menu;

    private int currentView = 0;


    public ViewManager(Stage primaryStage, Runnable modelInitializer){
        this.initializer = modelInitializer;
        this.primaryStage = primaryStage;

        primaryStage.setMaximized(true);
        primaryStage.setTitle("Game Of Life  v " + VERSION);
        primaryStage.show();

        Config.setRequestedWindowHeight((int)Screen.getPrimary().getBounds().getHeight());
        Config.setRequestedWindowWidth((int)Screen.getPrimary().getBounds().getWidth());

        menu = new WindowedMenu(this::viewInit);
        primaryStage.setScene(menu.getMenu());
    }

    private void sceneToggle(){
        if (currentView < views.size()-1){
                currentView++;
        }else{
            currentView = 0;
        }
        primaryStage.setScene(views.get(currentView).getScene());
    }

    public void viewInit(){
        views = new ArrayList<>();
        consoleOutput = null;
        currentView = 0;

        if(Config.VIEW_3D){
            initSingleView(new JavaFX3DView());
        }
        if(Config.JAVAFX_VIEW){
            initSingleView(new JavaFXView());
        }
        if(Config.CONSOLE_VIEW){
            consoleOutput = new ConsoleOutput();
            consoleOutput.viewInit();
        }
        if (views.size() > 0){
            initializer.run();
            primaryStage.setScene(views.get(currentView).getScene());
            for (ViewInterface view : views
                    ) {
                tutorial.addTextHolders(view.getText());

            }
            tutorial.timeline.playFromStart();

        }else{
            Logger.error("Cannot initialize view, please change board size settings", this);
        }
    }

    private void initSingleView(ViewInterface view){
        try{
            view.viewInit();
            attachKeyHandler(view.getScene());
            views.add(view);
        }catch (SystemConfigTooWeekException exception){
            Logger.error(exception.getMessage(), view);
        }
    }

    private void attachKeyHandler(Scene scene) {
        scene.setOnMouseReleased(event -> {
            views.get(currentView).handleMouse(event);
            handleInput(event);
        });

        scene.setOnMousePressed(event -> {
            views.get(currentView).handleMouse(event);
        });

        scene.setOnMouseDragged(event -> {
            views.get(currentView).handleMouse(event);
        });

        scene.setOnKeyReleased(event -> {
            if(event.getCode().equals(KeyCode.TAB)){
                sceneToggle();
            }else
            if(event.getCode().equals(KeyCode.M)){
                primaryStage.setScene(menu.getMenu());
            }else{
                views.get(currentView).handleKeyboard(event);
                handleInput(event);
            }
        });
    }

    public void handleInput(InputEvent event) {
        if (event.getEventType().equals(MOUSE_RELEASED)) {
            MouseEvent mouseEvent = (MouseEvent) event;
            switch (mouseEvent.getButton()) {
                case PRIMARY:
                    if (((MouseEvent) event).getPickResult().getIntersectedNode() != null){
                        if( ((MouseEvent) event).getPickResult().getIntersectedNode().getClass().equals(BoxB.class)){
                            BoxB rectangle = (BoxB) mouseEvent.getPickResult().getIntersectedNode();
                            int gridXposition = (rectangle.getBoardX());
                            int gridYposition = (rectangle.getBoardY());
                            int[] position = new int[]{gridXposition, gridYposition};
                            synchronized (SharedResources.keyboardInput){
                                SharedResources.positions.add(position);
                            }

                        }else{
                            if( ((MouseEvent) event).getPickResult().getIntersectedNode().getClass().equals(Rectangle.class)){
                            Rectangle rectangle = (Rectangle) mouseEvent.getPickResult().getIntersectedNode();
                            int gridXposition = (int) (rectangle.getX() / RECTANGLE_WIDTH);
                            int gridYposition = (int) (rectangle.getY() / RECTANGLE_HEIGHT);
                            int position[] = new int[]{gridXposition, gridYposition};
                            synchronized (SharedResources.keyboardInput){
                                SharedResources.positions.add(position);
                            }}
                        }
                    }
                    break;
                case SECONDARY:
                    synchronized (SharedResources.keyboardInput){
                        SharedResources.keyboardInput.add(KeyCode.P);
                    }

                    break;
                default:
                    break;
            }
        } else if (event.getEventType().equals(KEY_RELEASED)) {
            KeyEvent keyEvent = (KeyEvent) event;
            synchronized (SharedResources.keyboardInput){
                SharedResources.keyboardInput.add((keyEvent.getCode()));
            }

        }
    }

    public void refresh(Dot[][] board) {
        for (ViewInterface view : views
                ) {
            view.refresh(board);
        }
        if(CONSOLE_VIEW){
            consoleOutput.refresh(board);
        }
    }
    @Override
    public String toString(){
        return "View Manager";
    }
}
