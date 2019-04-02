package com.sikoramarek.gameOfLife.client;

import com.sikoramarek.gameOfLife.model.Dot;

import java.io.*;
import java.net.Socket;

public class Client {

	Socket client;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;

	public Client() throws IOException {

	 client = new Socket("217.182.73.80", 65432);
//	 client = new Socket("localhost", 65432);
	 outputStream  = new ObjectOutputStream(client.getOutputStream());
//	 inputStream = new ObjectInputStream(client.getInputStream());

	}

	public void sendData(Dot[][] dotBoard) throws IOException{
		outputStream.writeObject(dotBoard);
		outputStream.flush();
//		System.out.println(inputStream.readObject());
	}
}

