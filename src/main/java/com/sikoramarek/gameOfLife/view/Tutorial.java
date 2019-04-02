package com.sikoramarek.gameOfLife.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

class Tutorial {

    private int DISPLAY_DURATION = 5000;

    private List<Text> placeholders = new ArrayList<>();
    private int currentLine = 0;
    private Timeline timeline;

    private String[] tutorial = new String[]{
            "",
            "Welcome in Game Of Life game",
            "This guide will walk You through game controls",
            "Press N for placing example cells",
            "Press P or right click to unpause/pause",
            "In 3D mode You can light up whole board using L key",
            "In 3D mode this will allow You to click on any cell",
            "In 3D mode press V to hide/show corner elements",
            "In 3D mode You can rotate board by simply click-dragging mouse",
            "Also move camera using W/S/A/D keys",
            "Rotate camera using LEFT and RIGHT ARROW",
            "And move up/down by UP/DOWN ARROW",
            "There are also speed modifier - SHIFT and CTRL",
            "Combine holding these keys along with any movement key",
            "If You get lost just press Z key",
            "Press TAB for changing views (2D/3D)",
            "You can click any cell to switch state (active / not active)",
            "You may want to pause (P) first",
            "You can press C to clear board",
            "Keys 1 thru 9 will change game rules",
            "Numpad keys 1-3 followed by click places different models",
            "You can experiment with these numbers",
            "F key will turn on full screen mode",
            "+ and - Keys allow You to change speed",
            "M key will return You to start menu",
            "Have fun!",
            ""
    };

    Tutorial(){
        makeTimeline();
    }

    void playTutorial(){
        timeline.stop();
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
