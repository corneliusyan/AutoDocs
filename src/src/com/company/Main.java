package com.company;

public class Main {
    public static void main(String[] args) {
        System.out.println("Program Start");

        String host = "localhost";
        int port = 9000;

        Controller controller = new Controller(host, port);
        controller.start();

    }
}
