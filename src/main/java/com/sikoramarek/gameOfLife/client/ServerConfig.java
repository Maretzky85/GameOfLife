package com.sikoramarek.gameOfLife.client;

import java.io.Serializable;

public class ServerConfig implements Serializable {

    public int xSize;
    public int ySize;
    public int framerate;

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().equals(this.getClass())){
            return false;
        }
        ServerConfig serverConfig = (ServerConfig) obj;
        if(serverConfig.xSize != this.xSize){
            return false;
        }
        if(serverConfig.ySize != this.ySize){
            return false;
        }
        if(serverConfig.framerate != this.framerate){
            return false;
        }
        return true;
    }
}
