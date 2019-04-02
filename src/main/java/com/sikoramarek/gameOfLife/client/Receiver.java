package com.sikoramarek.gameOfLife.client;

import com.sikoramarek.gameOfLife.common.Logger;
import com.sikoramarek.gameOfLife.model.Dot;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class Receiver {

    private Socket socket;
    private ObjectInputStream inputStream;
    private BufferedInputStream bufferedInputStream;

    Receiver(){
    }

    void connect(Socket socket) throws IOException {
        this.socket = socket;
        bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        inputStream = new ObjectInputStream(bufferedInputStream);
    }

    boolean handleResponce(){
        try {
            while (bufferedInputStream.available() > 0){
                Object receivedObject = inputStream.readObject();
                if(receivedObject instanceof ServerConfig){
                    Logger.log("config", this);
                }
                else if(receivedObject instanceof Dot){
                    Logger.log("Board", this);
                }else if(receivedObject instanceof String){
                    Logger.log((String) receivedObject, "Message:");
                    if(receivedObject.equals("Ping")){
                        return true;
                    }
                }
                else{
                    Logger.error("Communication error", this);
                }
            }
        } catch (IOException e) {
            Logger.error("Communication error - "+e.getMessage(), this);
            try {
                bufferedInputStream.skip(bufferedInputStream.available());
            } catch (IOException e1) {
                Logger.error(e.getMessage(), this);
            }
            Logger.error("Try to sync", this);
        } catch (ClassNotFoundException e) {
            Logger.error("Message error", this);
        }
    return false;
    }

    @Override
    public String toString(){
        return "Client - receiver \n"+socket.getRemoteSocketAddress();
    }
}
