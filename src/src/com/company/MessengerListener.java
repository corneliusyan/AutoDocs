package com.company;

public interface MessengerListener {
    void handleRemoteInsert(Char c, int count);

    void handleRemoteDelete(Char c, int count);

}
