package com.juniper.npitracker.activereleases;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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
import java.util.Arrays;
import java.util.HashMap;

/*import com.github.mikephil.charting.data.PieEntry;*/

/**
 * Created by koteswara on 26/12/16.
 */

public class ActiveRelReport extends AppCompatActivity implements View.OnClickListener {
    private final String TAG=getClass().getSimpleName();
    TextView _release_number,_text_total_scripts_planned_display,_text_total_scripts_executed_display,_text_congratulation,_text_pass,
            _total_scripts_pending_display,_total_scripts_passed_display,_text_false_failute_display,_text_pending_debug_display,_text_percenatge;
    Button _b_total_scripts_planned,_b_total_scripts_executed,_b_scripts_pending,_b_scripts_passed,_b_false_failute,_b_pendingdebug,like_image,_b_firstpass;
            ImageButton _b_id_taptapme;
    LinearLayout _relreport_reload,_relport_main_layout;
    boolean condition=false;
    Typeface font;RadioButton btn;
    CustomDialog customDialog;
    ArrayList<String> barEntries  = new ArrayList<>();
    ArrayList<String> functions;
    HashMap<String,String> values=new HashMap<String,String>();
    ArrayList<String>labels ;
   protected BarChart barchart;
    String countryName[] = { "BBE","EX4600","EX-Mojito","EX-Rodnik","QFX10002","QFX10008/QFX10016","QFX-OPUS","RPD","BBE","EX4600","EX-Mojito","EX-Rodnik","QFX10002","QFX10008/QFX10016","QFX-OPUS","RPD","BBE","EX4600","EX-Mojito","EX-Rodnik","QFX10002","QFX10008/QFX10016","QFX-OPUS","RPD","BBE","EX4600","EX-Mojito","EX-Rodnik","QFX10002","QFX10008/QFX10016","QFX-OPUS","RPD"};
    ArrayList<String> function=new ArrayList<String>(Arrays.asList(countryName));
    //String ActiveRel_API="http://rbu.juniper.net/reg_testbeds/activerelreport.php/search.json?rn=";
    String ActiveRel_Function_API= "http://rbu.juniper.net/reg_testbeds/functionname.php?rn=";
    String ActiveRel_URL="http://rbu.juniper.net/reg_testbeds/activerelreport.php/search.json?rn=";
    String s_scriptsplanned, s_scriptsexecuted, s_scriptspending, s_scriptspassed, s_falsefailure, s_pendingdebugs, S_releasename,rel_name,S_firstpass;
    StringRequest testReportStringRequest,functionStringRequest;
    RadioGroup rg;LinearLayout mLinearLayout;
     RadioButton[] rb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_rel_report);
        font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        init();
        fontAwesomeInt();
        _release_number=(TextView)findViewById(R.id.releasenumber);
        _b_id_taptapme.setOnClickListener(this);

        barchart=(BarChart) findViewById(R.id.barchartlive);
       // barchart.setOnChartValueSelectedListener(this);
        barchart.setDescription(null);
        barChartInit();
         mLinearLayout = (LinearLayout) findViewById(R.id.linear1);

        // create radio button
         rb = new RadioButton[function.size()];
         rg = new RadioGroup(this);

        rg.setOrientation(RadioGroup.HORIZONTAL);
       // rg.setBackground(R.drawable.rbtn_selector);
        mLinearLayout.addView(rg);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            assert bundle != null;
            _release_number.setText(bundle.getString("releasenumber"));
            rel_name=bundle.getString("releasenumber");
            S_firstpass=bundle.getString("firstpass");
            ConnectionDetector conn = new ConnectionDetector(getApplicationContext());
            if(conn.isConnectingToInternet()) {

               // new FunctionAsychTask().execute();
                sendFunctionRequest();

            }else{

                CustomTast ct=new CustomTast(ActiveRelReport.this);
                ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
            }
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                rb[checkedId].setBackground(getResources().getDrawable(R.drawable.rbtn_selector));
                btn = (RadioButton)findViewById(checkedId);
                String text= (String) btn.getText();
               // Toast.makeText(ActiveRelReport.this,"dynamicradio"+ text,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ActiveRelReport.this,FunctionsDetailsActivity.class);
                intent.putExtra("function",(String) btn.getText());
                intent.putExtra("relname",rel_name);
                startActivity(intent);

            }
        });

    }



    private  void getFunctionList(ArrayList<String> functionList){
      for (int i = 0; i < functionList.size(); i++) {
          rb[i] = new RadioButton(this);
          rg.addView(rb[i]);
          rb[i].setText(functionList.get(i));
          rb[i].setPadding(15,15,15,15);
          rb[i].setTextSize(18);
          rb[i].setButtonDrawable(R.drawable.rbtn_selector);
          rb[i].setTextColor(ColorStateList.valueOf(Color.WHITE));
          // rb[i].setHeight(80);
          rb[i].setBackground(getResources().getDrawable(R.drawable.rbtn_selector));

      }

  }

    private class TestReportAsychtask extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialog=new CustomDialog(ActiveRelReport.this,"Loading...");
            customDialog.setCanceledOnTouchOutside(false);
            customDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            barEntries = new ArrayList<>();

            // Log.i(TAG+"url",ActiveRel_URL);
            HttpHandler sh = new HttpHandler();

            String jsonStr =sh.makeServiceCall(ActiveRel_URL+rel_name);
            // Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jo_object = new JSONObject(jsonStr);

                    JSONArray array = jo_object.getJSONArray("activerel");
                    for(int i = 0 ; i < array.length() ; i++){

                        JSONObject jo_inside = array.getJSONObject(i);

                        s_scriptsplanned=jo_inside.get("spl").toString();
                        //  Log.i(TAG,s_scriptsplanned);
                        s_scriptsexecuted=jo_inside.get("sce").toString();
                        //  Log.i(TAG,s_scriptsexecuted);
                        s_scriptspending=jo_inside.get("tdp").toString();
                        // Log.i(TAG,s_scriptspending);
                        s_scriptspassed=jo_inside.get("tp").toString();
                        //  Log.i(TAG,s_scriptspassed);
                        s_falsefailure=jo_inside.get("tf").toString();
                        //  Log.i(TAG,s_falsefailure);
                        s_pendingdebugs=jo_inside.get("tpd").toString();
                        // Log.i(TAG,s_pendingdebugs);


                    }
                    JSONArray chartarray = jo_object.getJSONArray("barchart");

                    // Log.i(TAG,"check"+chartarray.toString());
                    for(int i = 0 ; i < chartarray.length() ; i++){
                        try {
                            JSONObject jo_inside = chartarray.getJSONObject(i);

                            barEntries.add((jo_inside.get("sp").toString()));
                            //  Log.i(TAG,jo_inside.get("sp").toString());
                            barEntries.add(jo_inside.get("se").toString());
                            //  Log.i(TAG,jo_inside.get("se").toString());
                            barEntries.add(jo_inside.get("td").toString());
                            //  Log.i(TAG,jo_inside.get("tsp").toString());
                            barEntries.add(jo_inside.get("tsp").toString());
                            //  Log.i(TAG,jo_inside.get("tsf").toString());
                            barEntries.add(jo_inside.get("tsf").toString());
                            //  Log.i(TAG,jo_inside.get("rpd").toString());
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
            if (barEntries!=null)condition=true;
            return condition;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid) {
                customDialog.dismiss();

                _text_total_scripts_planned_display.setText(s_scriptsplanned);
                _text_total_scripts_executed_display.setText(s_scriptsexecuted);
                _total_scripts_pending_display.setText(s_scriptspending);
                _total_scripts_passed_display.setText(s_scriptspassed);
                _text_false_failute_display.setText(s_falsefailure);
                _text_pending_debug_display.setText(s_pendingdebugs);
              /*  for (int i = 0; i < barEntries.size(); i++) {
                    System.out.println("inside to check bar value" + barEntries.get(i));
                }*/
                setData(5, barEntries);
            }
            else {
                //  Log.i(TAG,"else");
                customDialog.dismiss();

            }

        }

    }


    private void init(){
        //butons id
        findViewById(R.id.report_back_button).setOnClickListener(this);
        findViewById(R.id.report_home_button).setOnClickListener(this);


        _relreport_reload=(LinearLayout) findViewById(R.id.relreport_reload);
        _relport_main_layout=(LinearLayout) findViewById(R.id.relport_main_layout);
        //textviews id
        _text_total_scripts_planned_display=(TextView)findViewById(R.id.text_total_scripts_planned_display);
        _text_total_scripts_executed_display=(TextView)findViewById(R.id.text_total_scripts_executed_display);
        _total_scripts_pending_display=(TextView)findViewById(R.id.total_scripts_pending_display);
        _total_scripts_passed_display=(TextView)findViewById(R.id.total_scripts_passed_display);
        _text_false_failute_display=(TextView)findViewById(R.id.text_false_failute_display);
        _text_pending_debug_display=(TextView)findViewById(R.id.text_pending_debug_display);
        _text_percenatge=(TextView)findViewById(R.id.text_percenatge);
        _text_congratulation=(TextView)findViewById(R.id.text_congratulation);
        _text_pass=(TextView)findViewById(R.id.text_pass);
        labels = new ArrayList<String>();
        labels.add("planned");
        labels.add("executed");
        labels.add("pending");
        labels.add("failed");
        labels.add("debug pending");

    }
    private void fontAwesomeInt(){
        _b_total_scripts_planned=(Button)findViewById(R.id.b_total_scripts_planned);
        _b_total_scripts_executed=(Button)findViewById(R.id.b_total_scripts_executed);
        _b_firstpass=(Button)findViewById(R.id.b_firstpass);
        _b_scripts_pending=(Button)findViewById(R.id.b_scripts_pending);
        _b_scripts_passed=(Button)findViewById(R.id.b_scripts_passed);
        _b_false_failute=(Button)findViewById(R.id.b_false_failute);
        _b_pendingdebug=(Button)findViewById(R.id.b_pendingdebug);
        _b_id_taptapme=(ImageButton)findViewById(R.id.b_id_taptapme);
        like_image=(Button)findViewById(R.id.like_image);

        _b_total_scripts_planned.setTypeface(font);
        _b_total_scripts_executed.setTypeface(font);
        _b_scripts_pending.setTypeface(font);
        _b_scripts_passed.setTypeface(font);
        _b_false_failute.setTypeface(font);
        _b_pendingdebug.setTypeface(font);
     //   _b_id_taptapme.setTypeface(font);
        like_image.setTypeface(font);
        _b_firstpass.setTypeface(font);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            assert bundle != null;
            _release_number.setText(bundle.getString("releasenumber"));
            rel_name=bundle.getString("releasenumber");
            S_firstpass=bundle.getString("firstpass");
            Log.i(TAG,rel_name);

           /*// Log.i(TAG,rel_name);
            _text_percenatge.setText(S_firstpass);
            if (Integer.parseInt(S_firstpass.substring(0, S_firstpass.length()-1))>80){
                like_image.setTextColor(ContextCompat.getColor(this, R.color.j_excellence));
              //  Log.i(TAG,""+Integer.parseInt(S_firstpass.substring(0, S_firstpass.length()-1)));
            }else{
                like_image.setText(R.string.fa_icon_down);
                like_image.setTextColor(ContextCompat.getColor(this, R.color.j_authetic));
                _text_congratulation.setText("U Can Do Better");
                // _release_number.setTextColor(ContextCompat.getColor(this, R.color.j_authetic));
                _text_percenatge.setTextColor(ContextCompat.getColor(this, R.color.j_authetic));
                _text_congratulation.setTextColor(ContextCompat.getColor(this, R.color.j_authetic));
                _text_pass.setTextColor(ContextCompat.getColor(this, R.color.j_authetic));
            }*/

        }
       //
        ConnectionDetector conn = new ConnectionDetector(getApplicationContext());
        if(conn.isConnectingToInternet()) {

            sendRequest();
           // new TestReportAsychtask().execute();


        }else{

            CustomTast ct=new CustomTast(ActiveRelReport.this);
            ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        }



    }

    private void sendFunctionRequest() {

       /* customDialog=new CustomDialog(ActiveRelReport.this,"Loading...");
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();*/
        //  Log.i(TAG,ActiveRel_URL+rel_name);
        functionStringRequest = new StringRequest(ActiveRel_Function_API+rel_name,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Log.i(TAG,"report"+response);
                        ArrayList<String> testreportresponse= ParseFunctionJSON(response);
                        if (testreportresponse!=null){

                            getFunctionList(testreportresponse);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                       // customDialog.cancel();
                        _relreport_reload.setVisibility(View.VISIBLE);
                        _relport_main_layout.setVisibility(View.GONE);
                        CustomTast ct=new CustomTast(ActiveRelReport.this);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                });

        JdiAppController.getInstance().addToRequestQueue(functionStringRequest);

    }
    private void sendRequest() {

        customDialog=new CustomDialog(ActiveRelReport.this,"Loading...");
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
      //  Log.i(TAG,ActiveRel_URL+rel_name);
        testReportStringRequest = new StringRequest(ActiveRel_URL+rel_name,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //   Log.i(TAG,"report"+response);
                        HashMap<String,String> testreportresponse= ParseJSON(response);
                        if (testreportresponse!=null){
                            customDialog.cancel();
                            _relreport_reload.setVisibility(View.GONE);
                            _relport_main_layout.setVisibility(View.VISIBLE);

                            _text_total_scripts_planned_display.setText(testreportresponse.get("scriptsplanned"));
                            _text_total_scripts_executed_display.setText(testreportresponse.get("scriptsexecuted"));
                            _total_scripts_pending_display.setText(testreportresponse.get("scriptspending"));
                            _total_scripts_passed_display.setText(testreportresponse.get("scriptspassed"));
                            _text_false_failute_display.setText(testreportresponse.get("falsefailure"));
                            _text_pending_debug_display.setText(testreportresponse.get("pendingdebugs"));
                          //  Log.i(TAG, "onpost beforeadapter.notifyDataSetChanged");
                            setData(5, barEntries);

                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        customDialog.cancel();
                        _relreport_reload.setVisibility(View.VISIBLE);
                        _relport_main_layout.setVisibility(View.GONE);
                        CustomTast ct=new CustomTast(ActiveRelReport.this);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                });

        JdiAppController.getInstance().addToRequestQueue(testReportStringRequest);

    }
    private ArrayList<String> ParseFunctionJSON(String jsonStr){
        functions  = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONObject jo_object = new JSONObject(jsonStr);

                JSONArray array = jo_object.getJSONArray("function");
                for(int i = 0 ; i < array.length() ; i++){

                    functions.add(array.getString(i));
                    Log.e("json", i+"="+functions.get(i));

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
        /*if (jsonStr != null) {
            try {
                JSONObject obj = new JSONObject(jsonStr);

                // String result = obj.getString("status");
                // Log.i(TAG+"inside parse",result);
                // int i= (int) obj.get("status");
               *//* if(result.equalsIgnoreCase("1"))
                {*//*
                // Log.i(TAG,"inside chart  parse ok");
                // JSONObject m_jArry_object = obj.getJSONObject("data");
                //  Log.i(TAG,"inside chart  parse fater data");

                JSONArray  m_jArry=obj.getJSONArray("activerel");

                for (int ii = 0; ii < m_jArry.length(); ii++) {
                    JSONObject jo_inside = m_jArry.getJSONObject(ii);

                    TestRportModel model = new TestRportModel();
                    values.put("scriptsplanned", jo_inside.getString("spl").toString());
                    values.put("scriptsexecuted", jo_inside.getString("sce").toString());
                    values.put("scriptspending", jo_inside.getString("tdp").toString());
                    values.put("scriptspassed", jo_inside.getString("tp").toString());
                    values.put("falsefailure", jo_inside.getString("tf").toString());
                    values.put("pendingdebugs", jo_inside.getString("tpd").toString());
                }
                JSONArray chartarray = obj.getJSONArray("barchart");

                //  Log.i(TAG,"check"+chartarray.toString());
                for(int i = 0 ; i < chartarray.length() ; i++){
                    try {
                        JSONObject jo_inside = chartarray.getJSONObject(i);


                        barEntries.add((jo_inside.get("sp").toString()));
                        // Log.i(TAG+"iside",jo_inside.get("sp").toString());
                        barEntries.add(jo_inside.get("se").toString());
                        // Log.i(TAG,jo_inside.get("se").toString());
                        barEntries.add(jo_inside.get("td").toString());
                        // Log.i(TAG,jo_inside.get("tsp").toString());
                        barEntries.add(jo_inside.get("tsf").toString());
                        // Log.i(TAG,jo_inside.get("tsf").toString());
                        barEntries.add(jo_inside.get("rpd").toString());

                        // Log.i(TAG,jo_inside.get("rpd").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), "Please Connect With Your Pulse Properly", Toast.LENGTH_LONG).show();
                    CustomTast ct=new CustomTast(ActiveRelReport.this);
                    ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                }
            });
        }*/
        return functions;


    }
    private HashMap<String,String> ParseJSON(String jsonStr){
        if (jsonStr != null) {
            try {
                JSONObject obj = new JSONObject(jsonStr);

               // String result = obj.getString("status");
               // Log.i(TAG+"inside parse",result);
                // int i= (int) obj.get("status");
               /* if(result.equalsIgnoreCase("1"))
                {*/
                   // Log.i(TAG,"inside chart  parse ok");
                    // JSONObject m_jArry_object = obj.getJSONObject("data");
                  //  Log.i(TAG,"inside chart  parse fater data");

                    JSONArray  m_jArry=obj.getJSONArray("activerel");

                    for (int ii = 0; ii < m_jArry.length(); ii++) {
                        JSONObject jo_inside = m_jArry.getJSONObject(ii);

                        TestRportModel model = new TestRportModel();
                        values.put("scriptsplanned", jo_inside.getString("spl").toString());
                        values.put("scriptsexecuted", jo_inside.getString("sce").toString());
                        values.put("scriptspending", jo_inside.getString("tdp").toString());
                        values.put("scriptspassed", jo_inside.getString("tp").toString());
                        values.put("falsefailure", jo_inside.getString("tf").toString());
                        values.put("pendingdebugs", jo_inside.getString("tpd").toString());
                    }
                    JSONArray chartarray = obj.getJSONArray("barchart");

                  //  Log.i(TAG,"check"+chartarray.toString());
                    for(int i = 0 ; i < chartarray.length() ; i++){
                        try {
                            JSONObject jo_inside = chartarray.getJSONObject(i);


                            barEntries.add((jo_inside.get("sp").toString()));
                           // Log.i(TAG+"iside",jo_inside.get("sp").toString());
                            barEntries.add(jo_inside.get("se").toString());
                           // Log.i(TAG,jo_inside.get("se").toString());
                            barEntries.add(jo_inside.get("td").toString());
                           // Log.i(TAG,jo_inside.get("tsp").toString());
                            barEntries.add(jo_inside.get("tsf").toString());
                           // Log.i(TAG,jo_inside.get("tsf").toString());
                            barEntries.add(jo_inside.get("rpd").toString());

                           // Log.i(TAG,jo_inside.get("rpd").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                /*}else {
                    Log.e(TAG, "Couldn't get response from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(), "Please Connect With Your Pulse Properly", Toast.LENGTH_LONG).show();
                            CustomTast ct=new CustomTast(ActiveRelReport.this);
                            ct.showCustomAlert("Server Busy Try Later",R.drawable.warning);
                        }
                    });

                }*/

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), "Please Connect With Your Pulse Properly", Toast.LENGTH_LONG).show();
                    CustomTast ct=new CustomTast(ActiveRelReport.this);
                    ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                }
            });
        }
        return values;


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_translate);
//        btn.setChecked(false);
    }

    /*private class FunctionAsychTask extends AsyncTask<Void,Void,ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();
            functions  = new ArrayList<>();
            String jsonStr =sh.makeServiceCall(ActiveRel_Function_API+rel_name);
           // String jsonStr = Util.loadJSONFromAsset(ActiveRelReport.this,"functions.json");

            if (jsonStr != null) {
                try {
                    JSONObject jo_object = new JSONObject(jsonStr);

                    JSONArray array = jo_object.getJSONArray("function");
                    for(int i = 0 ; i < array.length() ; i++){

                        functions.add(array.getString(i));
                            Log.e("json", i+"="+functions.get(i));

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
            return functions;
        }

        @Override
        protected void onPostExecute(ArrayList<String> stringArrayList) {
            super.onPostExecute(stringArrayList);
            getFunctionList(stringArrayList);

        }
    }*/
    private void barChartInit(){
        barchart.setDrawBarShadow(false);
        barchart.setDrawValueAboveBar(true);
        barchart.setPinchZoom(false);

        barchart.setDrawGridBackground(false);
     //   IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);
        barchart.getAxisLeft().setAxisMinValue(0);

        XAxis xAxis = barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawLabels(true);
        // xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
       // barchart.getXAxis().setLabelsToSkip(0);
      /*  xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(5);*/

      //  xAxis.setValueFormatter(xAxisFormatter);

       // IAxisValueFormatter custom = new MyAxisValueFormatter();

       /* YAxis leftAxis = barchart.getAxisLeft();
      //  leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
       // leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
*/
       /* YAxis rightAxis = barchart.getAxisRight();
        rightAxis.setDrawGridLines(false);
      //  rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
       // rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
*/
        Legend l = barchart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);
       /*  l.setExtra(ColorTemplate.COLORFUL_COLORS, new String[] { "Executed",
         "Planned", "Pending", "Failed", "DebugPending" });*/
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        /*XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        barchart.setMarker(mv); // Set the marker to the chart*/



        // setting data

    }
    private void setData(int count,ArrayList<String> svales) {


        float start = 0f;

/*
        barchart.getXAxis().setAxisMinimum(start);
        barchart.getXAxis().setAxisMaximum(start + count + 2);
*/


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> xVals1 = new ArrayList<BarEntry>();



        for (int i = 0; i <svales.size(); i++) {

          //  yVals1.add(new BarEntry(i+1f,Float.valueOf(svales.get(i))));
           // yVals1.add(new BarEntry(i+1f,Float.valueOf(svales.get(i))));
            yVals1.add(new BarEntry(Float.valueOf(svales.get(i)), i));



        }

        BarDataSet set1;

        if (barchart.getData() != null &&
                barchart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barchart.getData().getDataSetByIndex(0);
         //   set1.setValues(yVals1);

            barchart.getData().notifyDataChanged();
            barchart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setValueTextSize(20f);
            set1.setColors(new int[]{this.getResources().getColor(R.color.amber),
                    this.getResources().getColor(R.color.green),
                    this.getResources().getColor(R.color.primary_dark),
                    this.getResources().getColor(R.color.j_aspiration),
                    this.getResources().getColor(R.color.j_trust)});
          /*  set1.setColors(new int[]{this.getResources().getColor(R.color.white),
                    this.getResources().getColor(R.color.white),
                    this.getResources().getColor(R.color.white),
                    this.getResources().getColor(R.color.white),
                    this.getResources().getColor(R.color.white)});*/

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);

            data.setValueTextSize(10f);

          //  data.setBarWidth(0.9f);
           // barchart.setFitBars(true);
            barchart.setData(data);
            barchart.getAxisRight().setEnabled(false);

            barchart.invalidate();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.report_back_button:
                startActivity(new Intent(ActiveRelReport.this, ActiveRelSummery.class));
              //  overridePendingTransition(R.anim.activity_close_translate,R.anim.activity_open_scale);
                finish();
                break;
            case R.id.report_home_button:
                startActivity(new Intent(ActiveRelReport.this, MainActivity.class));
              //  overridePendingTransition(R.anim.activity_close_translate,R.anim.activity_open_scale);
                finish();
                break;
            case R.id.b_id_taptapme:
                sendRequest();
                break;
        }
    }
}
