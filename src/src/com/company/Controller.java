package com.company;

import java.util.concurrent.TimeUnit;

public class Controller implements TextEditorListener {

    private CRDT crdt;
    private String siteId;
    private TextEditor textEditor;
    private Messenger messenger;

    public Controller() {
        siteId = "ci papi";
        crdt = new CRDT(siteId);
        textEditor = new TextEditor(400, 400);
        textEditor.setTextEditorListener(this);
        messenger = new Messenger();
    }

    @Override
    public void onInsert(char value, int index) {
        crdt.localInsert(value, index);
    }

    @Override
    public void onDelete(int index) {
        crdt.localDelete(index);
    }

    public void insertToTextEditor(String value, int index) {
        textEditor.getTextArea().insert(value, index);
        int curPos = textEditor.getCursorPos();
        textEditor.getTextArea().setCaretPosition(curPos + 1);
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
