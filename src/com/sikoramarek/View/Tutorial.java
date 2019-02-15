package com.sikoramarek.View;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

class Tutorial {

    private int DISPLAY_DURATION = 4000;
    private List<Text> placeholders = new ArrayList<>();
    private int currentLine = 0;
    private Timeline timeline;

    private String[] tutorial = new String[]{
            "",
            "Welcome in Game Of Life",
            "This guide will walk You through game basics",
            "press N for placing example game",
            "Press P or right click to unpause/pause",
            "In 3D mode You can light up whole board using L",
            "Use this if You want to change state of cells",
            "You can add or remove cells by left clicking on them",
            "In 2D mode You can click without lighting up",
            "In 3D mode You can rotate board by simply dragging",
            "You also can press V for corner elements to show",
            "They dont do anything, just looking good",
            "You can press C to clear board",
            "TAB is for changing views (2D/3D)",
            "press M for starting menu",
    };

    Tutorial(){
        makeTimeline();
    }

    void playTutorial(){
        timeline.playFromStart();
    }

    void addTextHolders(Text textHolder){
        placeholders.add(textHolder);
    }

    private void makeTimeline(){

        KeyFrame[] keyframes = new KeyFrame[tutorial.length];
        for (int i = 0; i < tutorial.length; i++) {
            keyframes[i] = new KeyFrame(Duration.millis(DISPLAY_DURATION+i*DISPLAY_DURATION), event -> changeLine());
        }

        timeline = new Timeline(keyframes);
        timeline.setOnFinished((event) -> timeline.stop());

    }

    private void changeLine() {
        if(currentLine < tutorial.length-1){
            currentLine++;
        }else{
            currentLine = 0;
        }

        String displayLine = tutorial[currentLine];

        for (Text text : placeholders
                ) {
            text.setText(displayLine);
        }
    }

}
