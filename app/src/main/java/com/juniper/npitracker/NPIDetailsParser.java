package com.juniper.npitracker;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.juniper.npitracker.adapter.PhaseOneAdapter;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.model.NPIDetailsModel;
import com.juniper.npitracker.util.ConnectionDetector;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;
import com.juniper.npitracker.util.HttpHandler;
import com.juniper.npitracker.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by sarahcs on 2/8/2017.
 */

public class NPIDetailsParser {
    String TAG=getClass().getSimpleName();
    private Context mContext;
    private String NPIId;
    CustomDialog customDialog;
    public NPIDetailsParser(Context mContext, String NPIId){
        this.mContext = mContext;
        this.NPIId = NPIId;

    }
    /*private void dBoperation(){
        ConnectionDetector conn = new ConnectionDetector(mContext.getApplicationContext());

        if(conn.isConnectingToInternet()) {

            new NPIDetailsAsychtask().execute();
            //sendRequest();
        }else{
            CustomTast ct=new CustomTast(mContext);
            ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        }
    }*/

   /* private void dropTable() {
        int result= mContext.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI,null,null);
        if (result!=0){
            Log.i(TAG,"rows affected"+result);
        }
    }*/
   /* private class NPIDetailsAsychtask extends AsyncTask<Void,Void,Uri> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialog=new CustomDialog(mContext,"Loading...");
            customDialog.setCanceledOnTouchOutside(false);
            customDialog.show();
        }

        @Override
        protected Uri doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            Uri uri=null;
            // npiTrackerModels.clear();
            //entries.clear();
          //  String jsonStr = Util.loadJSONFromAsset(mContext,"NPIDetails.json");
            // String jsonStr= sh.makeServiceCall(ActiveRel_API);
            //  Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject obj = new JSONObject(jsonStr);

                    String result = obj.getString("status");
                    if (result.equalsIgnoreCase("ok")){
                        dropTable();
                        ContentValues values = new ContentValues();
                        values.put(NPITrackerDBHelper.KEY_NPITRACKER_TEST_STATUS_API, jsonStr);
                        uri = mContext.getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI, values);
                        Log.i(TAG, values.toString());
                    }
                    else {
                        // Log.e(TAG, "Couldn't get response from server.");
                        CustomTast ct=new CustomTast(mContext);
                        ct.showCustomAlert("Server Down Try Later",R.drawable.disconnect);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                // Log.e(TAG, "Couldn't get response from server.");
                CustomTast ct=new CustomTast(mContext);
                ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);

            }

            return uri;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            if (uri!=null){
                if (ContentUris.parseId(uri)>0);
                customDialog.cancel();

            }else{
                customDialog.cancel();
                CustomTast ct=new CustomTast(mContext);
                ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
            }


        }

    }*/
    public NPIDetailsModel loadJSONFromAsset() {

        String json = null;
        NPIDetailsModel npiDetails = new NPIDetailsModel();;

       /* try {
            InputStream is = mContext.getAssets().open("NPIDetails.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
      //  dBoperation();
        Cursor cursor = mContext.getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                json= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_TEST_STATUS_API));
                Log.i(TAG+"string test",json);

            } while (cursor.moveToNext());
        }

        try {

            JSONObject obj = new JSONObject(json);

            Log.i(TAG,json);
            if(obj.getString("status").equals("ok")){



                JSONObject data = obj.getJSONObject("data");
                Iterator<?> keys = data.keys();

                if(keys.hasNext()){

                    String phase = (String)keys.next();

                    if ( data.get(phase) instanceof JSONObject ) {

                        JSONObject j_npiDetails = data.getJSONObject(phase).getJSONObject(NPIId);

                        Log.d("NPI Name", j_npiDetails.getString("synopsis"));
                        // JSON First Level
                        String jo_synposis = j_npiDetails.getString("synopsis");
                        String jo_status = j_npiDetails.getString("npi_status");
                        String jo_status_reason = j_npiDetails.getString("npi_status_reason");
                        String jo_releaseString = j_npiDetails.getString("release_str");
                        JSONObject jo_sysTest = j_npiDetails.getJSONObject("test_status");


                        npiDetails.setNPIId(NPIId);
                        npiDetails.setNPIName(jo_synposis);
                        npiDetails.setStatus(jo_status);
                        npiDetails.setStatusReason(jo_status_reason);
                        npiDetails.setVersionNumber(jo_releaseString);

                        // JSON Sys Test
                        npiDetails.setTestProgress(jo_sysTest.getInt("total_rlis"));
                        npiDetails.setActualExecuted(jo_sysTest.getInt("rsp_executed"));
                        npiDetails.setTotalExecution(jo_sysTest.getInt("rsp_total"));
                        npiDetails.setActualPassRate(jo_sysTest.getInt("rsp_pass"));
                        npiDetails.setActualScripted(jo_sysTest.getInt("rsp_scripted"));
                        npiDetails.setTotalScripted(jo_sysTest.getInt("rsp_scriptable"));
                        npiDetails.setExecutionPercentage(jo_sysTest.getInt("rsp_executed_percentage"));
                        npiDetails.setPassRatePercentage(jo_sysTest.getInt("rsp_pass_percentage"));
                        npiDetails.setScriptedPercentage(jo_sysTest.getInt("rsp_scripted_percentage"));
                        npiDetails.setRLIsAtRisk(jo_sysTest.getInt("at_risk"));
                        npiDetails.setRLIsMissingFSApproval(jo_sysTest.getInt("functional_spec_status"));
                        npiDetails.setRLIsMissingUTPApproval(jo_sysTest.getInt("unit_test_plan_status"));
                        npiDetails.setRLIsMissingFTPApproval(jo_sysTest.getInt("product_test_plan_status"));
                        npiDetails.setRLIsMissingFCCApproval(jo_sysTest.getInt("fcc_approval"));
                        npiDetails.setRLIsWithRejectedFCC(jo_sysTest.getInt("fcc_rejected"));

                        if( npiDetails.getRLIsAtRisk() != 0){
                            JSONObject j_RLIs = jo_sysTest.getJSONObject("rlis_at_risk_tree");

                            Iterator<?> RLIKeys = j_RLIs.keys();

                            while(RLIKeys.hasNext()){

                                String rli_key = (String)RLIKeys.next();
                                npiDetails.addRLIRisksToList(rli_key, j_RLIs.getJSONObject(rli_key).getString("Synopsis"),j_RLIs.getJSONObject(rli_key).getString("Reason"));

                            }
                        }

                        if( npiDetails.getRLIsMissingFSApproval() != 0){
                            JSONObject j_RLIs = jo_sysTest.getJSONObject("functional_spec_status_tree");

                            Iterator<?> RLIKeys = j_RLIs.keys();

                            while(RLIKeys.hasNext()){

                                String rli_key = (String)RLIKeys.next();
                                npiDetails.addRLIsMissingFSApprovalToList(rli_key, j_RLIs.getJSONObject(rli_key).getString("Synopsis"));

                            }
                        }

                        if( npiDetails.getRLIsMissingUTPApproval() != 0){
                            JSONObject j_RLIs = jo_sysTest.getJSONObject("unit_test_plan_status_tree");

                            Iterator<?> RLIKeys = j_RLIs.keys();

                            while(RLIKeys.hasNext()){

                                String rli_key = (String)RLIKeys.next();
                                npiDetails.addRLIsMissingUTPApprovalToList(rli_key, j_RLIs.getJSONObject(rli_key).getString("Synopsis"));

                            }
                        }

                        if( npiDetails.getRLIsMissingFTPApproval() != 0){
                            JSONObject j_RLIs = jo_sysTest.getJSONObject("product_test_plan_status_tree");

                            Iterator<?> RLIKeys = j_RLIs.keys();

                            while(RLIKeys.hasNext()){

                                String rli_key = (String)RLIKeys.next();
                                npiDetails.addRLIsMissingFTPApprovalToList(rli_key, j_RLIs.getJSONObject(rli_key).getString("Synopsis"));

                            }
                        }

                        if( npiDetails.getRLIsMissingFCCApproval() != 0){
                            JSONObject j_RLIs = jo_sysTest.getJSONObject("fcc_approval_tree");

                            Iterator<?> RLIKeys = j_RLIs.keys();

                            while(RLIKeys.hasNext()){

                                String rli_key = (String)RLIKeys.next();
                                npiDetails.addRLIsMissingFCCApprovalToList(rli_key, j_RLIs.getJSONObject(rli_key).getString("Synopsis"));

                            }
                        }

                        if( npiDetails.getRLIsWithRejectedFCC() != 0){
                            JSONObject j_RLIs = jo_sysTest.getJSONObject("fcc_rejected_tree");

                            Iterator<?> RLIKeys = j_RLIs.keys();

                            while(RLIKeys.hasNext()){

                                String rli_key = (String)RLIKeys.next();
                                npiDetails.addRLIsRejectedFCCToList(rli_key, j_RLIs.getJSONObject(rli_key).getString("Synopsis"));

                            }
                        }

                        // JSON PR Summary
                        if (!j_npiDetails.isNull("pr_summary")) {
                            JSONObject jo_prSummary = j_npiDetails.getJSONObject("pr_summary");
                            Iterator<?> pr_groups = jo_prSummary.keys();

                            while(pr_groups.hasNext()){

                                String groupName = (String)pr_groups.next();

                                JSONObject jo_severitySummaryObj = jo_prSummary.getJSONObject(groupName);

                                npiDetails.addPRSeverityToList(
                                        jo_severitySummaryObj.getString("group_name"),
                                        jo_severitySummaryObj.getInt("any_severity_open"),
                                        jo_severitySummaryObj.getInt("any_severity_verify"),
                                        jo_severitySummaryObj.getInt("any_severity_closed"),
                                        jo_severitySummaryObj.getInt("any_severity_total"),
                                        jo_severitySummaryObj.getInt("any_severity_last_7_days"),
                                        jo_severitySummaryObj.getInt("blocker_open"),
                                        jo_severitySummaryObj.getInt("blocker_verify"),
                                        jo_severitySummaryObj.getInt("il12_open"),
                                        jo_severitySummaryObj.getInt("il12_verify"),
                                        jo_severitySummaryObj.getInt("il34_open"),
                                        jo_severitySummaryObj.getInt("il34_verify")
                                );
                            }

                            JSONObject jo_customerPRs = j_npiDetails.getJSONObject("customer_prs");
                            npiDetails.setClBlocker(jo_customerPRs.getInt("cl_blocker"));
                            npiDetails.setClClosed(jo_customerPRs.getInt("cl_closed"));
                            npiDetails.setClNotClosed(jo_customerPRs.getInt("cl_not_closed"));
                            npiDetails.setCl1(jo_customerPRs.getInt("cl1"));
                            npiDetails.setCl2(jo_customerPRs.getInt("cl2"));
                            npiDetails.setCl3(jo_customerPRs.getInt("cl3"));
                            npiDetails.setCl4(jo_customerPRs.getInt("cl4"));
                        }


                    }
                }

                return npiDetails;

            }
            else{
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return npiDetails;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            CustomTast ct=new CustomTast(mContext);
            ct.showCustomAlert("no details for pr summary",R.drawable.warning);
            return npiDetails;
        }
    }
}
