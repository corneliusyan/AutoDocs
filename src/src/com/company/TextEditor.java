package com.company;

import javafx.scene.input.KeyCode;

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
        frame = new JFrame("Peer to Peer Collaborative Text Editor with CRDT");
        textArea = new JTextArea();
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textArea.addCaretListener(this);
        textArea.addKeyListener(this);
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
        System.out.println("[caretUpdate] curPos = " + e.getDot());
        cursorPos = e.getDot();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            controller.onDelete(this.getCursorPos());
        } else if (e.getKeyCode() != KeyEvent.VK_UP &&
                e.getKeyCode() != KeyEvent.VK_DOWN &&
                e.getKeyCode() != KeyEvent.VK_LEFT &&
                e.getKeyCode() != KeyEvent.VK_RIGHT &&
                e.getKeyCode() != KeyEvent.VK_TAB &&
                e.getKeyCode() != KeyEvent.VK_ALT &&
                e.getKeyCode() != KeyEvent.VK_SHIFT &&
                e.getKeyCode() != KeyEvent.VK_CANCEL &&
                e.getKeyCode() != KeyEvent.VK_CONTROL &&
                e.getKeyCode() != KeyEvent.VK_CAPS_LOCK &&
                e.getKeyCode() != KeyEvent.VK_ESCAPE &&
                e.getKeyCode() != KeyEvent.VK_END &&
                e.getKeyCode() != KeyEvent.VK_HOME
        ) {
            char value = '\0';
            value = e.getKeyChar();
            controller.onInsert(value, this.getCursorPos());
        }
        System.out.println("keyPressed");
    }

    public void keyReleased(KeyEvent e) {
        // pass
    }

    public void keyTyped(KeyEvent e) {
        // pass
    }


}
