package com.company;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;

public class TextEditor extends JFrame implements CaretListener, DocumentListener {
    private JFrame frame;
    private JTextArea textArea;
    private JPanel panel;

    private TextEditorListener controller;

    private int cursorPos;

    public TextEditor(int width, int height) {
        frame = new JFrame("Peer to Peer Text Editor with CRDT");
        textArea = new JTextArea();
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textArea.addCaretListener(this);
        textArea.getDocument().addDocumentListener(this);
        panel.add(textArea);
        frame.add(panel);
        frame.setSize(width, height);
    }

    public void setTextEditorListener(TextEditorListener controller) {
        this.controller = controller;
    }

    public void show() {
        frame.show();
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        cursorPos = e.getDot();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        char value = textArea.getText().charAt(cursorPos);
        controller.onInsert(value, cursorPos);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        controller.onDelete(cursorPos);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
