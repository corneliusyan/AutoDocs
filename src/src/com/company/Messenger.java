package com.company;

import com.google.gson.Gson;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Messenger {
    private MessengerListener controller;

    private List<String> peerList;
    private List<String> connectedPeerList;

    private String host;
    private int port;
    private String serverAddress;

    private ServerPeer serverPeer;
    private Gson gson;

    public Messenger(String host, int port, MessengerListener controller) {
        this.host = host;
        this.port = port;
        this.serverAddress = "ws://" + this.host + ":" + this.port;
        this.controller = controller;
        this.peerList = new ArrayList<String>();
        this.peerList.add("ws://localhost:9000");
        this.peerList.add("ws://localhost:9001");
//        this.peerList.add("ws://localhost:9002");
        this.peerList.remove(this.serverAddress);
        this.connectedPeerList = new ArrayList<String>();
        this.gson = new Gson();

        this.init();
    }

    public Messenger(String host, int port, MessengerListener controller, List<String> peerList) {
        this.host = host;
        this.port = port;
        this.serverAddress = "ws://" + this.host + ":" + this.port;
        this.controller = controller;
        this.peerList = peerList;
        this.connectedPeerList = new ArrayList<String>();
        this.gson = new Gson();

        this.init();
    }

    public void init() {
        // Initialize server peer
        this.startServerPeer();

        // Initialize client peers
        this.startClientPeers();
    }

    public void startServerPeer() {
        this.serverPeer = new ServerPeer(new InetSocketAddress(this.host, this.port), this);
        this.serverPeer.start();
    }

    public void startClientPeers() {
        System.out.println("MESSENGER - startClientPeers");
        while (this.connectedPeerList.size() < (this.peerList.size())) {
            for (String peer : this.peerList) {
                if (!this.connectedPeerList.contains(peer)) {
                    try {
                        ClientPeer peerNode = new ClientPeer(new URI(peer), peer, this);
                        boolean isSucceeded = peerNode.connectBlocking();
                        if (isSucceeded) {
                            this.connectedPeerList.add(peer);
                        } else {
                            System.out.println("Failed connecting to " + peer);
                        }
                    }catch (Exception ex) {
                        System.out.println("error tapi gpp");
                    }
                }
            }
            if (this.connectedPeerList.size() < (this.peerList.size())) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("error pas sleep bre");
                }
            }
        }
    }

    public void addPeer(String peer) {
        this.peerList.add(peer);
        this.startClientPeers();
    }

    public void broadcastInsert(Char data) {
        Operation op = new Operation(data, "insert");
        String payload = gson.toJson(op);
        this.serverPeer.broadcast(payload);
    }

    public void broadcastDelete(Char data) {
        Operation op = new Operation(data, "delete");
        String payload = gson.toJson(op);
        this.serverPeer.broadcast(payload);
    }

    public void handleRemoteInsert(Char data) {
        this.controller.handleRemoteInsert(data);
    }

    public void handleRemoteDelete(Char data) {
        this.controller.handleRemoteDelete(data);
    }
}
