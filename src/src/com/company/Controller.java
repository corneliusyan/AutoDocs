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
        System.out.println("[onDelete] START");
        System.out.println("[onDelete] call localDelete");
        Char c = crdt.localDelete(index);
        System.out.println("[onDelete] call broadCastDelete");
        this.messenger.broadcastDelete(c);
        System.out.println("[onDelete] FINISH");
    }

    public void insertToTextEditor(char value, int index) {
        textEditor.getTextArea().insert(String.valueOf(value), index);
        int curPos = textEditor.getCursorPos();
        if (index <= curPos) {
            textEditor.getTextArea().setCaretPosition(curPos + 1);
        }
    }

    public void deleteToTextEditor(int index) {
        System.out.println("[deleteToTextEditor] START");
        System.out.println("[deleteToTextEditor] >> delete in editor at index = " + index);
        textEditor.getTextArea().replaceRange("", index, index + 1);
        int curPos = textEditor.getCursorPos();
        if (index <= curPos) {
            System.out.println("[deleteToTextEditor] >> cursorPos = " + curPos);
            System.out.println("[deleteToTextEditor] >> setCaretPosition to " + (curPos - 1));
//            textEditor.getTextArea().setCaretPosition(curPos - 1);
            textEditor.getTextArea().setCaretPosition(0);
            System.out.println("[deleteToTextEditor] >> after update, cursorPos = " + textEditor.getCursorPos());
        }
        System.out.println("[deleteToTextEditor] FINISH");
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
        System.out.println("[handleRemoteDelete] START");
        System.out.println("[handleRemoteDelete] >> preparing operation");
        Version operationVersion = new Version(c.getSiteId(), c.getCounter());
        System.out.println("[handleRemoteDelete] >> check whether operation was already applied");
//        if (this.versionVector.hasBeenApplied(operationVersion)) return;
        Operation operation = new Operation(c, "delete");
        System.out.println("[handleRemoteDelete] >> add op to deletionBuffer");
        this.deletionBuffer.add(operation);
        System.out.println("[handleRemoteDelete] >> call processDeletionBuffer");
        this.processDeletionBuffer();
        System.out.println("[handleRemoteDelete] FINISH");
    }

    public void processDeletionBuffer() {
        System.out.println("[processDeletionBuffer] START");
        int huyu = 0;
        while (huyu < this.deletionBuffer.size()) {
            System.out.println("[processDeletionBuffer] >> check index = " + huyu);
            Operation op = this.deletionBuffer.get(huyu);
            System.out.println("[processDeletionBuffer] >> value = " + op.getData().getValue() + ", counter = " + op.getData().getCounter());
            System.out.println("[processDeletionBuffer] >> siteId = " + op.getData().getSiteId());
            if (this.hasInsertionBeenApplied(op)) {
                Version operationVersion = new Version(op.getData().getSiteId(), op.getData().getCounter());
                System.out.println("[processDeletionBuffer] >> currentCount = " + this.versionVector.getVersionFromVector(operationVersion).getCounter());
                this.crdt.remoteDelete(op.getData());
                this.versionVector.update(operationVersion);
                this.deletionBuffer.remove(op);
            } else {
                System.out.println("[processDeletionBuffer] >>>> insertion hasn't been applied yet!");
                huyu++;
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
