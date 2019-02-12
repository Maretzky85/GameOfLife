package com.sikoramarek.View;

import com.sikoramarek.Common.Config;
import com.sikoramarek.Common.SharedResources;
import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Controller.Controller;
import com.sikoramarek.Model.Dot;
import com.sikoramarek.View.Implementations.ConsoleView;
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

public class ViewManager implements ViewInterface {

    private Runnable initializer;
    private Stage primaryStage;
    private ArrayList<ViewInterface> views = new ArrayList<>();
//    private InputHandler inputHandler = new InputHandler();

    private WindowedMenu menu;

    private int currentView = 0;

    public ViewManager(Stage primaryStage, Runnable initializer){
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Game Of Life  v " + VERSION);
        primaryStage.show();
        Config.setRequestedWindowHeight((int)Screen.getPrimary().getBounds().getHeight());
        Config.setRequestedWindowWidth((int)Screen.getPrimary().getBounds().getWidth());


        this.initializer = initializer;
        this.primaryStage = primaryStage;
        menu = new WindowedMenu(() -> {
            try {
                viewInit();
            } catch (SystemConfigTooWeekException e) {
                e.printStackTrace();
            }
        });
        primaryStage.setScene(menu.getMenu());
    }

    private void sceneToggle(){
        //TODO
        if (currentView < views.size()-1){
                currentView++;
        }else{
            currentView = 0;
        }
        primaryStage.setScene(views.get(currentView).getScene());
    }

    @Override
    public Scene getScene() {
        return null;
    }

    @Override
    public void viewInit() throws SystemConfigTooWeekException {
        views = new ArrayList<>();
        currentView = 0;
        if(Config.CONSOLE_VIEW){
            views.add(new ConsoleView());
        }
        if(Config.JAVAFX_VIEW){
            views.add(new JavaFXView());
        }
        if(Config.VIEW_3D){
            views.add(new JavaFX3DView());
        }
        for (ViewInterface view : views
                ) {
            view.viewInit();
            attachKeyHandler(view.getScene());

        }
        initializer.run();
        primaryStage.setScene(views.get(currentView).getScene());
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

    @Override
    public void refresh(Dot[][] board) {
        for (ViewInterface view : views
                ) {
            view.refresh(board);
        }
    }

    @Override
    public void attachObserver(Controller controller) {
//        inputHandler.addObserver(controller);
//        for (ViewInterface view : views
//                ) {
//            view.attachObserver(controller);
//        }
    }

    @Override
    public int getDroppedFrames() {
        return 0;
    }

    @Override
    public int getRenderedFrames() {
        return 0;
    }

    @Override
    public void handleKeyboard(KeyEvent event) {

    }

    @Override
    public void handleMouse(MouseEvent me) {

    }
}
