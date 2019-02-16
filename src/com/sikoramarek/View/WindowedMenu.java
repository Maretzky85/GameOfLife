package com.sikoramarek.View;

import com.sikoramarek.Common.Config;
import com.sikoramarek.Common.Logger;
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

    TextField wHeight;
    TextField wWidth;

    private Runnable gameStarter;
    private GridPane menuGroup;
    private Scene menu;
    private String[] labels = new String[]{
            "Window Height",
            "Window Width",
            "Board X size",
            "Board Y size",
            "Frame Rate",
            "Console View",
            "JavaFX View",
            "JavaFX3D View",
            "World wrapping"};

    WindowedMenu(Runnable viewInitializer){
        this.gameStarter = viewInitializer;
        menuGroup = new GridPane();
        menu = new Scene(menuGroup, WIDTH, HEIGHT, Color.WHITE);
        init();
    }

    private void init(){
        menuGroup.setPadding(new Insets(10, 10, 10, 10));
        menuGroup.setVgap(5);
        menuGroup.setHgap(5);

        BackgroundSize backgroundSize = new BackgroundSize(
                (int) Screen.getPrimary().getBounds().getWidth(),
                (int)Screen.getPrimary().getBounds().getHeight(),
                true,
                true,
                true,
                true);

        try{
            menuGroup.setBackground(
                    new Background(
                            new BackgroundImage(
                                    new Image("gameoflife.jpg",
                                            (int) Screen.getPrimary().getBounds().getWidth(),
                                            (int)Screen.getPrimary().getBounds().getHeight(),
                                            false,
                                            true),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            backgroundSize)
                    )
            );
        }catch (IllegalArgumentException exception){
            Logger.error(exception.getMessage(), this);
        }


        wHeight = new TextField(Integer.toString(Config.getRequestedWindowHeight()));
        GridPane.setConstraints(wHeight, 1, 0);

        wWidth = new TextField(Integer.toString(Config.getRequestedWindowWidth()));
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

        CheckBox worldWrapingBox = new CheckBox();
        worldWrapingBox.setSelected(Config.isWorldWrapping());
        GridPane.setConstraints(worldWrapingBox, 1, 8);

        menuGroup.getChildren().addAll(
                wHeight,
                wWidth,
                xSize,
                ySize,
                frameRate,
                consoleViewBox,
                javaFXViewBox,
                jFX3dBox,
                worldWrapingBox);

        labelBuilder(labels);

        Button saveButton = new Button("Start");
        saveButton.setOnAction(event -> {
            Config.setRequestedWindowHeight(Integer.valueOf(wHeight.getText()));
            Config.setRequestedWindowWidth(Integer.valueOf(wWidth.getText()));
            Config.setXsize(Integer.valueOf(xSize.getText()));
            Config.setYsize(Integer.valueOf(ySize.getText()));
            Config.setFrameRate(Integer.valueOf(frameRate.getText()));
            Config.setConsoleView(consoleViewBox.isSelected());
            Config.setJavafxView(javaFXViewBox.isSelected());
            Config.setView3d(jFX3dBox.isSelected());
            Config.setWorldWrapping(worldWrapingBox.isSelected());
            gameStarter.run();
        });

        GridPane.setConstraints(saveButton, 0, labels.length);
        menuGroup.getChildren().add(saveButton);
    }

    private void labelBuilder(Object[] any){
        for(int i = 0; i < any.length; i++){
            Label newLabel = new Label(any[i].toString());
            GridPane.setConstraints(newLabel, 0, i);
            menuGroup.getChildren().add(newLabel);
        }
    }
    @Override
    public String toString(){
        return "Menu";
    }

    Scene getMenu() {
        return menu;
    }
}
