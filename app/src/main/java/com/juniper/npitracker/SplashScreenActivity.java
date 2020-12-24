package com.juniper.npitracker;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;
import com.juniper.npitracker.util.HttpHandler;
import com.juniper.npitracker.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity {
    private String TAG=getClass().getSimpleName();
    CustomDialog customDialog;
    String ActiveRel_API="http://rbu.juniper.net/mobile_apps/Page1.json";
    String Release_API= "http://rbu-dashboard.juniper.net/mobile_apps/RLI/active_release.json";
    String AcriveRelease_API= "http://rbu.juniper.net/reg_testbeds/active_release.php";

    StringRequest npiviewStringRequest,releaseStringRequest,activerelaesStringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


    }
    @Override
    protected void onStart() {
        super.onStart();
       /* ConnectionDetector conn = new ConnectionDetector(getApplicationContext());

        if(conn.isConnectingToInternet()) {
            dropTable();
           new NPITrackerAsychtask().execute();
           // sendRequest();
        }else{
            CustomTast ct=new CustomTast(SplashScreenActivity.this);
            ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        }*/
        new NPITrackerAsychtask().execute();
    }
    private void sendRequest() {

        customDialog=new CustomDialog(SplashScreenActivity.this,"Loading...");
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();

        npiviewStringRequest = new StringRequest(ActiveRel_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Log.i(TAG,response);

                        if ((response!=null)){
                            Uri  uri = ParseJSON(response);
                            if (uri!=null){
                                if (ContentUris.parseId(uri)>0);

                                customDialog.cancel();
                                startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));

                                finish();
                            }else{
                                customDialog.cancel();
                                CustomTast ct=new CustomTast(SplashScreenActivity.this);
                                ct.showCustomAlert("Data From Server Delayed",R.drawable.disconnect);
                            }

                        }else{
                            customDialog.cancel();
                            CustomTast ct=new CustomTast(SplashScreenActivity.this);
                            ct.showCustomAlert("Data From Server Delayed",R.drawable.disconnect);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customDialog.cancel();

                        CustomTast ct=new CustomTast(SplashScreenActivity.this);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                });
        releaseStringRequest = new StringRequest(Release_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Uri  uri =ParseRLIJSON(response);
                        if (response!=null) {
                            if (ContentUris.parseId(uri)>0);

                            customDialog.cancel();
                            startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));

                            finish();
                        }
                        else{
                            customDialog.cancel();
                            CustomTast ct=new CustomTast(SplashScreenActivity.this);
                            ct.showCustomAlert("",R.drawable.disconnect);
                        }

                        }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomTast ct=new CustomTast(SplashScreenActivity.this);
                        ct.showCustomAlert("Pulse Connection",R.drawable.disconnect);
                        //Toast.makeText(ActiveReleasesSummery.this,"Please Check Pulse Connectivity", Toast.LENGTH_LONG).show();
                    }
                });
        activerelaesStringRequest = new StringRequest(AcriveRelease_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Uri  uri =ParseActiveRelJSON(response);
                        if (response!=null) {
                            if (ContentUris.parseId(uri)>0);

                            customDialog.cancel();
                            startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));

                            finish();
                        }
                        else{
                            customDialog.cancel();
                            CustomTast ct=new CustomTast(SplashScreenActivity.this);
                            ct.showCustomAlert("",R.drawable.disconnect);
                        }

                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomTast ct=new CustomTast(SplashScreenActivity.this);
                        ct.showCustomAlert("Pulse Connection",R.drawable.disconnect);
                        //Toast.makeText(ActiveReleasesSummery.this,"Please Check Pulse Connectivity", Toast.LENGTH_LONG).show();
                    }
                });
        JdiAppController.getInstance().addToRequestQueue(npiviewStringRequest);
        JdiAppController.getInstance().addToRequestQueue(releaseStringRequest);
        JdiAppController.getInstance().addToRequestQueue(activerelaesStringRequest);
    }
    private Uri ParseJSON(String jsonStr) {
        // Log.i(Tag, jsonStr);
        Uri  uri=null;

        if (jsonStr != null) {
            try {

                JSONObject obj = new JSONObject(jsonStr);

                String  result = obj.getString("status");
                if (result.equalsIgnoreCase("ok")) {
                    // Log.i(Tag,"inside ok");
                    //JSONObject m_jArry_object = obj.getJSONObject("data");

                    ContentValues values = new ContentValues();
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API, jsonStr);
                      uri = getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI, values);



                } else {
                   // Log.e(TAG, "Couldn't get response from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                    /*Toast.makeText(getApplicationContext(),
                            "Please Connect With Your Pulse Properly", Toast.LENGTH_LONG).show();*/
                            CustomTast ct = new CustomTast(SplashScreenActivity.this);
                            ct.showCustomAlert("Data From Server Delayed",R.drawable.warning);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        return uri;
    }
    private Uri ParseRLIJSON(String jsonStr) {
        // Log.i(Tag, jsonStr);
        Uri  uri=null;

        if (jsonStr != null) {
            try {

                JSONObject obj = new JSONObject(jsonStr);

                String  result = obj.getString("status");
                if (result.equalsIgnoreCase("ok")) {
                    // Log.i(Tag,"inside ok");
                    //JSONObject m_jArry_object = obj.getJSONObject("data");

                    ContentValues values = new ContentValues();
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_PR_SUMMARY, jsonStr);
                    uri = getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_PR_SUMMARY_URI, values);
                    //JSONArray  m_jArry=m_jArry_object.getJSONArray("activerelsummary");



                } else {
                    // Log.e(TAG, "Couldn't get response from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                    /*Toast.makeText(getApplicationContext(),
                            "Please Connect With Your Pulse Properly", Toast.LENGTH_LONG).show();*/
                            CustomTast ct = new CustomTast(SplashScreenActivity.this);
                            ct.showCustomAlert("Data From Server Delayed",R.drawable.warning);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        return uri;
    }
    private Uri ParseActiveRelJSON(String jsonStr) {
        // Log.i(Tag, jsonStr);
        Uri  uri=null;

        if (jsonStr != null) {
            try {

                JSONObject obj = new JSONObject(jsonStr);
               // Log.i(TAG,jsonStr+"before status");
                String  result = obj.getString("status");
                if (result.equalsIgnoreCase("ok")) {
                   //  Log.i(TAG,"inside ok");
                   // Log.i(TAG,"jsonStr");


                    ContentValues values = new ContentValues();
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_LAST_SYNCH, jsonStr);
                    uri = getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_LAST_SYNCH_URI, values);




                } else {
                    // Log.e(TAG, "Couldn't get response from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            CustomTast ct = new CustomTast(SplashScreenActivity.this);
                            ct.showCustomAlert("Data From Server Delayed",R.drawable.warning);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        return uri;
    }
    private class NPITrackerAsychtask extends AsyncTask<Void,Void,Uri> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Uri doInBackground(Void... voids) {
             HttpHandler sh = new HttpHandler();
            Uri uri=null;
           // npiTrackerModels.clear();
            //entries.clear();
            String jsonStr = Util.loadJSONFromAsset(SplashScreenActivity.this,"npilists.json");
           // String jsonStr= sh.makeServiceCall(ActiveRel_API);
            //  Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject obj = new JSONObject(jsonStr);

                    String result = obj.getString("status");
                    if (result.equalsIgnoreCase("ok")){

                        ContentValues values = new ContentValues();
                        values.put(NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API, jsonStr);
                        uri = getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI, values);
                        Log.i(TAG, values.toString());
                    }
                    else {
                        // Log.e(TAG, "Couldn't get response from server.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                CustomTast ct=new CustomTast(SplashScreenActivity.this);
                                ct.showCustomAlert("Server Down Try Later",R.drawable.disconnect);
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                // Log.e(TAG, "Couldn't get response from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        CustomTast ct=new CustomTast(SplashScreenActivity.this);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                });

            }

            return uri;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            if (uri!=null){
                if (ContentUris.parseId(uri)>0);

                //Toast.makeText(SplashScreenActivity.this,"data inserted",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashScreenActivity.this,NPITrackerMainActivity.class));
                //startActivity(new Intent(SplashScreenActivity.this,NPITrackerPhaseActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }else{
                CustomTast ct=new CustomTast(SplashScreenActivity.this);
                ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
            }


        }

    }
    public void dropTable(){
        int result= this.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_URI,null,null);
        int result1= this.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI,null,null);
        int result2= this.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_LAST_SYNCH_URI,null,null);
        int result3= this.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_PR_SUMMARY_URI,null,null);
        int result4= this.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_RLI_STATUS_URI,null,null);
        int result5= this.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_SINGLE_API_URI,null,null);
       // int result6= this.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI,null,null);
        if (result!=0){
           // Log.i(TAG,"rows affected"+result);
        }
        if (result1!=0){
           // Log.i(TAG,"rows affected"+result1);
        }
        if (result2!=0){
           // Log.i(TAG,"rows affected"+result2);
        }
        if (result3!=0){
           // Log.i(TAG,"rows affected"+result);
        }
        if (result4!=0){
           // Log.i(TAG,"rows affected"+result1);
        }
        if (result5!=0){
          //  Log.i(TAG,"rows affected"+result2);
        }
       /* if (result6!=0){
           // Log.i(TAG,"rows affected"+result2);
        }*/
    }
}
