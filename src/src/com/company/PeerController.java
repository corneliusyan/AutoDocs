package com.company;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PeerController {
    private List<String> peerList;
    private List<String> connectedPeerList;

    private ServerPeer serverPeer;
    private String serverAddress;

    public PeerController() {
        this.peerList = new ArrayList<String>();
        this.connectedPeerList = new ArrayList<String>();
    }

    public ServerPeer getServerPeer() {
        return serverPeer;
    }

    public void setServerPeer(ServerPeer serverPeer) {
        this.serverPeer = serverPeer;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void startServerPeer(String host, int port) {
        this.setServerPeer(new ServerPeer(new InetSocketAddress(host, port)));
        this.serverPeer.start();
    }

    public List<String> getPeerList() {
        return peerList;
    }

    public void setPeerList(List<String> peerList) {
        this.peerList = peerList;
    }

    public List<String> getConnectedPeerList() {
        return connectedPeerList;
    }

    public void setConnectedPeerList(List<String> connectedPeerList) {
        this.connectedPeerList = connectedPeerList;
    }

    public void startClientPeers() {
        while (this.connectedPeerList.size() < 2) {
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
            if (this.connectedPeerList.size() < 2) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("error pas sleep bre");
//                    e.printStackTrace();
                }
            }
        }
    }

    public void sayHello() {
        this.serverPeer.broadcast("HELLO FROM " + this.serverAddress);
    }

    public List<String> generatePeers(int port) {
        List<String> peers = new ArrayList<String>();
        peers.add("ws://localhost:9000");
        peers.add("ws://localhost:9001");
        peers.add("ws://localhost:9002");
        peers.remove("ws://localhost:" + port);
        return peers;
    }

    public static void main(String[] args) {
        PeerController controller = new PeerController();

        // Start server peer
        String host = "localhost";
        int port = 9002;
        controller.setServerAddress("ws://" + host + ":" + port);
        controller.startServerPeer(host, port);

        // Start client peer
        System.out.println("Connecting to peers...");
        List<String> peers = new ArrayList<String>();
        controller.setPeerList(controller.generatePeers(port));

        controller.startClientPeers();
        // Test p2p connection
        System.out.println("Say Hello!!!!!!");
        controller.sayHello();
    }
}
