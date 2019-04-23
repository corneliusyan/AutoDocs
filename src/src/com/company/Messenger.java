package com.company;

import com.google.gson.Gson;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Messenger {
    private Controller controller;

    private List<String> peerList;
    private List<String> connectedPeerList;

    private String host;
    private int port;
    private String serverAddress;
    private String serverAddress;

    private ServerPeer serverPeer;
    private Gson gson;

    public Messenger(String host, int port, Controller controller) {
        this.host = host;
        this.port = port;
        this.serverAddress = "ws://" + this.host + ":" + this.port;
        this.controller = controller;
        this.peerList = new ArrayList<String>();
        this.connectedPeerList = new ArrayList<String>();
        this.gson = new Gson();

        this.init();
    }

    public Messenger(String host, int port, Controller controller, List<String> peerList) {
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
        while (this.connectedPeerList.size() < (this.peerList.size() - 1)) {
            for (String peer : this.peerList) {
                if (!this.connectedPeerList.contains(peer)) {
                    try {
                        ClientPeer peerNode = new ClientPeer(new URI(peer), peer);
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
            if (this.connectedPeerList.size() < (this.peerList.size() - 1)) {
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

    public void broadcastInsert(Char data, int count) {
        Operation op = new Operation(data, "insert", this.serverAddress, count);
        String payload = gson.toJson(op);
        this.serverPeer.broadcast(payload);
    }

    public void broadcastDelete(Char data, int count) {
        Operation op = new Operation(data, "delete", this.serverAddress, count);
        String payload = gson.toJson(op);
        this.serverPeer.broadcast(payload);
    }

    public void handleRemoteInsert(Char data) {
        System.out.println("[MESSENGER] HANDLE REMOTE INSERT");
    }

    public void handleRemoteDelete(Char data, String siteId) {
        System.out.println("[MESSENGER] HANDLE REMOTE DELETE");
    }
}
