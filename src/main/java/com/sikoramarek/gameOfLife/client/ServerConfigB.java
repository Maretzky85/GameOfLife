package com.sikoramarek.gameOfLife.client;

import com.sikoramarek.gameOfLife.common.Config;

public class ServerConfigB {

    private ServerConfigB(){}

    private static ServerConfig serverConfig = new ServerConfig();

    public static ServerConfig getConfig(){
        serverConfig.xSize = Config.X_SIZE;
        serverConfig.ySize = Config.Y_SIZE;
        serverConfig.framerate = Config.getFrameRate();
        return serverConfig;
    }

}
