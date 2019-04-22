package com.company;

public interface TextEditorListener {
    void onInsert(char value, int index);

    void onDelete(int index);
}
