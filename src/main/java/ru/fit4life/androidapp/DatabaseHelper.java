package ru.fit4life.androidapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;
    private static final  String DB_NAME = "f4l.db";
    private File dbFile;

    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase myDB;

    DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);
        this.context = context;
        createOrOpen ();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(Sys.TAG, "DataBase Created!");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean dbExists() {
        this.dbFile = new ContextWrapper(this.context).getDatabasePath(DB_NAME);
        return this.dbFile.exists();
    }

    private void createOrOpen () {

        if (dbExists()){
            Log.i(Sys.TAG, "Db already exists...");
        }
        else {
            Log.i(Sys.TAG, "Creating empty db...");

            //This method opens existing DB or Create new
            this.getReadableDatabase();
            //..and close
            this.close();

            Log.i(Sys.TAG, "Empty db created!");

            try {
                Log.i(Sys.TAG, "Copying db files...");
                copyDB();
            } catch (IOException e) {
                Log.e(Sys.TAG,"Can't copy DataBase: " + e.toString());
            }
        }
        myDB = SQLiteDatabase.openDatabase(dbFile.toString() , null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void copyDB () throws IOException {

        InputStream input = this.context.getAssets().open(DB_NAME);
        OutputStream output = new FileOutputStream(dbFile);

        byte [] buffer = new byte[1024];
        int length;
        Log.i(Sys.TAG, "input file" + input.toString());
        Log.i(Sys.TAG, "output file" + output.toString());
        while((length = input.read(buffer)) > 0)
            output.write(buffer,0,length);

        output.flush();
        output.close();
        input.close();
        Log.i(Sys.TAG, "Copy complete. File size: " + dbFile.getUsableSpace() );

    }
    @Override
    public synchronized void close() {

        if(myDB != null)
            myDB.close();

        super.close();
    }


    public Cursor getBodyParts () {
        String query = "SELECT * FROM BodyParts ORDER BY name";
        this.getReadableDatabase();

        Cursor c = myDB.rawQuery(query, null);
        if (c != null)
            c.moveToFirst();

        return c;
    }

    public Cursor getExercises () {
        String query = "SELECT * FROM exercises ORDER BY name";
        this.getReadableDatabase();

        Cursor c = myDB.rawQuery(query, null);
        if (c != null)
            c.moveToFirst();

        return c;
    }

    public Cursor getExercisesByBodyPartId (int id) {
        String query = "SELECT e.* FROM exercisebybodyparts as b INNER JOIN exercises as e ON e._id = b.exerciseid WHERE b.bodypartid = " + id + " ORDER BY e.name";
        this.getReadableDatabase();

        Cursor c = myDB.rawQuery(query, null);
        if (c != null)
            c.moveToFirst();

        return c;
    }

}