package com.juniper.npitracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.juniper.npitracker.activereleases.ActiveRelSummery;
import com.juniper.npitracker.adapter.MainGridAdapter;
import com.juniper.npitracker.rlistatus.RLIViewActivity;


public class MainActivity extends AppCompatActivity {

    private GridView grid;

    int[] titles = {
            R.string.npi_view,
            R.string.rli_status,
            R.string.active_releases,
            R.string.regression_tests

    } ;
    int[] imageId = {
            R.drawable.ic_list,
            R.drawable.ic_media,
            R.drawable.ic_outline,
            R.drawable.ic_registry
    };

    int[][] colors = {
            {R.color.orange, R.color.darkOrange},
            {R.color.blue, R.color.darkBlue},
            {R.color.green, R.color.darkGreen},
            {R.color.red, R.color.darkRed}
    };

    Class[] menuActivities = {
            NPITrackerPhaseActivity.class,
            RLIViewActivity.class,
            ActiveRelSummery.class,
            null
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = getContentWidth(metrics);
        int height = getContentHeight(metrics);


        MainGridAdapter adapter = new MainGridAdapter(MainActivity.this, titles, imageId, colors, width, height);
        grid=(GridView)findViewById(R.id.gridview);
        grid.setAdapter(adapter);
//        grid.setOnTouchListener(new AdapterView.OnTouchListener() {
//
//            @Override
//            public void onTouch(AdapterView<?> parent, View view,
//                                    int position, long id) {
//               // Toast.makeText(MainActivity.this, "You Clicked at " +titles[+ position], Toast.LENGTH_SHORT).show();
//               RelativeLayout layoutImage = (RelativeLayout) view.findViewById(R.id.layout_image);
//               layoutImage.setBackgroundDrawable( getResources().getDrawable(colors[position][1]) );
//
//
//            }
//        });

//        grid.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent me) {
//                int action = me.getActionMasked();  // MotionEvent types such as ACTION_UP, ACTION_DOWN
//
//
//                float currentXPosition = me.getX();
//                float currentYPosition = me.getY();
//                int position = grid.pointToPosition((int) currentXPosition, (int) currentYPosition);
//
//                // Access text in the cell, or the object itself
////                String s = (String) grid.getItemAtPosition(position);
//
//                View wantedView = grid.getChildAt(position);
//                if(action == me.ACTION_DOWN){
//
//                    RelativeLayout layoutImage = (RelativeLayout) wantedView.findViewById(R.id.layout_image);
//                    layoutImage.setBackgroundDrawable( getResources().getDrawable(colors[position][1]) );
//                }
//                else{
//                    RelativeLayout layoutImage = (RelativeLayout) wantedView.findViewById(R.id.layout_image);
//                    layoutImage.setBackgroundDrawable( getResources().getDrawable(colors[position][0]) );
//                }
////                    if (menuActivities[position]==null){
////                       open();
////                    }else {
////                        startActivity(new Intent(MainActivity.this,menuActivities[position]));
////                        finish();
////                    }
//
//
//
//                return true;
//            };
//        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RelativeLayout layoutImage;
                switch(position){
                    case 0:
                        layoutImage = (RelativeLayout) view.findViewById(R.id.layout_image);
                        layoutImage.setBackgroundDrawable( getResources().getDrawable(colors[position][1]) );

                        startActivity(new Intent(MainActivity.this,menuActivities[position]));
                        finish();

                        break;
                    case 1:
                        layoutImage = (RelativeLayout) view.findViewById(R.id.layout_image);
                        layoutImage.setBackgroundDrawable( getResources().getDrawable(colors[position][1]) );

                        startActivity(new Intent(MainActivity.this,menuActivities[position]));
                        finish();
                        break;
                    case 2:
                        layoutImage = (RelativeLayout) view.findViewById(R.id.layout_image);
                        layoutImage.setBackgroundDrawable( getResources().getDrawable(colors[position][1]) );

                        startActivity(new Intent(MainActivity.this,menuActivities[position]));
                        finish();
                        break;
                    case 3:
                        layoutImage = (RelativeLayout) view.findViewById(R.id.layout_image);
                        layoutImage.setBackgroundDrawable( getResources().getDrawable(colors[position][1]) );
                        open(layoutImage, position);
                        break;
                }
            }
        });

    }

    public int getContentWidth(DisplayMetrics metrics){
        return metrics.widthPixels;
    }

    public int getContentHeight(DisplayMetrics metrics){
        int screenHeight = metrics.heightPixels;
        int statusBarHeight = 0;
        int actionBarHeight = 0;

        int resource = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resource > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resource);
        }

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        return (screenHeight - statusBarHeight);
    }

    /**
     * Opens an alert dialog
     */
    public void open(final RelativeLayout layoutImage, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set dialog message
        alertDialogBuilder
                .setMessage("Functionality will be added soon.")
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {

                    RelativeLayout layout = layoutImage;
                    int pos = position;
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                        layout.setBackgroundDrawable( getResources().getDrawable(colors[pos][0]) );
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_close_translate,R.anim.activity_open_scale);
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_translate);
    }
}
