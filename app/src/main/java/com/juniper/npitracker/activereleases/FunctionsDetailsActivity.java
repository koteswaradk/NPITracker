package com.juniper.npitracker.activereleases;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.juniper.npitracker.JdiAppController;
import com.juniper.npitracker.MainActivity;
import com.juniper.npitracker.R;
import com.juniper.npitracker.util.ConnectionDetector;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;
import com.juniper.npitracker.util.HttpHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FunctionsDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG=getClass().getSimpleName();
    TextView totalscriptsplanned,totalscriptsexecuted,execution,firstrunpass,release,openblocker_function,openblocker_invisible;
    String s_totalscriptsplanned,s_totalscriptsexecuted,s_execution,s_firstrunpass,s_release,s_openblocker_function;
    ListView listview;
    StringRequest functionStringRequest;
    CustomDialog customDialog;
    LinearLayout _layout_function_present,_layout_no_content_display;
    ArrayList<FunctionDetailsModel> functionDetailsModels=new ArrayList<FunctionDetailsModel>();
    FunctionDetailsAdapter adapter;
    //String functionPRDetails_API= "http://rbu.juniper.net/reg_testbeds/openblocker.php/search.json?rn=";
    //http://rbu.juniper.net/reg_testbeds/openblocker.php/search.json?rn=15.1F7&fn=RPD 
    String functionPRDetails_API= "http://rbu.juniper.net/reg_testbeds/openblocker.php/search.json?rn=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions_details);

        init();
    }
    private void init(){
        release=(TextView)findViewById(R.id.release_display);

        totalscriptsplanned=(TextView)findViewById(R.id.t_planed_display);

        totalscriptsexecuted=(TextView)findViewById(R.id.t_total_sripts_executed_display);

        execution=(TextView)findViewById(R.id.t_xecution_display);

        firstrunpass=(TextView)findViewById(R.id.t_firstrunpass_percetage_display);

        release=(TextView)findViewById(R.id.release_display);

        openblocker_function=(TextView)findViewById(R.id.t_openprfunction_display);

        listview=(ListView) findViewById(R.id.list_function_details);
        findViewById(R.id.d_function_back_button).setOnClickListener(this);
        findViewById(R.id.d_home_button).setOnClickListener(this);
        _layout_function_present=(LinearLayout) findViewById(R.id.layout_function_present);

        _layout_no_content_display=(LinearLayout)findViewById(R.id.layout_no_content_display);
        openblocker_invisible=(TextView)findViewById(R.id.t_openblocker_invisible);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            assert bundle != null;
            release.setText(bundle.getString("relname"));
            s_release=bundle.getString("relname");
            openblocker_function.setText(bundle.getString("function"));
            s_openblocker_function=bundle.getString("function");

        }

        ConnectionDetector conn = new ConnectionDetector(getApplicationContext());
        if(conn.isConnectingToInternet()) {
           // sendRequest();
             new TestReportAsychtask().execute();

        }else{

            CustomTast ct=new CustomTast(FunctionsDetailsActivity.this);
            ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sendRequest() {

        customDialog=new CustomDialog(FunctionsDetailsActivity.this,"Loading...");
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        //  Log.i(TAG,ActiveRel_URL+rel_name);
        functionStringRequest = new StringRequest(functionPRDetails_API+s_release+"&"+s_openblocker_function,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Log.i(TAG,"report"+response);
                        functionDetailsModels = ParseJSON(response);
                        if (functionDetailsModels!=null){
                            customDialog.cancel();
                            _layout_function_present.setVisibility(View.VISIBLE);
                            _layout_no_content_display.setVisibility(View.GONE);
                            totalscriptsplanned.setText(s_totalscriptsplanned);
                            totalscriptsexecuted.setText(s_totalscriptsexecuted);
                            execution.setText(s_execution+"%");
                            firstrunpass.setText(s_firstrunpass+"%");
                            if ((Integer.parseInt(s_firstrunpass)>80)){
                                firstrunpass.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.j_excellence));
                            }
                            if ((Integer.parseInt(s_firstrunpass)<80)){
                                firstrunpass.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.j_authetic));
                            }
                            if ((Integer.parseInt(s_firstrunpass)<50)){
                                firstrunpass.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.textcolor));
                            }
                            if ((Integer.parseInt(s_execution)>90)){
                                execution.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.j_excellence));
                            }
                            if ((Integer.parseInt(s_execution)<90)){
                                execution.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.j_authetic));
                            }
                            if ((Integer.parseInt(s_firstrunpass)<50)){
                                firstrunpass.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.textcolor));
                            }
                            customDialog.dismiss();
                            adapter=new FunctionDetailsAdapter(FunctionsDetailsActivity.this,functionDetailsModels);
                            listview.setAdapter(adapter);
                            // Log.i(Tag,"onpost beforeadapter.notifyDataSetChanged");
                            adapter.notifyDataSetChanged();

                            //  Log.i(TAG, "onpost beforeadapter.notifyDataSetChanged");


                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        customDialog.cancel();
                        _layout_function_present.setVisibility(View.GONE);
                        _layout_no_content_display.setVisibility(View.VISIBLE);
                        openblocker_invisible.setText(s_release);
                        CustomTast ct=new CustomTast(FunctionsDetailsActivity.this);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                });

        JdiAppController.getInstance().addToRequestQueue(functionStringRequest);

    }
    private ArrayList<FunctionDetailsModel> ParseJSON(String jsonStr){
        if (jsonStr != null) {
            try {
                JSONObject jo_object = new JSONObject(jsonStr);

                JSONArray array = jo_object.getJSONArray("functione");
                for (int i = 0; i < array.length(); i++) {

                    JSONObject jo_inside = array.getJSONObject(i);

                    s_totalscriptsplanned = jo_inside.get("totalscriptsplanned").toString();
                    //  Log.i(TAG,s_scriptsplanned);
                    s_totalscriptsexecuted = jo_inside.get("totalscriptsexecuted").toString();
                    //  Log.i(TAG,s_scriptsexecuted);
                    s_execution = jo_inside.get("execution").toString();
                    // Log.i(TAG,s_scriptspending);
                    s_firstrunpass = jo_inside.get("firstrunpass").toString();

                    // Log.i(TAG,s_pendingdebugs);


                }
                JSONArray chartarray = jo_object.getJSONArray("openblockerprdetails");

                // Log.i(TAG,"check"+chartarray.toString());
                for (int i = 0; i < chartarray.length(); i++) {
                    try {
                        JSONObject jo_inside = chartarray.getJSONObject(i);

                        FunctionDetailsModel model = new FunctionDetailsModel();
                        model.setNumber((jo_inside.get("number").toString()));
                        //  Log.i(TAG,jo_inside.get("sp").toString());
                        model.setArrivaldate(jo_inside.get("arrivaldate").toString());
                        //  Log.i(TAG,jo_inside.get("se").toString());
                        model.setReportedin(jo_inside.get("reportedin").toString());
                        //  Log.i(TAG,jo_inside.get("tsp").toString());
                        model.setState(jo_inside.get("state").toString());
                        //  Log.i(TAG,jo_inside.get("tsf").toString());
                        model.setProblemlevel(jo_inside.get("problemlevel").toString());
                        //  Log.i(TAG,jo_inside.get("rpd").toString());
                        model.setHit(jo_inside.get("hit").toString());
                        functionDetailsModels.add(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            //  Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
        return functionDetailsModels;


    }
    @Override
    protected void onPause() {
        super.onPause();
       // overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_translate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.d_function_back_button:
                startActivity(new Intent(FunctionsDetailsActivity.this, ActiveRelReport.class));
               // overridePendingTransition(R.anim.activity_close_translate,R.anim.activity_open_scale);
                finish();
                break;

            case R.id.d_home_button:
                startActivity(new Intent(FunctionsDetailsActivity.this, MainActivity.class));
               // overridePendingTransition(R.anim.activity_close_translate,R.anim.activity_open_scale);
                finish();
                break;
        }
    }

    private class TestReportAsychtask extends AsyncTask<Void,Void,ArrayList<FunctionDetailsModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialog=new CustomDialog(FunctionsDetailsActivity.this,"Loading...");
            customDialog.setCanceledOnTouchOutside(false);
            customDialog.show();
            functionDetailsModels.clear();
        }

        @Override
        protected ArrayList<FunctionDetailsModel> doInBackground(Void... voids) {

           // String functionPRDetails_API= "http://rbu.juniper.net/reg_testbeds/openblocker.php/search.json?rn="+"15.1F7"+"&fn="+"RPD";
            // Log.i(TAG+"url",ActiveRel_URL);
            HttpHandler sh = new HttpHandler();
            //String jsonStr = Util.loadJSONFromAsset(FunctionsDetailsActivity.this,"functiondetails.json");
            String jsonStr =sh.makeServiceCall(functionPRDetails_API+s_release+"&fn="+s_openblocker_function);
           // Log.e(TAG, "  url: " + functionPRDetails_API+s_release+"&fn="+s_openblocker_function);
           // Log.e(TAG, "Response from url: " + jsonStr);
           // Log.e(TAG, s_openblocker_function);

            if (jsonStr != null) {
                try {
                    JSONObject jo_object = new JSONObject(jsonStr);

                        JSONArray array = jo_object.getJSONArray("functionexecution");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject jo_inside = array.getJSONObject(i);

                            s_totalscriptsplanned = jo_inside.get("totalscriptsplanned").toString();
                              Log.i(TAG,s_totalscriptsplanned);
                            s_totalscriptsexecuted = jo_inside.get("totalscriptsexecuted").toString();
                              Log.i(TAG,s_totalscriptsexecuted);
                            s_execution = jo_inside.get("execution").toString();
                            Log.i(TAG,s_execution);
                            s_firstrunpass = jo_inside.get("firstrunpass").toString();

                             Log.i(TAG,s_firstrunpass);


                        }
                        JSONArray chartarray = jo_object.getJSONArray("openblockerprdetails");


                        // Log.i(TAG,"check"+chartarray.toString());
                        for (int i = 0; i < chartarray.length(); i++) {
                            try {
                                JSONObject jo_inside = chartarray.getJSONObject(i);

                                FunctionDetailsModel model = new FunctionDetailsModel();
                                model.setNumber((jo_inside.get("number").toString()));
                                Log.i(TAG, jo_inside.get("number").toString());
                                model.setArrivaldate(jo_inside.get("arrivaldate").toString());
                                Log.i(TAG, jo_inside.get("arrivaldate").toString());
                                model.setReportedin(jo_inside.get("reportedin").toString());
                                Log.i(TAG, jo_inside.get("reportedin").toString());
                                model.setState(jo_inside.get("state").toString());
                                Log.i(TAG, jo_inside.get("state").toString());
                                model.setProblemlevel(jo_inside.get("problemlevel").toString());
                                Log.i(TAG, jo_inside.get("problemlevel").toString());
                                model.setHit(jo_inside.get("hit").toString());
                                Log.i(TAG, jo_inside.get("hit").toString());
                                functionDetailsModels.add(model);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                    }


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                //  Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return functionDetailsModels;
        }

        @Override
        protected void onPostExecute(ArrayList<FunctionDetailsModel> model) {
            super.onPostExecute(model);
            if (model!=null) {
                _layout_function_present.setVisibility(View.VISIBLE);
                _layout_no_content_display.setVisibility(View.GONE);
                totalscriptsplanned.setText(s_totalscriptsplanned);
                totalscriptsexecuted.setText(s_totalscriptsexecuted);
                execution.setText(s_execution+"%");
                firstrunpass.setText(s_firstrunpass+"%");
                if ((Integer.parseInt(s_firstrunpass)>80)){
                    firstrunpass.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.j_excellence));
                }
                if ((Integer.parseInt(s_firstrunpass)<80)){
                    firstrunpass.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.j_authetic));
                }
                if ((Integer.parseInt(s_firstrunpass)<50)){
                    firstrunpass.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.textcolor));
                }
                if ((Integer.parseInt(s_execution)>90)){
                    execution.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.j_excellence));
                }
                if ((Integer.parseInt(s_execution)<90)){
                    execution.setTextColor(ContextCompat.getColor(FunctionsDetailsActivity.this, R.color.j_authetic));
                }

                customDialog.dismiss();
                adapter=new FunctionDetailsAdapter(FunctionsDetailsActivity.this,model);
                listview.setAdapter(adapter);
                // Log.i(Tag,"onpost beforeadapter.notifyDataSetChanged");
                adapter.notifyDataSetChanged();
            }
            else if (model.size()<0){
                 // Log.i(TAG,"else");
                customDialog.dismiss();
                _layout_function_present.setVisibility(View.GONE);
                _layout_no_content_display.setVisibility(View.VISIBLE);
                openblocker_invisible.setText(s_release);


            }

        }

    }
}
