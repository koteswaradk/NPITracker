package com.juniper.npitracker.rlistatus;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.juniper.npitracker.R;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RLIListDetailActivity extends AppCompatActivity implements View.OnClickListener{

    TextView t_lastsync,t_release_set_t1,rlilist_layout_no_data_display;
    ListView rlilist;
    CustomDialog customDialog;
    StringRequest releaselistStringRequest;
    RLIListDetailAdapter adapter;
    SearchView rlisearchview;
    String lastsync;
    Spinner pfnfpinner;
    LinearLayout layout_back_button;
    private ArrayList<RLILIstDetailsModel> rlilistdetailsModel = new ArrayList<RLILIstDetailsModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlilist_detail);
        t_lastsync=(TextView)findViewById(R.id.t_lastsync_display);
        rlilist=(ListView) findViewById(R.id.rlilist);
        t_release_set_t1=(TextView)findViewById(R.id.release_set_t1);
        rlisearchview=(SearchView) findViewById(R.id.rlisearchview);
        pfnfpinner=(Spinner)findViewById(R.id.pfnfpinner);
        findViewById(R.id.bt_back).setOnClickListener(this);
        findViewById(R.id.npimenu).setOnClickListener(this);
        layout_back_button=(LinearLayout) findViewById(R.id.layout_back_button);
        layout_back_button.setOnClickListener(this);
        rlilist_layout_no_data_display=(TextView)findViewById(R.id.rlilist_layout_no_data_display);

        setupSearchView();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)t_release_set_t1.setText("("+bundle.getString("releasename")+")");

        rlilistdetailsModel.clear();

        Cursor cursor = getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_RLI_STATUS_URI, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_RLI_STATUS));
                // Log.i(TAG+"String",apistring);
                rlilistdetailsModel=  parseDbStringValues(apistring);
                //  Log.i("string",""+apistring);
                t_lastsync.setText(lastsync);
                adapter=new RLIListDetailAdapter(RLIListDetailActivity.this,rlilistdetailsModel);
                rlilist.setAdapter(adapter);

            } while (cursor.moveToNext());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_back_button:
                startActivity(new Intent(RLIListDetailActivity.this,RLIViewActivity.class));
                finish();
                break;
            case R.id.bt_back:
                startActivity(new Intent(RLIListDetailActivity.this,RLIViewActivity.class));
                finish();
                break;
            case R.id.npimenu:
                startActivity(new Intent(RLIListDetailActivity.this,RLIViewActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        pfnfpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                switch (position){
                    case 0:
                        rlilistdetailsModel.clear();

                        Cursor cursor = getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_RLI_STATUS_URI, null, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            do{
                                String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_RLI_STATUS));
                                // Log.i(TAG+"String",apistring);
                                rlilistdetailsModel=  parseDbStringValues(apistring);
                                //  Log.i("string",""+apistring);

                                if (rlilistdetailsModel.isEmpty())
                                {
                                    rlilist_layout_no_data_display.setVisibility(View.VISIBLE);
                                    rlilist.setVisibility(View.GONE);
                                    rlisearchview.setVisibility(View.GONE);
                                }else {
                                    rlilist_layout_no_data_display.setVisibility(View.GONE);
                                    rlilist.setVisibility(View.VISIBLE);
                                    rlisearchview.setVisibility(View.VISIBLE);
                                }

                                t_lastsync.setText(lastsync);
                                adapter=new RLIListDetailAdapter(RLIListDetailActivity.this,rlilistdetailsModel);
                                rlilist.setAdapter(adapter);

                            } while (cursor.moveToNext());
                        }
                        // Toast.makeText(RLIListDetailActivity.this,item,Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        rlilistdetailsModel.clear();
                        Cursor cursorprocessfol = getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_RLI_STATUS_URI, null, null, null, null, null);
                        if (cursorprocessfol.moveToFirst()) {
                            do{
                                String apistring= cursorprocessfol.getString(cursorprocessfol.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_RLI_STATUS));
                                // Log.i(TAG+"String",apistring);
                                rlilistdetailsModel=  parseDbStringValues(apistring);


                                ArrayList<RLILIstDetailsModel> rliProcessFollowed = new ArrayList<RLILIstDetailsModel>();
                                for(RLILIstDetailsModel rli: rlilistdetailsModel){

                                    if(rli.getProcessfollowed().equalsIgnoreCase("yes")){
                                        rliProcessFollowed.add(rli);
                                    }
                                }
                                    if (rliProcessFollowed.isEmpty())
                                    {
                                        rlilist_layout_no_data_display.setVisibility(View.VISIBLE);
                                        rlilist.setVisibility(View.GONE);
                                        rlisearchview.setVisibility(View.GONE);
                                    }else {
                                        rlilist_layout_no_data_display.setVisibility(View.GONE);
                                        rlilist.setVisibility(View.VISIBLE);
                                        rlisearchview.setVisibility(View.VISIBLE);
                                    }

                                //  Log.i("string",""+apistring);
                                t_lastsync.setText(lastsync);
                                adapter=new RLIListDetailAdapter(RLIListDetailActivity.this,rliProcessFollowed);
                                rlilist.setAdapter(adapter);

                            } while (cursorprocessfol.moveToNext());
                        }
                        //Toast.makeText(RLIListDetailActivity.this,item,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        rlilistdetailsModel.clear();
                        Cursor cursorprocessnotfol = getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_RLI_STATUS_URI, null, null, null, null, null);
                        if (cursorprocessnotfol.moveToFirst()) {
                            do{
                                String apistring= cursorprocessnotfol.getString(cursorprocessnotfol.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_RLI_STATUS));
                                // Log.i(TAG+"String",apistring);
                                rlilistdetailsModel=  parseDbStringValues(apistring);
                                //  Log.i("string",""+apistring);

                                ArrayList<RLILIstDetailsModel> rliProcessNotFollowed = new ArrayList<RLILIstDetailsModel>();
                                for(RLILIstDetailsModel rli: rlilistdetailsModel){

                                    if(rli.getProcessfollowed().equalsIgnoreCase("no")){
                                        rliProcessNotFollowed.add(rli);
                                    }
                                }
                                if (rliProcessNotFollowed.isEmpty())
                                {
                                    rlilist_layout_no_data_display.setVisibility(View.VISIBLE);
                                    rlilist.setVisibility(View.GONE);
                                    rlisearchview.setVisibility(View.GONE);
                                }else {
                                    rlilist_layout_no_data_display.setVisibility(View.GONE);
                                    rlilist.setVisibility(View.VISIBLE);
                                    rlisearchview.setVisibility(View.VISIBLE);
                                }
                                t_lastsync.setText(lastsync);
                                adapter=new RLIListDetailAdapter(RLIListDetailActivity.this,rliProcessNotFollowed);
                                rlilist.setAdapter(adapter);

                            } while (cursorprocessnotfol.moveToNext());
                        }
                       // Toast.makeText(RLIListDetailActivity.this,item,Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rli_pro_nonpro, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* android.support.v4.app.Fragment fragment = null;
        String title = getString(R.string.app_name);*/
       /* int id = item.getItemId();
        boolean birthSort=true;
        switch (id){
            case R.id.action_pf:
               // fragment = new ChartFragment();
              //  Toast.makeText(DebugAnalysisMainActivity.this,"ChartFragment",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_pnf:
               *//* fragment = new ReportFragment();
                Toast.makeText(DebugAnalysisMainActivity.this,"ReportFragment",Toast.LENGTH_SHORT).show();*//*
                break;
            case R.id.action_allnpi:

               *//* Toast.makeText(DebugAnalysisMainActivity.this,"Action synch",Toast.LENGTH_SHORT).show();*//*

                break;

        }*/

       /* if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }*/
        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_report) {
            return true;
        }*/


        return super.onOptionsItemSelected(item);
    }
    private void setupSearchView()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        rlisearchview.setIconifiedByDefault(false);
        rlisearchview.setQueryRefinementEnabled(true);
        rlisearchview.setSubmitButtonEnabled(true);
        rlisearchview.setQueryHint("Search Here");
        rlisearchview.setIconifiedByDefault(false);

        rlisearchview.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                //return false;
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);
//                mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);

                if (TextUtils.isEmpty(newText)) {
                    rlilist.clearTextFilter();
                    rlilistdetailsModel.clear();
                    rlisearchview.clearFocus();
                    Cursor cursor = getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_RLI_STATUS_URI, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do{
                            String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_RLI_STATUS));
                            // Log.i(TAG+"String",apistring);
                            rlilistdetailsModel=  parseDbStringValues(apistring);
                            //  Log.i("string",""+apistring);
                            t_lastsync.setText(lastsync);
                            adapter=new RLIListDetailAdapter(RLIListDetailActivity.this,rlilistdetailsModel);
                            rlilist.setAdapter(adapter);

                        } while (cursor.moveToNext());
                    }
                } else {
                    RLIListDetailAdapter ca = (RLIListDetailAdapter)rlilist.getAdapter();
                    ca.getFilter().filter(newText);
                    //following line was causing the ugly popup window.
                    //m_listView.setFilterText(newText);
                }
                /*if (TextUtils.isEmpty(s)) {
                    listview.clearTextFilter();
                } else {
                    listview.setFilterText(s);
                }*/

                return true;
            }

        });

    }



    private ArrayList<RLILIstDetailsModel> parseDbStringValues(String jsonStr){

        if (jsonStr!=null){
            try {

                JSONObject obj = new JSONObject(jsonStr);

                Log.i("JSON OBJ", jsonStr);

                lastsync = obj.getString("sync_time");
                //  Log.i(TAG+"synch time-----------",result);

                JSONArray m_jArry = obj.getJSONArray("data");

                for (int i = 0; i < m_jArry.length(); i++) {
                    JSONObject jo_inside = m_jArry.getJSONObject(i);

                    RLILIstDetailsModel model = new RLILIstDetailsModel();

                    model.setRliid(jo_inside.getString("rli_id"));
                    //Log.i(TAG,model.getNpiname());
                    model.setSynopsis(jo_inside.getString("synopsis"));
                    // Log.i(TAG,model.getSwlead());
                    model.setProcessfollowed(jo_inside.getString("process_followed"));
                    //  Log.i(TAG,model.getPlm());
                    model.setTestExecutedPercentage(jo_inside.getString("tests_executed_percentage"));
                    model.setTestPassedPercentage(jo_inside.getString("tests_passed_percentage"));
                    model.setTestScriptsPercentage(jo_inside.getString("tests_script_percentage"));
                    model.setNpiprogram(jo_inside.getString("npi_program"));
                    // Log.i(TAG,model.getTestlead());
                    rlilistdetailsModel.add(model);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            CustomTast ct=new CustomTast(RLIListDetailActivity.this);
            ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        }


        return rlilistdetailsModel;
    }
}
