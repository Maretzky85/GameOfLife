package com.sikoramarek.View;

import com.sikoramarek.Common.Config;
import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Controller.Controller;
import com.sikoramarek.Model.Dot;
import com.sikoramarek.View.Implementations.Common.InputHandler;
import com.sikoramarek.View.Implementations.ConsoleView;
import com.sikoramarek.View.Implementations.JavaFXView;
import com.sikoramarek.View.Implementations.View3D.JavaFX3DView;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.sikoramarek.Common.Config.VERSION;

public class ViewManager implements ViewInterface {

    private Runnable initializer;
    private Stage primaryStage;
    private ArrayList<ViewInterface> views = new ArrayList<>();
    private InputHandler inputHandler = new InputHandler();

    private WindowedMenu menu;

    private int currentView = 0;

    public ViewManager(Stage primaryStage, Runnable initializer){
//        primaryStage.setFullScreen(true);
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
        scene.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.TAB)){
                sceneToggle();
            }else
            if(event.getCode().equals(KeyCode.M)){
                primaryStage.setScene(menu.getMenu());
            }else{
                inputHandler.handleInput(event);
            }
        });
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
        inputHandler.addObserver(controller);
        for (ViewInterface view : views
                ) {
            view.attachObserver(controller);
        }
    }

    @Override
    public int getDroppedFrames() {
        return 0;
    }

    @Override
    public int getRenderedFrames() {
        return 0;
    }
}
