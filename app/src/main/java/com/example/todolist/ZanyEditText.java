package com.example.todolist;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.TableLayout;

import java.util.Objects;
import java.util.Random;

public class ZanyEditText extends androidx.appcompat.widget.AppCompatEditText {

    private final Random r = new Random();

    public ZanyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ZanyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZanyEditText(Context context) {
        super(context);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new ZanyInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class ZanyInputConnection extends InputConnectionWrapper {

        public ZanyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                //ZanyEditText.this.setRandomBackgroundColor();
                Log.e(String.valueOf(((TableLayout) ZanyEditText.this.getParent().getParent()).getChildCount()),
                        String.valueOf(((TableLayout) ZanyEditText.this.getParent().getParent().getParent()).getChildCount()));
                if (Objects.requireNonNull(ZanyEditText.this.getText()).toString().trim().isEmpty() &&
                        ((TableLayout) ZanyEditText.this.getParent().getParent()).getChildCount() == 2) {
                    return false;
                }
                // Un-comment if you wish to cancel the backspace:
                // return false;
            }
            return super.sendKeyEvent(event);
        }


        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
            if (beforeLength == 1 && afterLength == 0) {
                // backspace
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }

    }

}