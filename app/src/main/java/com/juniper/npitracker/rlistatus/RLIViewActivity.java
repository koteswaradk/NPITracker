package com.juniper.npitracker.rlistatus;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.juniper.npitracker.JdiAppController;
import com.juniper.npitracker.MainActivity;
import com.juniper.npitracker.NPITrackerPhaseActivity;
import com.juniper.npitracker.R;

import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.util.ConnectionDetector;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RLIViewActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG=getClass().getSimpleName();
    TextView t_lastsync;
//    ListView rlistview;
    FloatingGroupExpandableListView myList;
    CustomDialog customDialog;
    StringRequest releaselistStringRequest;
    ReleaseListAdapter adapter;
    String lastsync;
     EditText  alert_edittext_testlead,alert_edittext_softwarelead,synopisis;
    TextInputLayout inputLayout_tl,inputLayout_sw,inputLayout_release;
    LinearLayout layout_back_button;
//    String ActiveRel_API="http://rbu-dashboard.juniper.net/mobile_apps/RLI/active_release.json";
    String ActiveRel_API="http://rbu-dashboard.juniper.net/mobile_apps/RLI/Page1.json";
    String updateRangeScheduler_API="http://jdiregression.juniper.net/mobile/api/";
    private ArrayList<ReleaseListModel> releaseListModels = new ArrayList<ReleaseListModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_list);
        t_lastsync=(TextView)findViewById(R.id.t_lastsync_display);
//        rlistview=(ListView) findViewById(R.id.releaselist);
        myList = (FloatingGroupExpandableListView) findViewById(R.id.releaselist);

        findViewById(R.id.bt_back).setOnClickListener(this);
        findViewById(R.id.npimenu).setOnClickListener(this);
        layout_back_button=(LinearLayout) findViewById(R.id.layout_back_button);
        layout_back_button.setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);

        releaseListModels.clear();
        ConnectionDetector conn = new ConnectionDetector(getApplicationContext());
        if(conn.isConnectingToInternet()) {
            sendRequest();
        }else{
            CustomTast ct=new CustomTast(RLIViewActivity.this);
            ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

       /* Cursor cursor = getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_PR_SUMMARY_URI, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do{
                String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PR_SUMMARY));
                // Log.i(TAG+"String",apistring);
                releaseListModels=  ParseJSON(apistring);
//                adapter=new ReleaseListAdapter(RLIViewActivity.this,releaseListModels);
//                rlistview.setAdapter(adapter);

                BaseExpandableListAdapter myAdapter = new ReleaseListAdapter(RLIViewActivity.this,releaseListModels);
                WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(myAdapter);
                myList.setAdapter(wrapperAdapter);

                t_lastsync.setText(lastsync);
            } while (cursor.moveToNext());
        }
    */}

    private void sendRequest() {

        customDialog=new CustomDialog(RLIViewActivity.this,"Loading...");
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();

        releaseListModels.clear();
        releaselistStringRequest = new StringRequest(ActiveRel_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        releaseListModels = ParseJSON(response);
                        // ArrayList<String> data = ParseChartJSON(response);
                        // Log.i(TAG,response);
                        //  if ((activeReleaseModel!=null)&&(data!=null)){
                        if ((releaseListModels!=null)){
                            customDialog.cancel();
//                            _relsummary_linear_text_status_view.setVisibility(View.GONE);
//                            _activerel_main_layout.setVisibility(View.VISIBLE);
                            t_lastsync.setText(lastsync);
//                            adapter = new ReleaseListAdapter(RLIViewActivity.this, releaseListModels);
//                            rlistview.setAdapter(adapter);

                            BaseExpandableListAdapter myAdapter = new ReleaseListAdapter(RLIViewActivity.this,releaseListModels);
                            WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(myAdapter);
                            myList.setAdapter(wrapperAdapter);

                            myAdapter.notifyDataSetChanged();

                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ActiveReleasesSummery.this,"Connect EasyConnect JuniperNetwork", Toast.LENGTH_LONG).show();
                        customDialog.cancel();
//                        _relsummary_linear_text_status_view.setVisibility(View.VISIBLE);
//                        _activerel_main_layout.setVisibility(View.GONE);
                        CustomTast ct=new CustomTast(RLIViewActivity.this);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                });

        JdiAppController.getInstance().addToRequestQueue(releaselistStringRequest);

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
        startActivity(new Intent(RLIViewActivity.this,MainActivity.class));
        finish();
    }

