package com.company;

public class Controller {

    private CRDT crdt;
    private String siteId;

    public Controller() {
        siteId = "ci papi";
        crdt = new CRDT(siteId);
    }

    public void start() {
        for (int i = 0; i < 100; i++) {
            crdt.localInsert('a', 0);
        }
    }
}
