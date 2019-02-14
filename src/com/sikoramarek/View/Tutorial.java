package com.sikoramarek.View;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Tutorial {

    int DISPLAY_DURATION = 4000;

    List<Text> placeholders = new ArrayList<>();

    public Tutorial(){
        makeTimeline();
    }

    public void addTextHolders(Text textholder){
        placeholders.add(textholder);
    }

    public String getDisplayLine() {
        timeline.play();
        return displayLine;
    }

    int currentline = 0;
    String displayLine;

    String[] tutorial = new String[]{
            "Welcome in Game Of Life",
            "This guide will walk You through game basics",
            "Press p or right click to unpause/pause",
            "press n for placing example game",
            "You can add or remove cells by left clicking on game area",
            "It can be easier when paused",
            "you can press C to clear board"
    };

    Timeline timeline;

    void makeTimeline(){

        KeyFrame[] keyframes = new KeyFrame[tutorial.length];
        for (int i = 0; i < tutorial.length; i++) {
            keyframes[i] = new KeyFrame(Duration.millis(DISPLAY_DURATION+i*DISPLAY_DURATION), event -> {changeLine();});
        }

        timeline = new Timeline(keyframes);
        timeline.setOnFinished((event) -> { timeline.playFromStart(); });

    }

    private void changeLine() {
        if(currentline < tutorial.length-1){
            currentline++;
        }else{
            currentline = 0;
        }

        displayLine = tutorial[currentline];

        for (Text text : placeholders
                ) {
            text.setText(displayLine);
        }
        System.out.println(displayLine);
    }

}
