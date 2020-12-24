package com.juniper.npitracker.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.juniper.npitracker.JdiAppController;
import com.juniper.npitracker.NPIDetailsActivity;
import com.juniper.npitracker.R;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.util.ConnectionDetector;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by koteswara on 15/02/17.
 */

public class SelectedNPICursorAdapter extends BaseAdapter {
    String TAG=getClass().getSimpleName();
    Cursor cursor;
    Context mContext;
    CustomDialog customDialog;
    String ActiveRel_API ="http://rbu.juniper.net/mobile_apps/json_page3.php?npi=";
    //private static final String[] DETAIL_COLUMNS = {JDIDBHelper.KEY_RELEASENAME,JDIDBHelper.KEY_OVERALLPASS};
    private LayoutInflater layoutInflater;
    StringRequest npiviewStringRequest;
    public SelectedNPICursorAdapter(Context context, Cursor cursor){
        this.mContext=context;
        this.cursor = cursor;
        Log.i("inside adapter","constructo"+cursor);
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        SelectedNPICursorAdapter.ViewHolder viewHolder=null;
        cursor.moveToPosition(position);

        Log.i("inside cadapter","After inflater"+position);
        if(convertView == null){
            viewHolder = new SelectedNPICursorAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.selected_npi_row, null);

            viewHolder.selecteddnpiname=(TextView)convertView.findViewById(R.id.s_npi_name);
            viewHolder.selectedswlead=(TextView)convertView.findViewById(R.id.s_npiswlead_display);
            viewHolder.selectedplm=(TextView)convertView.findViewById(R.id.s_npiplm_display);
            viewHolder.selectedtestlead=(TextView)convertView.findViewById(R.id.s_npi_testlead_display);
            viewHolder._linearLayout=(RelativeLayout)convertView.findViewById(R.id.s_npi_id);



            viewHolder.selecteddnpiname.setText(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPINAME)));
            viewHolder.selectedswlead.setText(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_SWLEAD)));
            viewHolder.selectedplm.setText(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PLM)));
            viewHolder.selectedtestlead.setText(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_TL)));

            viewHolder._linearLayout.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPI_ID))));



            viewHolder._linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   TextView cnpiid =(TextView) view.findViewById(R.id.s_npi_name);
                    Log.i(TAG,cnpiid.getText().toString());
                    ConnectionDetector conn = new ConnectionDetector(mContext.getApplicationContext());
                    if(conn.isConnectingToInternet()) {

                        dropTable();
                        // new NPIDetailsAsychtask().execute(activeReleModelItem.getNpiid());
                        sendRequest(cnpiid.getText().toString());

                    }else{
                        CustomTast ct=new CustomTast(mContext);
                        ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                    }
                   /* if(view.getId() == R.id.relmain_id){
                        Log.e("Delete a record: ", "Item Id: "+ " position: "+position);
                    }
                    // String one= cursor.getString(cursor.getColumnIndex(JDIDBHelper.KEY_RELEASENAME));
                    //  Log.i("inside cursor adapter","on click");
                    // String[] selectionArgs = new String[]{ position };
                    Cursor cursorConversations = mContext.getContentResolver().query(ActiveReleaseProvider.CONTENT_RELEASE_URI, DETAIL_COLUMNS, JDIDBHelper.KEY_ID+" =?", new String[]{Integer.toString(position+1)}, null);
                    // Log.i("inside cursor adapter",one);
                    if (cursorConversations.moveToFirst()) {
                        do{
                            String s_relname= cursorConversations.getString(cursorConversations.getColumnIndex(JDIDBHelper.KEY_RELEASENAME));
                            String s_overallpass= cursorConversations.getString(cursorConversations.getColumnIndex(JDIDBHelper.KEY_OVERALLPASS));

                            Log.i("","perticular value "+s_relname);
                            Intent intent = new Intent(mContext,TestReport.class);
                            intent.putExtra("releasenumber",s_relname);
                            intent.putExtra("overallpass",s_overallpass);
                            mContext.startActivity(intent);

                        } while (cursorConversations.moveToNext());
                    }*/

                }
            });
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (SelectedNPICursorAdapter.ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    private void dropTable() {
        int result= mContext.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI,null,null);
        if (result!=0){
            //  Log.i(TAG,"rows affected"+result);
        }
    }
    private void sendRequest(final String s_npiid) {

        customDialog=new CustomDialog(mContext,"Loading...");
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();


        npiviewStringRequest = new StringRequest(ActiveRel_API+s_npiid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Log.i(TAG,response);
                        try {

                            JSONObject obj = new JSONObject(response);

                            String result = obj.getString("status");
                            if (result.equalsIgnoreCase("ok")){

                                ContentValues values = new ContentValues();
                                values.put(NPITrackerDBHelper.KEY_NPITRACKER_TEST_STATUS_API, response);

                                Uri uri = mContext.getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI, values);
                                if (uri!=null){
                                    if (ContentUris.parseId(uri)>0);

                                    customDialog.cancel();
                                    Intent intent=new Intent(mContext,NPIDetailsActivity.class);
                                    // intent.putExtra("releasenumber",activeReleModelItem.getNpiname());
                                    intent.putExtra("NPI_Id",s_npiid);
                                    mContext.startActivity(intent);

                                }else{
                                    customDialog.cancel();
                                    CustomTast ct=new CustomTast(mContext);
                                    ct.showCustomAlert("Server Down",R.drawable.disconnect);
                                }

                            }
                            else {
                                customDialog.cancel();
                                CustomTast ct=new CustomTast(mContext);
                                ct.showCustomAlert("Server Down",R.drawable.disconnect);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customDialog.cancel();

                        CustomTast ct=new CustomTast(mContext);
                        ct.showCustomAlert("Server Delay",R.drawable.disconnect);
                    }
                });
        npiviewStringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        JdiAppController.getInstance().addToRequestQueue(npiviewStringRequest);

    }
    static class ViewHolder{

        RelativeLayout _linearLayout;
        TextView selecteddnpiname;
        TextView selectedswlead;
        TextView selectedplm;
        TextView selectedtestlead;

    }
}
