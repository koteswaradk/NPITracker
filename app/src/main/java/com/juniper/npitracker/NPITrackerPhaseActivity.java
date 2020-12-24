package com.juniper.npitracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.fragments.AllNPIFragment;
import com.juniper.npitracker.fragments.SelectedNPIFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class NPITrackerPhaseActivity extends AppCompatActivity implements View.OnClickListener,AllNPIFragment.OnFragmentInteractionListener,SelectedNPIFragment.OnFragmentInteractionListener {
    String TAG=getClass().getSimpleName();

    String lastsync;
    SwitchCompat toggleButton;
    TextView tt_lastsync_display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npitracker_phase);

        findViewById(R.id.bt_back).setOnClickListener(this);
        findViewById(R.id.npimenu).setOnClickListener(this);

         tt_lastsync_display=(TextView) findViewById(R.id.t_lastsync_display);

         toggleButton=(SwitchCompat) findViewById(R.id.toggleButton);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            android.support.v4.app.Fragment home = new AllNPIFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, home);
            ft.commit();
        }
       /* viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("P1 Exit"));
        tabLayout.addTab(tabLayout.newTab().setText("P2 Exit"));
        tabLayout.addTab(tabLayout.newTab().setText("P3 Exit"));
        tabLayout.addTab(tabLayout.newTab().setText("P4 Exit"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPagerAdapte pageAdapter = new ViewPagerAdapte(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });*/


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_npi_synch:
               // Toast.makeText(this,"sync",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_back:
                startActivity(new Intent(NPITrackerPhaseActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.npimenu:
                //Toast.makeText(this,"sync",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NPITrackerPhaseActivity.this,MainActivity.class));
                finish();
                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API));
                // Log.i(TAG+"String",apistring);
                 lastsync=  parseDbStringValues(apistring);
                /*if (lastsync!=null){

                }*/

            } while (cursor.moveToNext());
        }
        tt_lastsync_display.setText(lastsync);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    android.support.v4.app.Fragment home = new SelectedNPIFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, home);
                    ft.commit();

                }else {

                    android.support.v4.app.Fragment home = new AllNPIFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, home);
                    ft.commit();
                }


                /*if (b){
                    viewPagerselected = (ViewPager) findViewById(R.id.viewpagerselected);
                    tabLayoutselected = (TabLayout) findViewById(R.id.tabsselected);
                    tabLayoutselected.addTab(tabLayoutselected.newTab().setText("Selected NPI's"));
                    tabLayoutselected.setTabGravity(TabLayout.GRAVITY_FILL);

                    final SelctedPageAdapter pageAdapter = new SelctedPageAdapter(getSupportFragmentManager(), tabLayoutselected.getTabCount());
                    viewPagerselected.setAdapter(pageAdapter);
                    viewPagerselected.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutselected));
                    tabLayoutselected.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            viewPagerselected.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });


                }else {
                    viewPager = (ViewPager) findViewById(R.id.viewpager);
                    tabLayout = (TabLayout) findViewById(R.id.tabs);

                    tabLayout.addTab(tabLayout.newTab().setText("P1 Exit"));
                    tabLayout.addTab(tabLayout.newTab().setText("P2 Exit"));
                    tabLayout.addTab(tabLayout.newTab().setText("P3 Exit"));
                    tabLayout.addTab(tabLayout.newTab().setText("P4 Exit"));
                    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                    final ViewPagerAdapte pageAdapter = new ViewPagerAdapte(getSupportFragmentManager(), tabLayout.getTabCount());
                    viewPager.setAdapter(pageAdapter);
                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

                }*/
            }
        });
    }
    private String parseDbStringValues(String jsonStr) {
        String result=null;
        try {

            JSONObject obj = new JSONObject(jsonStr);

             result = obj.getString("sync_time");
           // Log.i(TAG+"synch time-----------",result);
                      /*  String result = obj.getString("ststus");
                        if (result.equalsIgnoreCase("ok")){*/
           // JSONArray m_jArry = obj.getJSONArray("npilistp1");

            // }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
