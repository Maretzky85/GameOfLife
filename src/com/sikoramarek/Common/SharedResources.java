package com.sikoramarek.Common;

import javafx.scene.input.KeyCode;

import java.util.LinkedList;
import java.util.List;

public class SharedResources {

    private static final List<KeyCode> keyboardInput = new LinkedList<>();

    public static final List<int[]> positions = new LinkedList<>();

    public static void addKeyboardInput(KeyCode key){
        if(!keyboardInput.contains(key)){
            keyboardInput.add(key);
        }
    }

    public static List<KeyCode> getKeyboardInput(){
        return keyboardInput;
    }

    public static void clearKeyboardInput(){
        keyboardInput.clear();
    }
}
