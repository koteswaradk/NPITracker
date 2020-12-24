package com.juniper.npitracker;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.model.NPIDetailsModel;
import com.juniper.npitracker.util.ConnectionDetector;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;
import com.juniper.npitracker.util.HttpHandler;
import com.juniper.npitracker.util.Util;

import org.json.JSONException;
import org.json.JSONObject;


public class NPIDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG=getClass().getSimpleName();
    private TextView npiName;
    CustomDialog customDialog;
    private View statusIndicator;
    private LinearLayout activityBack;
    //String ActiveRel_API="http://rbu.juniper.net/mobile_apps/Page1.json?npi=1537";
    NPIDetailsParser npiDetailsParser;
    private NPIDetailsModel npiDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npi_details);

        Bundle bundle = getIntent().getExtras();
        String NPIId = bundle.getString("NPI_Id");
        //String NPIId = "1537";
        npiDetailsParser = new NPIDetailsParser(NPIDetailsActivity.this, NPIId);
        npiDetails = npiDetailsParser.loadJSONFromAsset();
        // Get Data
            findViewById(R.id.iv).setOnClickListener(this);
            findViewById(R.id.tv).setOnClickListener(this);

        if(npiDetails != null){
            initializeViews();

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.sys_test));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.pr_summary));
//            tabLayout.addTab(tabLayout.newTab().setText(R.string.rli_status));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            final NPIDetailsPagerAdapter adapter = new NPIDetailsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), npiDetails);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
        else{

            CustomTast ct=new CustomTast(NPIDetailsActivity.this);
            ct.showCustomAlert("no data for given NPI",R.drawable.warning);

            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    public void initializeViews(){

        npiName = (TextView) findViewById(R.id.npi_name);
        npiName.setText(npiDetails.getNPIName());
        npiName.setTypeface(null, Typeface.BOLD);

        statusIndicator = findViewById(R.id.status_indicator);
        statusIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(npiDetails.getStatusReason()!= null && !npiDetails.getStatusReason().isEmpty()){
                    Log.i("REASON", npiDetails.getStatusReason());
                    openDialog(npiDetails.getStatusReason());
                }
            }
        });


        activityBack = (LinearLayout) findViewById(R.id.activity_back);
       /* activityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NPIDetailsActivity.super.onBackPressed();


            }
        });*/

        checkStatus();
    }

    /*
     * Used to check the status of a given NPI
     * Changes color of the status icon depending on the status
     */
    public void checkStatus() {

        String status = npiDetails.getStatus();
//        Log.i("STATUS_SELECTED", status);
        try {
            if (status.equalsIgnoreCase("green")) {
                statusIndicator.setBackgroundResource(R.drawable.npi_status_success);
            } else if (status.equalsIgnoreCase("red")) {
                statusIndicator.setBackgroundResource(R.drawable.npi_status_danger);
            } else if (status.equalsIgnoreCase("amber")) {
                statusIndicator.setBackgroundResource(R.drawable.npi_status_warning);
            }
           else{
                statusIndicator.setBackgroundResource(R.drawable.npi_status_default);
            }

        }catch (NullPointerException e){
            CustomTast ct=new CustomTast(NPIDetailsActivity.this);
            ct.showCustomAlert("no status",R.drawable.warning);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv:
                NPIDetailsActivity.super.onBackPressed();
                break;
            case R.id.iv:
                NPIDetailsActivity.super.onBackPressed();
                break;
        }
    }

    public void openDialog(String reason){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                NPIDetailsActivity.this);

        LayoutInflater inflater= LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.dialog_alert_scrollable, null);

        TextView textview=(TextView)view.findViewById(R.id.textmsg);
        textview.setText(reason);

        // set dialog message
        alertDialogBuilder
                .setTitle("NPI Status Reason")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
