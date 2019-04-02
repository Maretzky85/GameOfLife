package com.sikoramarek.gameOfLife.client;

import com.sikoramarek.gameOfLife.common.Logger;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class Client implements Runnable{

	private static Client instance;
	private Socket serviceSocket;
	GameState gameState = GameState.CONFIG;

	private ObjectInputStream inputStream;
	private BufferedInputStream bufferedInputStream;
	private ObjectOutputStream outputStream;

	private boolean connected = false;

	private LinkedList<String> messagesToSend;

	private Client(){
		Logger.log("Client created", this);
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public static Client getClient(){
		if (instance == null){
			instance = new Client();
		}
		return instance;
	}

	public void connect(){
		if (connected){
			System.out.println(connected);
			Logger.error("Reconnecting", this);
			disconnect();
			new Thread(this).start();
		}else{
			new Thread(this).start();
		}
	}

	public void disconnect(){
		if(serviceSocket != null){
			try {
				inputStream.close();
				outputStream.close();
				serviceSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void createConnection(){
		int retry = 3;
		do {
			try {
				serviceSocket = new Socket("localhost", 65432);
				outputStream = new ObjectOutputStream(serviceSocket.getOutputStream());
				bufferedInputStream = new BufferedInputStream(serviceSocket.getInputStream());
				inputStream = new ObjectInputStream(bufferedInputStream);
				messagesToSend = new LinkedList<>();
				connected = true;
				Logger.log("Connected", this);
			} catch (IOException e) {
				retry -= 1;
				Logger.error("Connection problem -  "+e.getMessage(), this);
				connected = false;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					Logger.error("Interrupted", this);
				}
			}
		}while (!connected && retry > 0);
	}

	@Override
	public String toString(){
		return "Client";
	}

	@Override
	public void run() {
		//			serviceSocket = new Socket("217.182.73.80", 65432);
		createConnection();
		while(connected){
			while (messagesToSend.size() > 0){
				sendObjectToServer(messagesToSend.removeFirst());
			}
			handleResponse();
		}
		disconnect();
	}

	private void handleResponse() {
		try{
			while (bufferedInputStream.available() > 0){
				inputStream.readObject();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void sendObjectToServer(Object obj){
		try {
			outputStream.writeObject(obj);
		} catch (IOException e) {
			Logger.error(e.getMessage(), this);
		}
	}

}

