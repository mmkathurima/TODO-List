package com.example.todolist;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.TableLayout;

import java.util.Objects;

public class BigZanyEditText extends androidx.appcompat.widget.AppCompatEditText {
    public BigZanyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BigZanyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BigZanyEditText(Context context) {
        super(context);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new BigZanyInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    private class BigZanyInputConnection extends InputConnectionWrapper {
        public BigZanyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                //ZanyEditText.this.setRandomBackgroundColor();
                Log.e("BIG",
                        String.valueOf(((TableLayout) BigZanyEditText.this.getParent().getParent().getParent()).getChildCount()));
                if (Objects.requireNonNull(BigZanyEditText.this.getText()).toString().trim().isEmpty() &&
                        ((TableLayout) BigZanyEditText.this.getParent().getParent().getParent()).getChildCount() == 2) {
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
