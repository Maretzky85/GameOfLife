package com.sikoramarek.View;

import com.sikoramarek.Common.Config;
import com.sikoramarek.Common.Logger;
import com.sikoramarek.Common.SharedResources;
import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Model.Dot;
import com.sikoramarek.View.Implementations.ConsoleOutput;
import com.sikoramarek.View.Implementations.JavaFXView;
import com.sikoramarek.View.Implementations.View3D.JavaFX3DView;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.sikoramarek.Common.Config.*;
import static javafx.scene.input.KeyCode.M;

public class ViewManager{

    private Runnable initializer;
    private Stage primaryStage;
    private ArrayList<ViewInterface> views = new ArrayList<>();
    private ConsoleOutput consoleOutput;
    private Tutorial tutorial = new Tutorial();
    private WindowedMenu menu;
    private InputHandler inputHandler = new InputHandler();

    private int currentView = 0;
    public boolean boardLoadSuccess;


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
        removeHandlers(primaryStage.getScene());
        if (currentView < views.size()-1){
                currentView++;
        }else{
            currentView = 0;
        }
        attachHandlers(views.get(currentView).getScene());
        primaryStage.setScene(views.get(currentView).getScene());
        primaryStage.setFullScreen(true);
    }

    private void viewInit(){
        views = new ArrayList<>();
        consoleOutput = null;
        currentView = 0;

        long startTime = System.currentTimeMillis();
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
        if(System.currentTimeMillis() - startTime > 2000){
            Logger.log("\n\n*************************************************************\n" +
                    "            WARNING: Performance may be low    WARNING\n" +
                    "*************************************************************\n", this);
        }
        if (views.size() > 0){
            initializer.run();
            if(boardLoadSuccess){
                primaryStage.setScene(views.get(currentView).getScene());
                attachHandlers(primaryStage.getScene());
                for (ViewInterface view : views
                ) {
                    view.getScene().setCursor(Cursor.CROSSHAIR);
                    tutorial.addTextHolders(view.getTutorialPlaceholder());
                }
                tutorial.playTutorial();
            }
            primaryStage.setFullScreen(true);
        }else{
            Logger.error("Cannot initialize view, please change board size settings", this);
        }
    }

    private void initSingleView(ViewInterface view){
        try{
            view.viewInit();
            views.add(view);
        }catch (SystemConfigTooWeekException exception){
            Logger.error(exception.getMessage(), view);
        }
    }

    private void removeHandlers(Scene scene){
        scene.setOnMouseReleased(null);
        scene.setOnMousePressed(null);
        scene.setOnMouseDragged(null);
        scene.setOnKeyReleased(null);
    }

    private void attachHandlers(Scene scene) {
        scene.setOnMouseReleased(event -> {
            views.get(currentView).handleMouse(event);
            inputHandler.handleInput(event);
        });

        scene.setOnMousePressed(event -> views.get(currentView).handleMouse(event));

        scene.setOnMouseDragged(event -> views.get(currentView).handleMouse(event));

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case TAB:
                    sceneToggle();
                    break;
                case M:
                    SharedResources.addKeyboardInput(M);
                    primaryStage.setScene(menu.getMenu());
                    break;
                case F:
                    primaryStage.setFullScreen(true);
                default:
                    views.get(currentView).handleKeyboard(event);
                    inputHandler.handleInput(event);
                    break;
            }
        });
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

    public int getRenderedFrames() {
        return views.get(currentView).getRenderedFrames();
    }

    public int getDroppedFrames() {
        return views.get(currentView).getDroppedFrames();
    }
}
