package com.sikoramarek.gameOfLife.client;

import com.sikoramarek.gameOfLife.common.Config;
import com.sikoramarek.gameOfLife.common.Logger;
import com.sikoramarek.gameOfLife.model.Dot;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Sender {

    private Socket socket;
    private ObjectOutputStream outputStream;

    Sender(){
        this.socket = socket;
    }

    void connect(Socket socket) throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    void sendConfig() {
        try {
            outputStream.writeObject(Config.gameState);
            outputStream.writeObject(ServerConfigB.getConfig());
        } catch (IOException e) {
            Logger.error("Cannot send", this);
        }
    }
    @Override
    public String toString(){
        return "Sender";
    }

    public void sendGameState() {
        try {
            outputStream.writeObject(Config.gameState);
        } catch (IOException e) {
            Logger.error(e.getMessage(), this);
        }
    }

    public void synchronize(Dot[][] board) {
        try {
            outputStream.writeObject(board);
        } catch (IOException e) {
            Logger.error(e.getMessage(), this);
        }
    }

    void sendMsg(String msg) {
        try {
            outputStream.writeObject(msg);
        } catch (IOException e) {
            Logger.error(e.getMessage(), this);
        }
    }
}
