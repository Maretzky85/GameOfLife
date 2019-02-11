package com.sikoramarek.View;

import com.sikoramarek.Common.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import static com.sikoramarek.Common.Config.HEIGHT;
import static com.sikoramarek.Common.Config.WIDTH;

public class WindowedMenu {

    Runnable runnable;
    GridPane menuGroup = new GridPane();
    private Scene menu = new Scene(menuGroup, WIDTH, HEIGHT, Color.WHITE);

    public WindowedMenu(Runnable runnable){
        this.runnable = runnable;
        init();
    }

    public WindowedMenu(){
        init();
    }

    private void init(){
        Object[] labels = new Object[]
                {"Window Height", "Window Width", "Board X size","Board Y size", "Frame Rate", "Console View",
                "JavaFX View", "JavaFX3D View"};



        menuGroup.setPadding(new Insets(10, 10, 10, 10));
        menuGroup.setVgap(5);
        menuGroup.setHgap(5);

        BackgroundSize backgroundSize = new BackgroundSize((int) Screen.getPrimary().getBounds().getWidth(),
                (int)Screen.getPrimary().getBounds().getHeight(), true, true, true, true);

        menuGroup.setBackground(new Background(new BackgroundImage(new Image("gameoflife.jpg",(int) Screen.getPrimary().getBounds().getWidth(),
                (int)Screen.getPrimary().getBounds().getHeight(),
                false,true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize)));

        TextField wHeight = new TextField(Integer.toString(Config.getRequestedWindowHeight()));
        GridPane.setConstraints(wHeight, 1, 0);
        TextField wWidth = new TextField(Integer.toString(Config.getRequestedWindowWidth()));
        GridPane.setConstraints(wWidth, 1, 1);
        TextField xSize = new TextField(Integer.toString(Config.getxSize()));
        GridPane.setConstraints(xSize, 1, 2);
        TextField ySize = new TextField(Integer.toString(Config.getySize()));
        GridPane.setConstraints(ySize, 1, 3);
        TextField frameRate = new TextField(Integer.toString(Config.getFrameRate()));
        GridPane.setConstraints(frameRate, 1, 4);
        CheckBox consoleViewBox = new CheckBox();
        consoleViewBox.setSelected(Config.CONSOLE_VIEW);
        GridPane.setConstraints(consoleViewBox, 1, 5);
        CheckBox javaFXViewBox = new CheckBox();
        javaFXViewBox.setSelected(Config.JAVAFX_VIEW);
        GridPane.setConstraints(javaFXViewBox, 1, 6);
        CheckBox jFX3dBox = new CheckBox();
        jFX3dBox.setSelected(Config.VIEW_3D);
        GridPane.setConstraints(jFX3dBox, 1, 7);
        menuGroup.getChildren().addAll(wHeight,wWidth,xSize,ySize,frameRate, consoleViewBox, javaFXViewBox, jFX3dBox);

        labelBuilder(0, labels);

        Button save = new Button("Run");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Config.setRequestedWindowHeight(Integer.valueOf(wHeight.getText()));
                Config.setRequestedWindowWidth(Integer.valueOf(wWidth.getText()));
                Config.setXsize(Integer.valueOf(xSize.getText()));
                Config.setYsize(Integer.valueOf(ySize.getText()));
                Config.FRAME_RATE = Integer.valueOf(frameRate.getText());
                Config.CONSOLE_VIEW = consoleViewBox.isSelected();
                Config.JAVAFX_VIEW = javaFXViewBox.isSelected();
                Config.VIEW_3D = jFX3dBox.isSelected();
                System.out.println("saved!");
                runnable.run();
            }
        });
        GridPane.setConstraints(save, 0, labels.length);
        menuGroup.getChildren().add(save);
    }

    private void labelBuilder(int columnIndex, Object[] any){
        for(int i = 0; i < any.length; i++){
            Label newLabel = new Label(any[i].toString());
            GridPane.setConstraints(newLabel, 0, i);
            menuGroup.getChildren().add(newLabel);
        }
    }

    public Scene getMenu() {
        return menu;
    }
}
