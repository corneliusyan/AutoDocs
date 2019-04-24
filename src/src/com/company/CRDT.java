package com.company;

import java.util.List;
import java.util.ArrayList;

public class CRDT {
    private int base;
    private int boundary;
    private String siteId;
    private List<Char> struct;
    private Controller controller;
    private VersionVector versionVector;

    public CRDT(String siteId, Controller controller) {
        this.siteId = siteId;
        this.base = 32;
        this.boundary = 10;
        this.struct = new ArrayList<Char>();
        this.controller = controller;
        this.versionVector = controller.getVersionVector();
    }

    public CRDT(String siteId, int base, int boundary, Controller controller) {
        this.siteId = siteId;
        this.base = base;
        this.boundary = boundary;
        this.struct = new ArrayList<Char>();
        this.controller = controller;
        this.versionVector = controller.getVersionVector();
    }

    public void printLocation(Char c) {
        System.out.print("[printLocation] loc = [");
        List<Identifier> location = c.getPosition();
        for (int i = 0; i < location.size(); i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(location.get(i).getDigit());
        }
        System.out.println("]");
    }

    public Char localInsert(char value, int index) {
        this.versionVector.increment();
        Char curChar = this.generateChar(value, index);
//        System.out.println("[CRDT->localInsert] val = " + value + ", index = " + index);
//        this.printLocation(curChar);
        this.struct.add(index, curChar);
        printString();
        return curChar;
    }

    public void remoteInsert(Char c) {
//        int index = this.findInsertPosition(c);
        int index = this.findInsertIndex(c);
//        System.out.println("[Remote Insert] val = " + c.getValue() + ", index = " + index);
//        this.printLocation(c);
        this.struct.add(index, c);
        this.controller.insertToTextEditor(c.getValue(), index);
    }

    public int findInsertPosition(Char c) {
        int minIndex = 0;
        int maxIndex = this.struct.size() - 1;
        if (this.struct.size() == 0 || c.compareTo(this.struct.get(0)) <= 0) {
//            System.out.println("[CRDT->findInsertPosition] index = 0");
            return 0;
        }
        Char lastChar = this.struct.get(maxIndex);
        if (c.compareTo(lastChar) > 0) {
//            System.out.println("[CRDT->findInsertPosition] index = " + this.struct.size());
            return this.struct.size();
        }
        for (int i = 1; i < (this.struct.size() - 1); i++) {
            Char leftChar = this.struct.get(i - 1);
            Char rightChar = this.struct.get(i + 1);
            if (c.compareTo(rightChar) == 0) {
//                System.out.println("[CRDT->findInsertPosition] index = " + i + " (equal to right)");
                return i;
            }
            if ((c.compareTo(leftChar) > 0) && (c.compareTo(rightChar) < 0)) {
//                System.out.println("[CRDT->findInsertPosition] index = " + i + " (in between)");
                return i;
            }
        }
        // u stoopid
//        System.out.println("[CRDT->findInsertPosition] ERROR ! ! !");
        return 0; // hrsna ga ke sini dongg
    }

    public int findInsertIndex(Char val) {
        int left = 0;
        int right = this.struct.size() - 1;

        if (this.struct.size() == 0 || val.compareTo(this.struct.get(left)) < 1) {
            return left;
        } else if (val.compareTo(this.struct.get(right)) > 0) {
            return this.struct.size();
        }

        while ((left + 1) < right) {
            int mid = (int) Math.floor(left + (right - left) / 2);
//            System.out.println("Check Index, i = " + mid);
//            System.out.print(">> mid position -> ");
//            this.printLocation(this.struct.get(mid));
            int compareNum = val.compareTo(this.struct.get(mid));

            if (compareNum == 0) {
//                System.out.println("Mid match!");
                return mid;
            } else if (compareNum > 0) {
                left = mid;
            } else {
                right = mid;
            }
        }

        if (val.compareTo(this.struct.get(left)) == 0) {
//            System.out.print(">> FINAL LEFT position -> ");
//            this.printLocation(this.struct.get(left));
            return left;
        } else {
//            System.out.print(">> FINAL RIGHT position -> ");
//            this.printLocation(this.struct.get(right));
            return right;
        }
    }

    public Char localDelete(int index) {
        System.out.println("[localDelete] START");
        System.out.println("[localDelete] >> increment vector");
        this.versionVector.increment();
        Char c = this.struct.get(index - 1);
//        System.out.println("[localDelete] >> set counter to deleted char");
//        c.setCounter(this.versionVector.getLocalVersion().getCounter());
        System.out.println("[localDelete] >> remove char from local struct");
        this.struct.remove(index - 1); // karena dari editor, makanya index - 1
        printString();
        System.out.println("[localDelete] FINISH");
        return c;
    }

    public void remoteDelete(Char c) {
        System.out.println("[remoteDelete] START");
        int index = this.findPosition(c);
        System.out.println("[remoteDelete] >> delete index = " + index);
        if (index == -1) {
            System.out.println("no matching index found");
            return;
        }
        System.out.println("[remoteDelete] >> remove char at index");
        this.struct.remove(index);
        System.out.println("[remoteDelete] >> delete on text editor");
        this.controller.deleteToTextEditor(index);
        printString();
    }

    public int findPosition(Char c) {
        for (int i = 0; i < this.struct.size(); i++) {
            if (c.compareTo(this.struct.get(i)) == 0) {
                return i;
            }
        }
        return -1;
    }

    public Char generateChar(char value, int index) {
        List<Identifier> posBefore;
        if (((index - 1) >= 0) && ((index - 1) < this.struct.size())) {
            posBefore = this.struct.get(index - 1).getPosition();
        } else {
            posBefore = new ArrayList<Identifier>();
        }

        List<Identifier> posAfter;
        if (((index) >= 0) && ((index) < this.struct.size())) {
            posAfter = this.struct.get(index).getPosition();
        } else {
            posAfter = new ArrayList<Identifier>();
        }

        List<Identifier> newPos = new ArrayList<Identifier>();
        this.generatePosBetween(posBefore, posAfter, newPos, 0);
        return new Char(value, newPos, this.siteId, this.versionVector.getLocalVersion().getCounter());
    }

    public void generatePosBetween(List<Identifier> posBefore,
                                   List<Identifier> posAfter,
                                   List<Identifier> newPos,
                                   int level) {
        System.out.println("[generatePosBetween] START");

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

//        System.out.println("[generatePosBetween] idBefore = " + idBefore.getDigit() + ", idAfter = " + idAfter.getDigit());

        if ((idAfter.getDigit() - idBefore.getDigit()) > 1) {
            int newDigit = this.generateIdBetween(idBefore.getDigit(),
                    idAfter.getDigit(),
                    boundaryStrategy);
//            System.out.println("[generatePosBetween] newDigit = " + newDigit);
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
        return idBetween;
    }

    public void printString() {
        for (Char c : this.struct) {
            System.out.print(c.getValue());
        }
        System.out.println("");
    }
}
