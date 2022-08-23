package com.example.todolist;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;

public class ZanyEditTextFactory {
    public enum ZanyEditTextType {
        ZANY_EDIT_TEXT,
        BIG_ZANY_EDIT_TEXT
    }

    public AppCompatEditText getZanyEditText(ZanyEditTextType type, Context context) {
        switch (type) {
            case ZANY_EDIT_TEXT:
                return new ZanyEditText(context);
            case BIG_ZANY_EDIT_TEXT:
                return new BigZanyEditText(context);
            default:
                return null;
        }
    }
}
