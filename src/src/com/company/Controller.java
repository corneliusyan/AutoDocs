package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Controller implements TextEditorListener, MessengerListener {

    private String host;
    private int port;

    private CRDT crdt;
    private String siteId;
    private TextEditor textEditor;
    private Messenger messenger;

    private VersionVector versionVector;
    private List<Operation> deletionBuffer;

    public Controller(String host, int port, List<String> peers) {
        siteId = "ws://" + host + ":" + port;
        versionVector = new VersionVector(siteId);
        crdt = new CRDT(siteId, this);
        textEditor = new TextEditor(400, 400);
        textEditor.setTextEditorListener(this);
        messenger = new Messenger(host, port, this, peers);
        deletionBuffer = new ArrayList<Operation>();
    }

    @Override
    public void onInsert(char value, int index) {
        Char c = crdt.localInsert(value, index);
        this.messenger.broadcastInsert(c);
    }

    @Override
    public void onDelete(int index) {
        Char c = crdt.localDelete(index);
        this.messenger.broadcastDelete(c);
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
    public void handleRemoteInsert(Char c) {
        Version operationVersion = new Version(c.getSiteId(), c.getCounter());
        if (this.versionVector.hasBeenApplied(operationVersion)) return;
        this.crdt.remoteInsert(c);
        this.versionVector.update(operationVersion);
        this.processDeletionBuffer();
    }

    @Override
    public void handleRemoteDelete(Char c) {
        System.out.println("C O N T R O L L E R ===> handleRemoteDelete");
        Version operationVersion = new Version(c.getSiteId(), c.getCounter());
        if (this.versionVector.hasBeenApplied(operationVersion)) return;
        Operation operation = new Operation(c, "delete");
        this.deletionBuffer.add(operation);
        this.processDeletionBuffer();
    }

    public void processDeletionBuffer() {
        System.out.println(">>>>> processDeletionBuffer");
        for (Operation op : this.deletionBuffer) {
            if (this.hasInsertionBeenApplied(op)) {
                Version operationVersion = new Version(op.getData().getSiteId(), op.getData().getCounter());
                this.crdt.remoteDelete(op.getData());
                this.versionVector.update(operationVersion);
                this.deletionBuffer.remove(op);
            }
        }
    }

    public boolean hasInsertionBeenApplied(Operation op) {
        Version charVersion = new Version(op.getData().getSiteId(), op.getData().getCounter());
        return this.versionVector.hasBeenApplied(charVersion);
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

    public VersionVector getVersionVector() {
        return versionVector;
    }

    public void setVersionVector(VersionVector versionVector) {
        this.versionVector = versionVector;
    }
}
