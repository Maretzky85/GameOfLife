package com.sikoramarek.gameOfLife.client;

import com.sikoramarek.gameOfLife.model.Dot;

import java.io.*;
import java.net.Socket;

public class Client {

	Socket client;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;

	public Client() throws IOException {

		 client = new Socket("127.0.0.1", 65432);
		 inputStream = new ObjectInputStream(client.getInputStream());
		 outputStream = new ObjectOutputStream(client.getOutputStream());

	}

	public void sendData(Dot[][] dotBoard) throws IOException {
//		int[][] data = new int[dotBoard[0].length][dotBoard.length];
//		for(int i = 0; i<data.length; i++){
//			for (int j=0; j>data[0].length; j++){
//				if (!dotBoard[i][j]){
//					data[i][j] = 0;
//				}else{
//					data[i][j] = 1;
//				}
//
//			}
//		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		outputStream.writeInt(dotBoard.length);
		outputStream.writeObject(dotBoard);
//		for (int[] line:data
//		     ) {
//
//			for (int cell:line
//			     ) {
//				outputStream.writeInt(cell);
//			}
//
//		}
		outputStream.flush();
	}
}

