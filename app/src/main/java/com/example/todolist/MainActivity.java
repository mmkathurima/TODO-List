package com.example.todolist;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //new DatabaseHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRows(true, new TableLayout(MainActivity.this));
            }
        });
    }

    public void createRows(Boolean newList, TableLayout tableLayout) {
        TableLayout task = findViewById(R.id.subtask);
        TableRow tableRow = new TableRow(MainActivity.this), row = new TableRow(MainActivity.this);
        CheckBox bigCheck = new CheckBox(MainActivity.this), checkBox = new CheckBox(MainActivity.this);
        BigZanyEditText edt = new BigZanyEditText(MainActivity.this);
        ZanyEditText qty = new ZanyEditText(MainActivity.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        ColorStateList oldColors = qty.getTextColors();

        lp.setMarginStart(50);
        checkBox.setLayoutParams(lp);
        qty.setLayoutParams(lp);
        qty.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        qty.setImeOptions(EditorInfo.IME_ACTION_GO);
        //qty.setImeActionLabel("Some Label", 666);
        qty.setMaxLines(1);
        qty.requestFocus();

        qty.setOnEditorActionListener(new ZanyEditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    TableLayout layout = (TableLayout) v.getParent().getParent();
                    //layout.setBackgroundColor(Color.GRAY);
                    createRows(false, layout);
                    return true;
                }
                return false;
            }
        });

        qty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    Log.e("Row count", String.format("%d", tableLayout.getChildCount()));
                    if (Objects.requireNonNull(qty.getText()).toString().trim().isEmpty() && tableLayout.getChildCount() != 2) {
                        tableLayout.removeView((TableRow) qty.getParent());
                        return true;
                    }
                }
                return false;
            }
        });

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TableLayout layout = (TableLayout) buttonView.getParent().getParent();
                for (int i = 0; i < layout.getChildCount(); i++) {
                    TableRow row1 = ((TableRow) layout.getChildAt(i));
                    for (int j = 0; j < row1.getChildCount(); j++) {
                        View child = row1.getChildAt(j);
                        if (child instanceof CheckBox) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((CheckBox) child).setChecked(isChecked);
                                }
                            }, 500);
                        } else if (child instanceof AppCompatEditText) {
                            if (isChecked) {
                                ((AppCompatEditText) child).setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                            } else {
                                ((AppCompatEditText) child).setTextColor(oldColors);
                            }
                        }
                    }
                }
                ((CheckBox) ((TableRow) tableLayout.getChildAt(1)).getChildAt(0)).setChecked(isChecked);
            }
        };


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                TableRow tr = (TableRow) checkBox.getParent();
                TableLayout tlayout = (TableLayout) checkBox.getParent().getParent();
                if (isChecked) {
                    qty.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                    tlayout.removeView(tr);
                    tlayout.addView(tr, tlayout.getChildCount());
                } else {
                    qty.setTextColor(oldColors);
                    tlayout.removeView(tr);
                    tlayout.addView(tr, 1);
                }
                int checkedCount = 0, checkCount = 0;
                for (int i = 0; i < tlayout.getChildCount(); i++) {
                    for (int j = 0; j < ((TableRow) tlayout.getChildAt(i)).getChildCount(); j++) {
                        View child = ((TableRow) tlayout.getChildAt(i)).getChildAt(j);
                        if (child instanceof CheckBox) {
                            checkCount++;
                            if (((CheckBox) child).isChecked()) {
                                checkedCount++;
                            }
                        }
                    }
                }
                if (isChecked) {
                    if (checkCount - 1 == checkedCount) {
                        bigCheck.setChecked(true);
                    }
                } else {
                    if (checkCount - 1 == checkedCount) {
                        bigCheck.setOnCheckedChangeListener(null);
                        bigCheck.setChecked(false);
                        bigCheck.setOnCheckedChangeListener(listener);
                    }
                }
                ((CheckBox) ((TableRow) tlayout.getChildAt(0)).getChildAt(0)).setChecked(checkCount - 1 == checkedCount);
                Log.e(String.format("Checkboxes %d", checkCount), String.format("Checkboxes %d", checkedCount));
            }
        });

        if (newList) {
            TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

            edt.setTypeface(null, Typeface.BOLD);
            lp2.setMarginStart(-20);
            bigCheck.setLayoutParams(lp2);
            //bigCheck.setVisibility(View.INVISIBLE);
            edt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    Log.e("COUNT", String.format("%d", task.getChildCount()));
                    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL && task.getChildCount() != 2) {
                        task.removeView(tableLayout);
                        return true;
                    }
                    return false;
                }
            });
            bigCheck.setOnCheckedChangeListener(listener);

            tableRow.addView(bigCheck);
            tableRow.addView(edt);
            tableLayout.addView(tableRow);

            TableRow.LayoutParams trlp = new TableRow.LayoutParams
                    (TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            tableLayout.setLayoutParams(trlp);
            task.addView(tableLayout);
        }

        row.addView(checkBox);
        row.addView(qty);
        tableLayout.addView(row);
    }

}