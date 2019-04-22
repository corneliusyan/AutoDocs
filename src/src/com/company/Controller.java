package com.company;

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


    public void start() {
        textEditor.show();
    }
}
