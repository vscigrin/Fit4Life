package ru.fit4life.androidapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ExercisesDBAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_ICON = "icon";
    public static final String KEY_NAME = "name";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE = "BodyParts";

    private final Context mCtx;

    public ExercisesDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public ExercisesDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getReadableDatabase();

        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor fetchExercisesByName(String inputText) throws SQLException {
        Log.w(Sys.TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {
            mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID,
                    KEY_ICON, KEY_NAME},
                    null, null, null, null, null);

        } else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[]{KEY_ROWID,
                    KEY_ICON, KEY_NAME},
                    KEY_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllExercises() {
        Cursor mCursor;
        mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID,
                KEY_ICON, KEY_NAME},
                null, null, null, null, KEY_NAME);

        if (mCursor != null) {

            mCursor.moveToFirst();
        }

        return mCursor;
    }
}
