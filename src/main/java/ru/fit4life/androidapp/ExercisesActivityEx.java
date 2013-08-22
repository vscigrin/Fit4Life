package ru.fit4life.androidapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;

public class ExercisesActivityEx extends Activity {

    private DatabaseHelper dh;
    private MyExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisesex);

        dh = new DatabaseHelper(this);

        fillData();
    }

    @Override
    protected void onDestroy() {
        Log.i(Sys.TAG, "Trying to destroy....");
        dh.close();
        super.onDestroy();
    }

    private void fillData() {
        dh = new DatabaseHelper(this);
        Cursor parentCursor = dh.getBodyParts();
        startManagingCursor(parentCursor);

        // The desired columns to be bound
        String[] parentColumns = new String[]{
                Sys.C_ICON,
                Sys.C_NAME
        };

        // the XML defined views which the data will be bound to
        int[] parentTo = new int[]{
                R.id.imageViewExerciseGroupIcon,
                android.R.id.text1
        };

        ExpandableListView elv = (ExpandableListView) findViewById(R.id.exerciseGroupListEx);

        this.adapter = new MyExpandableListAdapter(parentCursor, this,
                R.layout.exercise_group_list_row,
                android.R.layout.simple_list_item_1,
                parentColumns,
                parentTo,
                new String[]{Sys.C_NAME},
                new int[]{android.R.id.text1}
        );

        this.adapter.setViewBinder(new MyExpandableListAdapter.ViewBinder() {
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

        elv.setAdapter(this.adapter);
    }


    public class MyExpandableListAdapter extends SimpleCursorTreeAdapter {

        public MyExpandableListAdapter(Cursor cursor, Context context, int groupLayout,
                                       int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom,
                                       int[] childrenTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo,
                    childLayout, childrenFrom, childrenTo);

        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            //Cursor childCursor = mDbHelper.fetchChildren(groupCursor.getString(groupCursor.getColumnIndex("id_room"));
            Cursor exCursor = dh.getExercisesByBodyPartId(groupCursor.getInt(groupCursor.getColumnIndex(Sys.C_ID)));
            startManagingCursor(exCursor);
            return exCursor;
        }
    }
}
