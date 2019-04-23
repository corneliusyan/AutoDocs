package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("================================");
        System.out.print("Input host = ");
        String host = in.nextLine();
        System.out.print("Input port = ");
        int port = in.nextInt();

        System.out.println("--------------------------------");
        List<String> peers = new ArrayList<String>();
        System.out.print("Enter number of peers = ");
        int nPeers = in.nextInt();
        String buf = in.nextLine(); // consume \n after nextInt
        for (int i = 1; i <= nPeers; i++) {
            System.out.print("> Peer " + i + " URI = ");
            String peer = in.nextLine();
            peers.add(peer);
        }

        Controller controller = new Controller(host, port, peers);
        controller.start();
    }
}
