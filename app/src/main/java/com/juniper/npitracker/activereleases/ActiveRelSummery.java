package com.juniper.npitracker.activereleases;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.juniper.npitracker.JdiAppController;
import com.juniper.npitracker.MainActivity;
import com.juniper.npitracker.R;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.rlistatus.RLIViewActivity;
import com.juniper.npitracker.rlistatus.ReleaseListAdapter;
import com.juniper.npitracker.util.ConnectionDetector;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActiveRelSummery extends AppCompatActivity implements View.OnClickListener{
    private final String TAG=getClass().getSimpleName();
    ListView listview;private SearchView mSearchView;
    private ArrayList<ActiveRelModel> activeRelModeldata = new ArrayList<ActiveRelModel>();
    Typeface font;ImageButton _b_id_taptapme;
  //  private ArrayList<ActiveRelModel> activeRelModel = new ArrayList<ActiveRelModel>();
    CustomDialog customDialog;
    ActiveRelAdapter adapter;
    StringRequest sceduleStringRequest;
    String ActiveRel_API="http://rbu.juniper.net/reg_testbeds/active_release.php";
    LinearLayout _relsummary_linear_text_status_view,_activerel_main_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_rel_summery);
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.home_button).setOnClickListener(this);
        mSearchView=(SearchView) findViewById(R.id.searchViewReleaseName);
        findViewById(R.id.searchViewReleaseName);
        findViewById(R.id.newb_id_reload).setOnClickListener(this);
        font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        _relsummary_linear_text_status_view=(LinearLayout) findViewById(R.id.relsummary_linear_text_status_view);
        _activerel_main_layout=(LinearLayout) findViewById(R.id.activerel_main_layout);
        _b_id_taptapme=(ImageButton)findViewById(R.id.newb_id_reload);
       // _b_id_taptapme.setTypeface(font);
        _b_id_taptapme.setOnClickListener(this);

        listview=(ListView) findViewById(R.id.list_active_rel);
        setupSearchView();
        activeRelModeldata.clear();

        Cursor cursor = getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_LAST_SYNCH_URI, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do{
                String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_LAST_SYNCH));
                // Log.i(TAG+"String",apistring);
                activeRelModeldata=  ParseJSON(apistring);
                adapter=new ActiveRelAdapter(ActiveRelSummery.this,activeRelModeldata);
                listview.setAdapter(adapter);

            } while (cursor.moveToNext());
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView relname=(TextView) view.findViewById(R.id.active_rel_name);
                TextView firstpass=(TextView) view.findViewById(R.id.active_rel_firstpass_display);
                Intent intent=new Intent(ActiveRelSummery.this,ActiveRelReport.class);
                intent.putExtra("releasenumber",relname.getText().toString());
                intent.putExtra("firstpass",firstpass.getText().toString());
                startActivity(intent);



            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setupSearchView()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mSearchView.setQueryRefinementEnabled(true);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
        mSearchView.setIconifiedByDefault(false);


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);
//                mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);

                if (TextUtils.isEmpty(newText)) {
                    listview.clearTextFilter();
                    activeRelModeldata.clear();
                    mSearchView.clearFocus();
                    Cursor cursor = getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_LAST_SYNCH_URI, null, null, null, null, null);

                    if (cursor.moveToFirst()) {
                        do{
                            String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_LAST_SYNCH));
                            // Log.i(TAG+"String",apistring);
                            activeRelModeldata=  ParseJSON(apistring);
                            adapter=new ActiveRelAdapter(ActiveRelSummery.this,activeRelModeldata);
                            listview.setAdapter(adapter);

                        } while (cursor.moveToNext());
                    }

                } else {
                    ActiveRelAdapter ca = (ActiveRelAdapter)listview.getAdapter();
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



    @Override
    protected void onStart() {
        super.onStart();
        /*ConnectionDetector conn = new ConnectionDetector(getApplicationContext());
        if(conn.isConnectingToInternet()) {
            sendRequest();

        }else{
            CustomTast ct=new CustomTast(ActiveRelSummery.this);
            ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        }*/

       // new ActiveReleaseAsychtask().execute();

       /* ConnectionDetector conn = new ConnectionDetector(getApplicationContext());
        if(conn.isConnectingToInternet()) {
            new ActiveReleaseAsychtask().execute();

        }else{
            CustomTast ct=new CustomTast(ActiveRelSummery.this);
            ct.showCustomAlert("Check EasyConnect Or Pulse Connection",R.drawable.disconnect);
        }*/
    }


    private void sendRequest() {

        customDialog=new CustomDialog(ActiveRelSummery.this,"Loading...");
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        //entries.clear();
        activeRelModeldata.clear();
        sceduleStringRequest = new StringRequest(ActiveRel_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        activeRelModeldata = ParseJSON(response);
                        // ArrayList<String> data = ParseChartJSON(response);
                       // Log.i(TAG,response);
                        //  if ((activeReleaseModel!=null)&&(data!=null)){
                        if ((activeRelModeldata!=null)){
                            customDialog.cancel();
                            _relsummary_linear_text_status_view.setVisibility(View.GONE);
                            _activerel_main_layout.setVisibility(View.VISIBLE);
                            adapter = new ActiveRelAdapter(ActiveRelSummery.this, activeRelModeldata);
                            listview.setAdapter(adapter);
                            //  Log.i(Tag, "onpost beforeadapter.notifyDataSetChanged");
                            // setData(100, data);
                            adapter.notifyDataSetChanged();

                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ActiveReleasesSummery.this,"Connect EasyConnect JuniperNetwork", Toast.LENGTH_LONG).show();
                        customDialog.cancel();
                        _relsummary_linear_text_status_view.setVisibility(View.VISIBLE);
                        _activerel_main_layout.setVisibility(View.GONE);
                        CustomTast ct=new CustomTast(ActiveRelSummery.this);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                });
       /* chartStringRequest = new StringRequest(Chart_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> data =ParseChartJSON(response);
                        if (response!=null) {

                            setData(100, data);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomTast ct=new CustomTast(ActiveReleasesSummery.this);
                        ct.showCustomAlert("Check EasyConnect Or Pulse Connection",R.drawable.disconnect);
                        //Toast.makeText(ActiveReleasesSummery.this,"Please Check Pulse Connectivity", Toast.LENGTH_LONG).show();
                    }
                });*/
        JdiAppController.getInstance().addToRequestQueue(sceduleStringRequest);
        // JdiAppController.getInstance().addToRequestQueue(chartStringRequest);
    }
    private ArrayList<ActiveRelModel> ParseJSON(String jsonStr) {
        // Log.i(Tag, jsonStr);
        if (jsonStr != null) {
            try {

                JSONObject obj = new JSONObject(jsonStr);

                String result = obj.getString("status");
                if (result.equalsIgnoreCase("ok")) {
                    // Log.i(Tag,"inside ok");
                    //JSONObject m_jArry_object = obj.getJSONObject("data");
                    JSONArray  m_jArry=obj.getJSONArray("data");
                    //JSONArray  m_jArry=m_jArry_object.getJSONArray("activerelsummary");

                    for (int ii = 0; ii < m_jArry.length(); ii++) {

                            JSONObject jo_inside = m_jArry.getJSONObject(ii);

                            ActiveRelModel model = new ActiveRelModel();

                            model.setRelname(jo_inside.getString("rn"));
                          //  Log.i(TAG,model.getRelname());
                            model.setFirstpass(jo_inside.getString("fr"));
                           // Log.i(TAG,model.getFirstpass());
                            model.setOverallpas(jo_inside.getString("op"));
                         //   Log.i(TAG,model.getOverallpas());
                            model.setOpenblocker(jo_inside.getString("bl"));
                          //  Log.i(TAG,model.getOpenblocker());
                        activeRelModeldata.add(model);


                    }

                } else {
                   // Log.e(TAG, "Couldn't get response from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                    /*Toast.makeText(getApplicationContext(),
                            "Please Connect With Your Pulse Properly", Toast.LENGTH_LONG).show();*/
                            CustomTast ct = new CustomTast(ActiveRelSummery.this);
                            ct.showCustomAlert("Server Busy Try Later",R.drawable.warning);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        return activeRelModeldata;
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_translate);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_close_translate,R.anim.activity_open_scale);
        startActivity(new Intent(ActiveRelSummery.this, MainActivity.class));

        finish();

    }
    /*private class ActiveReleaseAsychtask extends AsyncTask<Void,Void,ArrayList<ActiveRelModel>> {
        @Override
        protected ArrayList<ActiveRelModel> doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            activeRelModel.clear();
            //entries.clear();
           // String jsonStr = Util.loadJSONFromAsset(ActiveRelSummery.this,"activerel.json");
            String jsonStr= sh.makeServiceCall(ActiveRel_API);
          //  Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject obj = new JSONObject(jsonStr);

                    String result = obj.getString("status");
                    if (result.equalsIgnoreCase("ok")){
                        JSONArray m_jArry = obj.getJSONArray("data");

                        for (int i = 0; i < m_jArry.length(); i++) {
                            JSONObject jo_inside = m_jArry.getJSONObject(i);

                            ActiveRelModel model = new ActiveRelModel();

                            model.setRelname(jo_inside.getString("rn"));
                          //  Log.i(TAG,model.getRelname());
                            model.setFirstpass(jo_inside.getString("fr"));
                           // Log.i(TAG,model.getFirstpass());
                            model.setOverallpas(jo_inside.getString("op"));
                          //  Log.i(TAG,model.getOverallpas());
                            model.setOpenblocker(jo_inside.getString("bl"));
                           // Log.i(TAG,model.getOpenblocker());
                            activeRelModel.add(model);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
               // Log.e(TAG, "Couldn't get response from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Please Connect With Your Pulse Properly", Toast.LENGTH_LONG).show();
                        CustomTast ct=new CustomTast(ActiveRelSummery.this);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                });

            }

            return activeRelModel;
        }

        @Override
        protected void onPostExecute(ArrayList<ActiveRelModel> activeRelModels) {
            super.onPostExecute(activeRelModels);
            // Log.i(Tag,"onpost before new SchdulerAdapter(Scheduler.this,schedulerModels)");
            adapter=new ActiveRelAdapter(ActiveRelSummery.this,activeRelModels);
            listview.setAdapter(adapter);
            // Log.i(Tag,"onpost beforeadapter.notifyDataSetChanged");
            adapter.notifyDataSetChanged();
            // Log.i(Tag,"onpost after.notifyDataSetChanged");
        }

    }*/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_button:
                startActivity(new Intent(ActiveRelSummery.this, MainActivity.class));
               // overridePendingTransition(R.anim.activity_close_translate,R.anim.activity_open_scale);
                finish();
                break;
            /*case R.id.home_button:
                startActivity(new Intent(ActiveRelSummery.this, DashBoard.class));
                overridePendingTransition(R.anim.activity_close_translate,R.anim.activity_open_scale);
                finish();
                break;*/
            case R.id.newb_id_reload:
                sendRequest();
                break;
        }
    }
}
