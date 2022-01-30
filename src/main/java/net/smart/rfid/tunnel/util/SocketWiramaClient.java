package net.smart.rfid.tunnel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWiramaClient {
	
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, long port1) throws Exception {
        int port = (int)port1;
    	clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws Exception {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
    }

}
