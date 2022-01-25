package com.app.swagliv;

import com.app.swagliv.constant.AppConstant;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketChatApplication {

    private Socket socket;

    private static SocketChatApplication socketChatApplication = new SocketChatApplication();

    public static SocketChatApplication getSocketChatApplication() {
        return socketChatApplication;
    }

    public Socket getSocket() {
        return socket;
    }

    public SocketChatApplication() {
        try {
            socket = IO.socket(AppConstant.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


}
