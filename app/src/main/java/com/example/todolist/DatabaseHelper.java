package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

/**
 * This is a template for SQLite CRUD purposes
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Projects";
    public static final String TABLE_NAME = "Todo";
    public static final String ID = "ID";
    public static final String CHECKED = "Checked";
    public static final String TASK = "Task";

    /**
     * NOTE: The database is not actually created and/or opened until one of getWritableDatabase() or getReadableDatabase() is called.
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        // For initial database & table creation, I used the line below:
        // SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CHECKED + " INTEGER," +
                TASK + " TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String checked, String task, boolean param) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            if (param) {
                /* With parameters */
                String query = "INSERT OR REPLACE INTO " + TABLE_NAME + " (" + CHECKED + ", " + TASK + ") VALUES (?,?)";
                SQLiteStatement statement = sqLiteDatabase.compileStatement(query);
                statement.bindLong(1, Long.parseLong(checked));
                statement.bindString(2, task);
                return statement.executeInsert() != -1;
            } else {
                /* Without parameters */
                ContentValues contentValues = new ContentValues();
                contentValues.put(CHECKED, checked);
                contentValues.put(TASK, task);
                return sqLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE) != -1;
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Demo (at MainActivity):<br><br>
     * <code>
     * // dh is the DatabaseHelper instance <br>
     * Cursor resultset = dh.<b>getRows()</b>;<br>
     * if(resultset.getCount()==0) {<br>
     * &nbsp;&nbsp;&nbsp;// inform user<br>
     * &nbsp;&nbsp;&nbsp;AlertDialog.Builder builder = new AlertDialog.Builder(this);<br>
     * &nbsp;&nbsp;&nbsp;builder.setCancelable(true);<br>
     * &nbsp;&nbsp;&nbsp;builder.setTitle("Title");<br>
     * `&nbsp;&nbsp;builder.setMessage("Message");<br>
     * &nbsp;&nbsp;&nbsp;builder.show();<br>
     * <p>
     * &nbsp;&nbsp;&nbsp;return;<br>
     * }<br>
     * StringBuffer buffer = new StringBuffer();<br><br>
     * while (resultset.moveToNext()) {<br>
     * &nbsp;&nbsp;&nbsp;// starts at 0 <br>
     * &nbsp;&nbsp;&nbsp;buffer.append(res.getString(1)+"\n");<br>
     * &nbsp;&nbsp;&nbsp;buffer.append(res.getString(2)+"\n");<br>
     * }<br><br>
     * <p>
     * // EXAMPLE: Show data from DB to <b>DIALOG</b> if desired.<br>
     * AlertDialog.Builder builder2 = new AlertDialog.Builder(this);<br>
     * builder2.setCancelable(true);<br>
     * builder2.setTitle("Title");<br>
     * builder2.setMessage(buffer.toString());<br>
     * builder2.show();<br>
     * </code>
     */
    public Cursor getRows() {
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean update(String id, String checked, String task, boolean param, Context context) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            if (param) {
                String query = "UPDATE " + TABLE_NAME + " SET " + CHECKED + "=?, " + TASK + "=? WHERE " + ID + "=?";
                SQLiteStatement statement = sqLiteDatabase.compileStatement(query);
                statement.bindLong(1, Long.parseLong(checked));
                statement.bindString(2, task);
                statement.bindLong(3, Long.parseLong(id));
                if (failedChanges(context, statement)) return false;
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, id);
                contentValues.put(CHECKED, checked);
                contentValues.put(TASK, task);
                if (sqLiteDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id}) < 1) {
                    // inform user of no such rows to update
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Title");
                    builder.setMessage("There exists no such rows to update.");
                    builder.show();
                    return false;
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(String id, boolean param, Context context) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            if (param) {
                String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID + " =?";
                SQLiteStatement statement = sqLiteDatabase.compileStatement(query);
                statement.bindLong(1, Long.parseLong(id));
                if (failedChanges(context, statement)) return false;
            } else {
                if (sqLiteDatabase.delete(TABLE_NAME, "ID = ?", new String[]{id}) < 1) {
                    // inform user of no such rows to update
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Title");
                    builder.setMessage("There exists no such rows to delete.");
                    builder.show();
                    return false;
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean failedChanges(Context context, SQLiteStatement statement) {
        if (statement.executeUpdateDelete() < 1) {
            // inform user of no such rows to update
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle("Title");
            builder.setMessage("There exists no such rows to update.");
            builder.show();
            return true;
        }
        return false;
    }
}