//    private ArrayList<ReleaseListModel> ParseJSON(String jsonStr) {
//        // Log.i(Tag, jsonStr);
//        if (jsonStr != null) {
//            try {
//
//                JSONObject obj = new JSONObject(jsonStr);
//
//                String result = obj.getString("status");
//                if (result.equalsIgnoreCase("ok")) {
//                    // Log.i(Tag,"inside ok");
//                     lastsync = obj.getString("sync_time");
//                    JSONArray m_jArry=obj.getJSONArray("data");
//                    //JSONArray  m_jArry=m_jArry_object.getJSONArray("activerelsummary");
//
//                    for (int i = 0; i < m_jArry.length(); i++) {
//
//                        JSONObject jo_inside = m_jArry.getJSONObject(i);
//
//                        ReleaseListModel model = new ReleaseListModel();
//
//                        model.setRelname(jo_inside.getString("release"));
//                        //  Log.i(TAG,model.getRelname());
//                        model.setHwlead(jo_inside.getString("hw_lead"));
//                        // Log.i(TAG,model.getFirstpass());
//                        model.setSwlead(jo_inside.getString("sw_lead"));
//                        //   Log.i(TAG,model.getOverallpas());
//
//                        releaseListModels.add(model);
//
//
//                    }
//
//                } else {
//                    // Log.e(TAG, "Couldn't get response from server.");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            CustomTast ct = new CustomTast(RLIViewActivity.this);
//                            ct.showCustomAlert(" no data for selected release",R.drawable.warning);
//                        }
//                    });
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//
//        }
//
//        return releaseListModels;
//    }


    private ArrayList<ReleaseListModel> ParseJSON(String jsonStr) {
        // Log.i(Tag, jsonStr);
        if (jsonStr != null) {
            try {

                JSONObject obj = new JSONObject(jsonStr);

                String result = obj.getString("status");
                if (result.equalsIgnoreCase("ok")) {
                    // Log.i(Tag,"inside ok");
                    lastsync = obj.getString("sync_time");
//                    JSONArray m_jArry=obj.getJSONArray("data");
                    //JSONArray  m_jArry=m_jArry_object.getJSONArray("activerelsummary");

//                    for (int i = 0; i < m_jArry.length(); i++) {

                    Iterator<?> releaseKeys = obj.keys();
                    while(releaseKeys.hasNext()){

//                        JSONObject jo_inside = m_jArry.getJSONObject(i);

                        String key = (String) releaseKeys.next();

                        if(key.equalsIgnoreCase("status") || key.equalsIgnoreCase("sync_time"))
                            continue;

                        JSONObject jo_inside = obj.getJSONObject(key);

                        ReleaseListModel model = new ReleaseListModel();

                        model.setRelname(jo_inside.getString("release"));
                        model.setHwlead(jo_inside.getString("hw_lead"));
                        // Log.i(TAG,model.getFirstpass());
                        model.setSwlead(jo_inside.getString("sw_lead"));
                        //   Log.i(TAG,model.getOverallpas());

                        //handle NPIS
                        JSONObject j_RLIs = jo_inside.getJSONObject("npi");

                        Iterator<?> NPIKeys = j_RLIs.keys();

                        while(NPIKeys.hasNext()){

                            String npi_name = (String)NPIKeys.next();
                            int totalRli;
                            int atRisk;
                            int processFollowed;
                            int processNotFollowed;

                            if(j_RLIs.getJSONObject(npi_name).isNull("total_rlis")){
                                totalRli = 0;
                            }
                            else{
                                totalRli = j_RLIs.getJSONObject(npi_name).getInt("total_rlis");
                            }

                            if(j_RLIs.getJSONObject(npi_name).isNull("at_risk")){
                                atRisk = 0;
                            }
                            else{
                                atRisk = j_RLIs.getJSONObject(npi_name).getInt("at_risk");
                            }

                            if(j_RLIs.getJSONObject(npi_name).isNull("process_followed")){
                                processFollowed = 0;
                            }
                            else{
                                processFollowed = j_RLIs.getJSONObject(npi_name).getInt("process_followed");
                            }

                            if(j_RLIs.getJSONObject(npi_name).isNull("processnot_followed")){
                                processNotFollowed = 0;
                            }
                            else{
                                processNotFollowed = j_RLIs.getJSONObject(npi_name).getInt("processnot_followed");
                            }

                            model.addToNPIList(npi_name,
                                    totalRli,
                                    atRisk,
                                    processFollowed,
                                    processNotFollowed
                            );

                        }

                        releaseListModels.add(model);


                    }

                } else {
                    // Log.e(TAG, "Couldn't get response from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomTast ct = new CustomTast(RLIViewActivity.this);
                            ct.showCustomAlert(" no data for selected release",R.drawable.warning);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        return releaseListModels;
    }


    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
         final EditText  alert_edittext_testlead = (EditText) dialogView.findViewById(R.id.alert_edittext_testlead);
         final EditText alert_edittext_softwarelead = (EditText) dialogView.findViewById(R.id.alert_edittext_softwarelead);
         final EditText releasename = (EditText) dialogView.findViewById(R.id.releasenme);

           inputLayout_tl=(TextInputLayout)dialogView.findViewById(R.id.input_layout_testlead);
           inputLayout_sw=(TextInputLayout)dialogView.findViewById(R.id.input_layout_softwarelead);
           inputLayout_release=(TextInputLayout)dialogView.findViewById(R.id.input_layout_releasename);

        inputLayout_tl.setErrorEnabled(true);
        inputLayout_sw.setErrorEnabled(true);
        inputLayout_release.setErrorEnabled(true);

        /*dialogBuilder.setTitle("RLI VIEW");*/
        dialogBuilder.setMessage("Request For");
       final AlertDialog b = dialogBuilder.create();
        dialogView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if (validate(alert_edittext_testlead.getText().toString(),alert_edittext_softwarelead.getText().toString(),releasename.getText().toString()))
                {
                    b.dismiss();
                    open();
                   // Toast.makeText(RLIViewActivity.this, alert_edittext_testlead.getText().toString() + "\n" + alert_edittext_softwarelead.getText().toString() + "\n" + synopisis.getText().toString(), Toast.LENGTH_SHORT).show();
                   // updateType(alert_edittext_testlead.getText().toString(),alert_edittext_softwarelead.getText().toString(),releasename.getText().toString());
                }


            }
        });
        /*dialogView.findViewById(R.id.button_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        b.show();
    }
    public boolean validate(String testlead,String swlead,String synopsis) {
        boolean valid = true;

        if (testlead.isEmpty()) {
            inputLayout_tl.setError("enter testlead");

            valid = false;
        } else {
            inputLayout_tl.setError(null);
        }
        if (swlead.isEmpty()) {
            inputLayout_sw.setError("enter swlead");
            valid = false;
        } else {
            inputLayout_sw.setError(null);
        }
        if (synopsis.isEmpty()) {
            inputLayout_release.setError("enter releasename");
            valid = false;
        } else {
            inputLayout_release.setError(null);
        }
        return valid;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_back_button:
                startActivity(new Intent(RLIViewActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.bt_back:
                startActivity(new Intent(RLIViewActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.npimenu:
                startActivity(new Intent(RLIViewActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.settings:
                showChangeLangDialog();
                break;
        }
    }
    private void updateType(final String testlead,final String swlead,final String releasename){
        // Log.i(TAG,"updateType");
        StringRequest request = new StringRequest(Request.Method.POST, updateRangeScheduler_API, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // running on main thread-------
                try {
                    JSONObject res = new JSONObject(response);
                    res.getString("result");
                    System.out.println("Response:" + res.getString("result"));
                    /*Log.i(TAG,response);*/

                    if (res.getString("result").equalsIgnoreCase("ok")) {
                        //your code here
                        //Log.i(TAG,"after result ok");
                       /* if (changedtype.equalsIgnoreCase("full")){
                            golbalmodel.setFull(true);
                            golbalmodel.setPartial(false);
                            golbalmodel.setType("full");
                            notifyDataSetChanged();

                        }if (changedtype.equalsIgnoreCase("partial")){
                            golbalmodel.setFull(false);
                            golbalmodel.setPartial(true);
                            golbalmodel.setType("partial");
                            notifyDataSetChanged();
                        }*/
                    /* Intent i = new Intent(context,Scheduler.class);
                     i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     //Log.i(TAG,"1");
                     i.putExtra("response", res.getString("result"));
                     //Log.i(TAG,"2");
                     ((Activity)context).startActivity(i);*/

                        //Log.i(TAG,"3");
                        //  notifyDataSetChanged();
                        // Log.i(TAG,"4");
                    }else{
                        CustomTast ct=new CustomTast(getApplicationContext());
                        ct.showCustomAlert("Network/Server Disconnected",R.drawable.disconnect);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    //Log.e("Response", "==> " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // running on main thread-------
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());

            }
        }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<String, String>();
               /* hashMapParams.put(Util.action, Util.updateData);
                hashMapParams.put(Util.page, Util.scheduler);*/
                hashMapParams.put("testlead", testlead);
                hashMapParams.put("swlead", swlead);
                hashMapParams.put("releasename", releasename);
               // System.out.println("Hashmap:" + hashMapParams);
                return hashMapParams;
            }
        };
        JdiAppController.getInstance().addToRequestQueue(request);

    }
    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                RLIViewActivity.this);

        String message = "Please Wait It Takes 24h To Update.";

        /*if(!plm.isEmpty() || !swLead.isEmpty() || !testLead.isEmpty()){
//            Log.d("LEADS", plm + ","+ swLead+ ","+testLead);
            message += "\nPlease Contact: ";
        }

        if (!plm.isEmpty()){
            message += plm + " ";
        }
        if (!swLead.isEmpty()){
            message += swLead + " ";
        }
        if (!testLead.isEmpty()){
            message += testLead + " ";
        }*/


        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {

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
