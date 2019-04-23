package com.company;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TextEditor extends JFrame implements CaretListener, DocumentListener, KeyListener {
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
        addKeyListener(this);
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

    public JTextArea getTextArea() {
        return textArea;
    }

    public int getCursorPos() {
        return cursorPos;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        cursorPos = e.getDot();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        System.out.println("TEXT EDITOR HUYU - insertUpdate");
//        char value = textArea.getText().charAt(cursorPos);
        char value = '\0';
        try {
            value = e.getDocument().getText(e.getOffset(), 1).charAt(0);
        } catch (BadLocationException ex) {

        }

        controller.onInsert(value, e.getOffset());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        controller.onDelete(cursorPos);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed");
    }

    public void keyReleased(KeyEvent e) {
        System.out.println("==========================");
        System.out.println("keyReleased");
        System.out.println("==========================");

    }

    public void keyTyped(KeyEvent e) {
        System.out.println("==========================");
        System.out.println("keyTyped");
        System.out.println("==========================");
    }


}
