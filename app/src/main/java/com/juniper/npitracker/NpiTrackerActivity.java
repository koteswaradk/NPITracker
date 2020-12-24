package com.juniper.npitracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.juniper.npitracker.util.CustomTast;
import com.juniper.npitracker.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NpiTrackerActivity extends AppCompatActivity {
    ListView listView;
    private ArrayList<NPITrackerModel> npiTrackerModels = new ArrayList<NPITrackerModel>();
    NPITrackerAdapter adapter;
    String lasysync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npi_tracker);

        listView=(ListView)findViewById(R.id.npilistview);
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new NPITrackerAsychtask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    private class NPITrackerAsychtask extends AsyncTask<Void,Void,ArrayList<NPITrackerModel>> {

        @Override
        protected ArrayList<NPITrackerModel> doInBackground(Void... voids) {
           // HttpHandler sh = new HttpHandler();
            npiTrackerModels.clear();
            //entries.clear();
             String jsonStr = Util.loadJSONFromAsset(NpiTrackerActivity.this,"npi.json");
            //String jsonStr= sh.makeServiceCall(ActiveRel_API);
            //  Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject obj = new JSONObject(jsonStr);

                    String result = obj.getString("status");
                    lasysync=obj.getString("sync_time");
                    if (result.equalsIgnoreCase("ok")){
                        JSONArray m_jArry = obj.getJSONArray("npilistp1");

                        for (int i = 0; i < m_jArry.length(); i++) {
                            JSONObject jo_inside = m_jArry.getJSONObject(i);

                            NPITrackerModel model = new NPITrackerModel();

                            model.setNpiname(jo_inside.getString("npiid"));
                            //  Log.i(TAG,model.getRelname());
                            model.setSwlead(jo_inside.getString("swlead"));
                            // Log.i(TAG,model.getFirstpass());
                            model.setPlm(jo_inside.getString("plm"));
                            //  Log.i(TAG,model.getOverallpas());
                            model.setTestlead(jo_inside.getString("testlead"));
                            // Log.i(TAG,model.getOpenblocker());
                            npiTrackerModels.add(model);

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
                        CustomTast ct=new CustomTast(NpiTrackerActivity.this);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                });

            }

            return npiTrackerModels;
        }

        @Override
        protected void onPostExecute(ArrayList<NPITrackerModel> npiModels) {
            super.onPostExecute(npiModels);
            // Log.i(Tag,"onpost before new SchdulerAdapter(Scheduler.this,schedulerModels)");
            adapter=new NPITrackerAdapter(NpiTrackerActivity.this,npiModels);
            listView.setAdapter(adapter);
            // Log.i(Tag,"onpost beforeadapter.notifyDataSetChanged");
            adapter.notifyDataSetChanged();
            // Log.i(Tag,"onpost after.notifyDataSetChanged");
        }

    }
}
