package com.sikoramarek.gameOfLife.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	ServerSocket server;
	DataInputStream inputStream;
	DataOutputStream outputStream;

	public Server() throws IOException {

		server = new ServerSocket(65432);
		Socket serviceSocket = server.accept();

		inputStream = new DataInputStream(serviceSocket.getInputStream());
		outputStream = new DataOutputStream(serviceSocket.getOutputStream());

	}
}
