package ru.fit4life.androidapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ExercisesActivity extends Activity {

    private SimpleCursorAdapter dataAdapter;
    private DatabaseHelper dh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exercises);

        dh = new DatabaseHelper(this);


        //Generate ListView from SQLite Database
        displayListView();
    }

    //@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayListView() {

        Cursor cursor = dh.getExercises();

        // The desired columns to be bound
        String[] columns = new String[]{
                Sys.C_ICON,
                Sys.C_NAME
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.imageViewExerciseGroupIcon,
                android.R.id.text1 //R.id.textViewExerciseGroupName
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.exercise_group_list_row,
                cursor,
                columns,
                to,
                0);


        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.imageViewExerciseGroupIcon) {
                    ImageView IV = (ImageView) view;
                    String iconName = cursor.getString(columnIndex);
                    Log.i(Sys.TAG, String.format("Icon name:%s", iconName));
                    //Log.i(Sys.TAG, String.format("Icon is empty:%s", iconName.isEmpty()));
                    if (iconName != null) {
                        int resID = getApplicationContext().getResources().getIdentifier(iconName, "drawable", getApplicationContext().getPackageName());
                        Log.i(Sys.TAG, String.format("Getted resid: %s", resID));
                        IV.setImageDrawable(getApplicationContext().getResources().getDrawable(resID));
//                        Log.e(Sys.TAG, String.format("Error fetching icon:%s%s", e.getMessage().toString(), e.getStackTrace().toString()));

                        return true;
                    }
                }
                return false;
            }
        });

        ListView listView = (ListView) findViewById(R.id.exerciseGroupList);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String exerciseName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                Toast.makeText(getApplicationContext(), exerciseName, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void navigateBack(View view) {
        finish();
    }

    public void navigateHome(View view) {

        Intent intent = new Intent(ExercisesActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        Log.i(Sys.TAG, "Trying to destroy....");
        dh.close();
        super.onDestroy();
    }

}
