package com.company;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Controller implements TextEditorListener, MessengerListener {

    private String host;
    private int port;

    private CRDT crdt;
    private String siteId;
    private TextEditor textEditor;
    private Messenger messenger;

    public Controller(String host, int port, List<String> peers) {
        siteId = "ws://" + host + ":" + port;
        crdt = new CRDT(siteId, this);
        textEditor = new TextEditor(400, 400);
        textEditor.setTextEditorListener(this);
        messenger = new Messenger(host, port, this, peers);
    }

    @Override
    public void onInsert(char value, int index) {
        Char c = crdt.localInsert(value, index);
        this.messenger.broadcastInsert(c, 0);
    }

    @Override
    public void onDelete(int index) {
        Char c = crdt.localDelete(index);
        this.messenger.broadcastDelete(c, 0);
    }

    public void insertToTextEditor(char value, int index) {
        textEditor.getTextArea().insert(String.valueOf(value), index);
        int curPos = textEditor.getCursorPos();
        if (index <= curPos) {
            textEditor.getTextArea().setCaretPosition(curPos + 1);
        }
    }

    public void deleteToTextEditor(int index) {
        textEditor.getTextArea().replaceRange("", index, index + 1);
        int curPos = textEditor.getCursorPos();
        if (index <= curPos) {
            textEditor.getTextArea().setCaretPosition(curPos - 1);
        }
    }

    @Override
    public void handleRemoteInsert(Char c, int count) {
        System.out.println("C O N T R O L L E R ===> handleRemoteInsert");
        this.crdt.remoteInsert(c);
    }

    @Override
    public void handleRemoteDelete(Char c, int count) {
        System.out.println("C O N T R O L L E R ===> handleRemoteDelete");
        this.crdt.remoteDelete(c);
    }


    public void start() {
        textEditor.show();
//        for (int i = 1; i <= 5; i++) {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                insertToTextEditor(Integer.toString(i), i);
//
//            } catch (InterruptedException ex) {
//
//            }
//        }

    }
}
