package com.company;

public class Operation {
    private Char data;
    private String type;
//    private String siteId;
//    private int count;

    public Operation(Char data, String type) {
        this.data = data;
        this.type = type;
//        this.siteId = siteId;
//        this.count = count;
    }

    public Char getData() {
        return data;
    }

    public void setData(Char data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public String getSiteId() {
//        return siteId;
//    }
//
//    public void setSiteId(String siteId) {
//        this.siteId = siteId;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
}
