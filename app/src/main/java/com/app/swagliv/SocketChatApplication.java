package com.app.swagliv;

import com.app.swagliv.constant.AppConstant;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketChatApplication {

    private static Socket socket;

    private static SocketChatApplication socketChatApplication = new SocketChatApplication();

    private SocketChatApplication() {
        try {
            socket = IO.socket(AppConstant.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized Socket doConnect() {
        if (!socket.connected()) {
            socket = socket.connect();
        }
        return socket;
    }

}
