package com.company;

import java.util.List;

public class Char implements Comparable<Char> {
    private char value;
    private List<Identifier> position;
    private String siteId;
    // private int counter;

    public Char(char value, List<Identifier> position, String siteId) {
        this.value = value;
        this.position = position;
        this.siteId = siteId;
    }

    public char getValue() {
        return this.value;
    }

    public List<Identifier> getPosition() {
        return this.position;
    }

    public String getSiteId() {
        return this.siteId;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public void setPosition(List<Identifier> position) {
        this.position = position;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public int compareTo(Char other) {
        List<Identifier> thisPosition = this.position;
        List<Identifier> otherPosition = other.position;

        int thisPosSize = thisPosition.size();
        int otherPosSize = otherPosition.size();

        int minPosSize = Math.min(thisPosSize, otherPosSize);
        for (int i = 0; i < minPosSize; i++) {
            Identifier thisIndex = thisPosition.get(i);
            Identifier otherIndex = otherPosition.get(i);
            if (thisIndex.compareTo(otherIndex) != 0) {
                return thisIndex.compareTo(otherIndex);
            }
        }

        if (thisPosSize < otherPosSize) {
            return -1;
        } else if (thisPosSize > otherPosSize) {
            return 1;
        } else {
            return 0;
        }
    }
}

