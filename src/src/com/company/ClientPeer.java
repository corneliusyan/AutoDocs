package com.company;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ClientPeer extends WebSocketClient {

    private Gson gson = new Gson();
    private String serverAddress;
    private Messenger messenger;

    public ClientPeer(URI serverURI, String serverAddress, Messenger messenger) {
        super(serverURI);
        this.serverAddress = serverAddress;
        this.messenger = messenger;
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        System.out.println("New PEER connection opened");
//        ControllerNode.getVersionVector().addSiteId(serverAddress, 0);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);

//        ControllerNode.getClientPeerNodes().remove(serverAddress);
//        ControllerNode.getVersionVector().removeSiteId(serverAddress);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("CLIENT received message: " + message);
        Operation op = this.gson.fromJson(message, Operation.class);
        if (op.getType().equals("insert")) {
            System.out.println("onMessage --> INSERT");
            this.messenger.handleRemoteInsert(op.getData());
        } else if (op.getType().equals("delete")) {
            System.out.println("onMessage --> DELETE");
            this.messenger.handleRemoteDelete(op.getData());
        }
    }

    @Override
    public void onError(Exception ex) {
//        System.err.println("an error occurred:" + ex);
        System.err.println("an error occurred:");
    }


}