package com.company;

import java.util.List;
import java.util.ArrayList;

public class CRDT {
    private int base;
    private int boundary;
    private String siteId;
    private List<Char> struct;

    public CRDT(String siteId) {
        this.siteId = siteId;
        this.base = 32;
        this.boundary = 10;
        this.struct = new ArrayList<Char>();
    }

    public CRDT(String siteId, int base, int boundary) {
        this.siteId = siteId;
        this.base = base;
        this.boundary = boundary;
        this.struct = new ArrayList<Char>();
    }

    public void localInsert(char value, int index) {
        System.out.println("\n\nLocal Insert");
        System.out.println("Val = " + value + ", Index = " + index);
        Char curChar = this.generateChar(value, index);
        this.struct.add(index, curChar);
    }

    public void localDelete(int index) {
        this.struct.remove(index);
    }

    public Char generateChar(char value, int index) {
        List<Identifier> posBefore;
        if (((index - 1) >= 0) && ((index - 1) < this.struct.size())) {
            posBefore = this.struct.get(index - 1).getPosition();
        } else {
            posBefore = new ArrayList<Identifier>();
        }
        System.out.println("> posBefore = " + posBefore);

        List<Identifier> posAfter;
        if (((index + 1) >= 0) && ((index + 1) < this.struct.size())) {
            posAfter = this.struct.get(index + 1).getPosition();
        } else {
            posAfter = new ArrayList<Identifier>();
        }
        System.out.println("> posAfter = " + posAfter);

        List<Identifier> newPos = new ArrayList<Identifier>();
        this.generatePosBetween(posBefore, posAfter, newPos, 0);
        System.out.println("> newPos   = " + newPos);
        return new Char(value, newPos, this.siteId);
    }

    public void generatePosBetween(List<Identifier> posBefore,
                                   List<Identifier> posAfter,
                                   List<Identifier> newPos,
                                   int level) {
        System.out.println(">> level = " + level);
        int base = (int) Math.pow(2, level) * this.base;
        char boundaryStrategy = this.retrieveStrategy(level);

        Identifier idBefore;
        if (posBefore.size() > 0) {
            idBefore = posBefore.get(0);
        } else {
            idBefore = new Identifier(0, this.siteId);
        }
        Identifier idAfter;
        if (posAfter.size() > 0) {
            idAfter = posAfter.get(0);
        } else {
            idAfter = new Identifier(base, this.siteId);
        }

        if ((idAfter.getDigit() - idBefore.getDigit()) > 1) {
            int newDigit = this.generateIdBetween(idBefore.getDigit(),
                    idAfter.getDigit(),
                    boundaryStrategy);
            newPos.add(new Identifier(newDigit, this.siteId));
        } else if ((idAfter.getDigit() - idBefore.getDigit()) == 1) {
            newPos.add(idBefore);
            if (posBefore.size() > 0) {
                posBefore = posBefore.subList(1, posBefore.size());
            }
            this.generatePosBetween(posBefore,
                    new ArrayList<Identifier>(),
                    newPos,
                    level + 1);
        } else if (idBefore.getDigit() == idAfter.getDigit()) {
            int comSiteId = idBefore.getSiteId().compareTo(idAfter.getSiteId());
            if (comSiteId < 0) {
                newPos.add(idBefore);
                if (posBefore.size() > 0) {
                    posBefore = posBefore.subList(1, posBefore.size());
                }
                this.generatePosBetween(posBefore,
                        new ArrayList<Identifier>(),
                        newPos,
                        level + 1);
            } else if (comSiteId == 0) {
                newPos.add(idBefore);
                if (posBefore.size() > 0) {
                    posBefore = posBefore.subList(1, posBefore.size());
                }
                if (posAfter.size() > 0) {
                    posAfter = posAfter.subList(1, posAfter.size());
                }
                this.generatePosBetween(posBefore,
                        posAfter,
                        newPos,
                        level + 1);
            } else {
                throw new Error("u no gud at coding");
            }
        }
    }

    public char retrieveStrategy(int level) {
        if (Math.round(Math.random()) == 1) {
            return '+';
        } else {
            return '-';
        }
    }

    public int generateIdBetween(int min, int max, char boundaryStrategy) {
        if ((max - min) < this.boundary) {
            min = min + 1;
        } else {
            if (boundaryStrategy == '-') {
                min = max - this.boundary;
            } else {
                min = min + 1;
                max = min + this.boundary;
            }
        }
        int idBetween = (int) Math.floor(Math.random() * (max - min)) + min;
        System.out.println(">> idBetween = " + idBetween);
        return idBetween;
    }

    public void printString() {
        for (Char c : this.struct) {
            System.out.print(c.getValue());
        }
        System.out.println("");
    }
}
