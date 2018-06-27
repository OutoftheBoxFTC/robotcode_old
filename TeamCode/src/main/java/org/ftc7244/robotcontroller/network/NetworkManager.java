package org.ftc7244.robotcontroller.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManager implements Runnable {
    ServerSocket serverSocket;
    Socket socket;
    InputStream inputStream;
    Thread serverStart;
    public NetworkManager(){
        serverStart = new Thread(this);
    }

    public void readSocket(byte[] buffer) throws IOException {
        if(inputStream != null) {
            inputStream.read(buffer);
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(8080);
            socket = serverSocket.accept();
            inputStream = socket.getInputStream();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
