package com.sikoramarek.Model;

import com.sikoramarek.Common.Logger;
import com.sikoramarek.Common.SharedResources;
import javafx.scene.input.KeyCode;

public class RuleManager {

    public static int[] ruleToLive = new int[]{2, 3};
    public static int[] ruleToGetAlive = new int[]{3};

    public void checkForInput(){
        for (KeyCode key : SharedResources.getKeyboardInput()
                ) {
        switch (key) {
            case DIGIT1:
                Logger.log("Conway`s Game of Life rules", this);
                ruleToLive = new int[]{2, 3};
                ruleToGetAlive = new int[]{3};
                break;
            case DIGIT2:
                Logger.log("HighLife 23/36 rule", this);
                ruleToLive = new int[]{2, 3};
                ruleToGetAlive = new int[]{3, 6};
                break;
            case DIGIT3:
                Logger.log("345/345 rule", this);
                ruleToGetAlive = new int[]{3, 4, 5};
                ruleToGetAlive = new int[]{3, 4, 5};
                break;
            case DIGIT4:
                Logger.log("Motion 234/368 rule", this);
                ruleToLive = new int[]{2, 4, 5};
                ruleToGetAlive = new int[]{3, 6, 8};
                break;
            case DIGIT5:
                Logger.log("Replicator 1357/1357 rule", this);
                ruleToLive = new int[]{1, 3, 5, 7};
                ruleToGetAlive = new int[]{1, 3, 5, 7};
                break;
            case DIGIT6:
                Logger.log("Labyrinth 12345/3 rule", this);
                ruleToLive = new int[]{1, 2, 3, 4, 5};
                ruleToGetAlive = new int[]{3};
                break;
            case DIGIT7:
                Logger.log("traycloth /234 rule", this);
                ruleToLive = new int[]{};
                ruleToGetAlive = new int[]{2, 3, 4};
                break;
            case DIGIT8:
                Logger.log("Leafs 012345678/3 rule", this);
                ruleToLive = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
                ruleToGetAlive = new int[]{3};
                break;
            case DIGIT9:
                Logger.log("Wolfram - 7(e) 012345678/1 rule", this);
                ruleToLive = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
                ruleToGetAlive = new int[]{1};
                break;
            default:
                break;
            }
        }

    }

    @Override
    public String toString(){
        return "Rule";
    }

}
