package com.sikoramarek.View;

import com.sikoramarek.Common.Config;
import com.sikoramarek.Common.SystemConfigTooWeekException;
import com.sikoramarek.Controller.Controller;
import com.sikoramarek.Model.Dot;
import com.sikoramarek.View.Implementations.ConsoleView;
import com.sikoramarek.View.Implementations.JavaFXView;
import com.sikoramarek.View.Implementations.View3D.JavaFX3DView;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ViewManager implements ViewInterface {

    private Stage primarystage;
    private ArrayList<ViewInterface> views = new ArrayList<>();

    private WindowedMenu menu;

    public ViewManager(Stage primarystage){
        this.primarystage = primarystage;
        menu = new WindowedMenu(() -> {
            try {
                viewInit();
            } catch (SystemConfigTooWeekException e) {
                e.printStackTrace();
            }
        });
        primarystage.setScene(menu.getMenu());
        primarystage.show();
    }

    private void SceneToggler(){
        //TODO
    }

    @Override
    public Scene getScene() {
        return null;
    }

    @Override
    public void viewInit() throws SystemConfigTooWeekException {
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
        }
        attachKeyHandler();
        primarystage.setScene(views.get(0).getScene());
    }

    private void attachKeyHandler() {
        primarystage.getScene().setOnKeyPressed(event -> {
            System.out.println(event.getText());
            if(event.getCode().equals(KeyCode.TAB)){
                primarystage.setScene(views.get(1).getScene());
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
