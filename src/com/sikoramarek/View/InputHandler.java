package com.sikoramarek.View;

import com.sikoramarek.View.Implementations.View3D.BoxB;

import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import static com.sikoramarek.Common.Config.RECTANGLE_HEIGHT;
import static com.sikoramarek.Common.Config.RECTANGLE_WIDTH;
import static com.sikoramarek.Common.SharedResources.*;
import static javafx.scene.input.KeyEvent.KEY_RELEASED;
import static javafx.scene.input.MouseEvent.MOUSE_RELEASED;

class InputHandler {

    void handleInput(InputEvent event) {
        if (event.getEventType().equals(MOUSE_RELEASED)) {
            MouseEvent mouseEvent = (MouseEvent) event;
            switch (mouseEvent.getButton()) {
                case PRIMARY:
                    if (((MouseEvent) event).getPickResult().getIntersectedNode() != null){
                        if( ((MouseEvent) event).getPickResult().getIntersectedNode().getClass().equals(BoxB.class)){
                            BoxB rectangle = (BoxB) mouseEvent.getPickResult().getIntersectedNode();
                            int gridXposition = (rectangle.getBoardX());
                            int gridYposition = (rectangle.getBoardY());
                            int[] position = new int[]{gridXposition, gridYposition}
                                    ;
                            synchronized (positions){
                                positions.add(position);
                            }

                        }else{
                            if( ((MouseEvent) event).getPickResult().getIntersectedNode().getClass().equals(Rectangle.class)){
                                Rectangle rectangle = (Rectangle) mouseEvent.getPickResult().getIntersectedNode();
                                int gridXposition = (int) (rectangle.getX() / RECTANGLE_WIDTH);
                                int gridYposition = (int) (rectangle.getY() / RECTANGLE_HEIGHT);
                                int[] position = new int[]{gridXposition, gridYposition};
                                synchronized (keyboardInput){
                                    positions.add(position);
                                }}
                        }
                    }
                    break;

                case SECONDARY:
                    synchronized (keyboardInput){
                        keyboardInput.add(KeyCode.P);
                    }
                    break;

                default:
                    break;
            }
        } else if (event.getEventType().equals(KEY_RELEASED)) {
            KeyEvent keyEvent = (KeyEvent) event;
            synchronized (keyboardInput){
                keyboardInput.add((keyEvent.getCode()));
            }

        }
    }

}
