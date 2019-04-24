package com.company;

import java.util.ArrayList;
import java.util.List;

public class Version {
    private String siteId;
    private int counter;
    private List<Integer> exceptions;

    public Version(String siteId) {
        this.siteId = siteId;
        this.counter = 0;
        this.exceptions = new ArrayList<Integer>();
    }

    public Version(String siteId, int counter) {
        this.siteId = siteId;
        this.counter = counter;
        this.exceptions = new ArrayList<Integer>();
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public List<Integer> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<Integer> exceptions) {
        this.exceptions = exceptions;
    }

    public void update(Version version) {
        int incomingCounter = version.getCounter();
//        if (incomingCounter <= this.counter) {
        if (incomingCounter < this.counter) {
            int index = this.exceptions.indexOf(incomingCounter);
            if (this.exceptions.contains(index)) {
                this.exceptions.remove(index);
            } else {
                System.out.println("[Version->update] index not found at exception! (psst.. your code is probably incorrect, but if it works, it works)");
            }
        } else if (incomingCounter == this.counter + 1) {
            this.counter++;
        } else {
            for (int i = this.counter + 1; i < incomingCounter; i++) {
                this.exceptions.add(i);
            }
            this.counter = incomingCounter;
        }
    }
}
