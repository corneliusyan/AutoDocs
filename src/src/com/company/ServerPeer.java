package com.company;


import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ServerPeer extends WebSocketServer {
    private Messenger messenger;
    private String webSocketAddress;
    private Gson gson = new Gson();

    public ServerPeer(InetSocketAddress address, Messenger messenger) {
        super(address);
        this.webSocketAddress = "ws://" + address.getHostName() + ":" + address.getPort();
        this.messenger = messenger;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake clientHandshake) {
        System.out.println("New connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
//        Operation op = this.gson.fromJson(message, Operation.class);
//        if (op.getType().equals("insert")) {
//            System.out.println("onMessage --> INSERT");
//            this.messenger.handleRemoteInsert(op.getData());
//        } else if (op.getType().equals("delete")) {
//            System.out.println("onMessage --> DELETE");
//            this.messenger.handleRemoteDelete(op.getData());
//        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("An error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("Node server started successfully");
    }

    public String getWebSocketAddress() {
        return webSocketAddress;
    }

//    public void broadcastInsertion(CharInfo data, int operationCount) {
//        Operation operation = new Operation(data, webSocketAddress, true, operationCount);
//
//        String message = gson.toJson(operation);
//        System.out.println(message);
//        broadcast(message);
//    }
//
//    public void broadcastDeletion(CharInfo data, int operationCount) {
//        Operation operation = new Operation(data, webSocketAddress, false, operationCount);
//
//        String message = gson.toJson(operation);
//        System.out.println(message);
//        broadcast(message);
//    }

}
