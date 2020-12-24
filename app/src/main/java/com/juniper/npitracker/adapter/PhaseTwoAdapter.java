package com.juniper.npitracker.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.juniper.npitracker.JdiAppController;
import com.juniper.npitracker.NPIDetailsActivity;
import com.juniper.npitracker.R;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.model.NPIModel;
import com.juniper.npitracker.model.PhaseOneModel;
import com.juniper.npitracker.model.PhaseTwoModel;
import com.juniper.npitracker.util.ConnectionDetector;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;
import com.juniper.npitracker.util.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by koteswara on 06/02/17.
 */

public class PhaseTwoAdapter extends BaseAdapter implements Filterable {
    private static final String TAG= "NPITrackerAdapter";
    private LayoutInflater layoutInflater;
    public ArrayList<PhaseTwoModel> _alnpitracker;
    public ArrayList<PhaseTwoModel> orig;
    CustomDialog customDialog;
    ArrayList<String> checkedlist;
    HashMap hMap;
    ArrayList<String>allNpiId;
    boolean[] checkBoxState;
    HashMap<String,ArrayList<String>> all;
    StringRequest npiviewStringRequest;
    String ActiveRel_API ="http://rbu.juniper.net/mobile_apps/json_page3.php?npi=";
    Context npicontext;
    JdiAppController globalVariable;
    public PhaseTwoAdapter(Context context,ArrayList<PhaseTwoModel> alnpitracker ){
        this.npicontext=context;
        this._alnpitracker=alnpitracker;
        checkBoxState=new boolean[alnpitracker.size()];
        checkedlist=new ArrayList<>();
        hMap = new HashMap();
        all=new HashMap<String,ArrayList<String>>();
        globalVariable = (JdiAppController) npicontext.getApplicationContext();
        all=new HashMap<String,ArrayList<String>>();

        allNpiId=new ArrayList<>();
        Cursor cursor = npicontext.getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_USER_SELECTED_NPI_URI, null, null, null, null, null);
        if (cursor.moveToFirst()) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                allNpiId.add(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPI_ID)));
                Log.i(TAG,allNpiId+"");

                // do what you need with the cursor here
            }
        }
    }
    @Override
    public int getCount() {
        return _alnpitracker.size();
    }

    @Override
    public Object getItem(int position) {
        return _alnpitracker.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        PhaseTwoAdapter.ViewHolder viewHolder=null;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) npicontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.npitrackermain_row, null);
            viewHolder = new PhaseTwoAdapter.ViewHolder();

            viewHolder.checkbox=(CheckBox)convertView.findViewById(R.id.select);
            viewHolder.npiname=(TextView)convertView.findViewById(R.id.npi_name);
            viewHolder.swlead=(TextView)convertView.findViewById(R.id.npiswlead_display);
            viewHolder.plm=(TextView)convertView.findViewById(R.id.npiplm_display);
            viewHolder.testlead=(TextView)convertView.findViewById(R.id.npi_testlead_display);
            viewHolder._linearLayout=(LinearLayout)convertView.findViewById(R.id.npi_id);
            viewHolder.cardView = (CardView) convertView.findViewById(R.id.card_viewnpimain);
            viewHolder.statusLinearLayout = (LinearLayout) convertView.findViewById(R.id.color_layout_l2);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (PhaseTwoAdapter.ViewHolder) convertView.getTag();
        }
        final PhaseTwoModel activeReleModelItem = (PhaseTwoModel) _alnpitracker.get(position);
        // View rowView = convertView;

        viewHolder.npiname.setText(activeReleModelItem.getNpiname());
        viewHolder.swlead.setText(activeReleModelItem.getSwlead());
        viewHolder.plm.setText(activeReleModelItem.getPlm());
        viewHolder.testlead.setText(activeReleModelItem.getTestlead());
        viewHolder.checkbox.setChecked(checkBoxState[position]);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Log.i(TAG,activeReleModelItem.getNpiname());
                Log.i(TAG,activeReleModelItem.getPlm());*/
               // Log.i(TAG,activeReleModelItem.getNpiid());
                ConnectionDetector conn = new ConnectionDetector(npicontext.getApplicationContext());
                if(conn.isConnectingToInternet()) {

                    dropTable();
                   // new PhaseTwoAdapter.NPIDetailsAsychtask().execute(activeReleModelItem.getNpiid());
                    sendRequest(activeReleModelItem.getNpiid(), activeReleModelItem.getPlm(), activeReleModelItem.getSwlead(), activeReleModelItem.getTestlead());

                }else{
                    CustomTast ct=new CustomTast(npicontext);
                    ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                }

            }
        });

        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    checkBoxState[position] = true;
                    hMap.put(activeReleModelItem.getNpiid(),activeReleModelItem.getNpiname());
                  /*  checkedlist.add(activeReleModelItem.getNpiname());
                    checkedlist.add(activeReleModelItem.getSwlead());
                    checkedlist.add(activeReleModelItem.getPlm());
                    checkedlist.add(activeReleModelItem.getTestlead());
                     checkedlist.add(activeReleModelItem.getNpiid());*/
                    checkedlist.add(activeReleModelItem.getNpiid());

                    all.put(activeReleModelItem.getNpiid(),checkedlist);
                    ContentValues values = new ContentValues();
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPINAME,activeReleModelItem.getNpiname());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPI_ID, activeReleModelItem.getNpiid());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_SWLEAD, activeReleModelItem.getSwlead());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_PLM, activeReleModelItem.getPlm());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_STATUS, activeReleModelItem.getStatus());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_TL, activeReleModelItem.getTestlead());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_PHASE, "P2");
                    Uri uri = npicontext.getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_USER_SELECTED_NPI_URI, values);
                    if (uri!=null){
                        if (ContentUris.parseId(uri)>0);
                       /* CustomTast ct=new CustomTast(npicontext);
                        ct.showCustomAlert("Inseted",R.drawable.disconnect);*/

                    }else{
                       /* CustomTast ct=new CustomTast(npicontext);
                        ct.showCustomAlert("not inserted",R.drawable.disconnect);*/

                    }

                }
                else {
                    checkBoxState[position] = false;
                    checkedlist.remove(activeReleModelItem.getNpiid());
                    hMap.remove(activeReleModelItem.getNpiid());

                    all.remove(activeReleModelItem.getNpiid());
                    //String npiid=activeReleModelItem.getNpiid();

                    String[] selectionArgs = new String[]{ activeReleModelItem.getNpiid() };


                    int result= npicontext.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_USER_SELECTED_NPI_URI, NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPI_ID+" =?", selectionArgs);
                    if (result!=0){
                        Log.i(TAG,"rows affected"+result);
                    }

                }
                globalVariable.setNpiid(checkedlist);
                globalVariable.setNpikeyandid(hMap);
            }
        });
        if (activeReleModelItem.getStatus().equalsIgnoreCase("Critical")){
            viewHolder.statusLinearLayout.setBackgroundColor(ContextCompat.getColor(npicontext, R.color.j_aspiration));

        }
        if (activeReleModelItem.getStatus().equalsIgnoreCase("On Track")){
            viewHolder.statusLinearLayout.setBackgroundColor(ContextCompat.getColor(npicontext, R.color.j_excellence));

        }
        if (activeReleModelItem.getStatus().equalsIgnoreCase("At Risk")){
            viewHolder.statusLinearLayout.setBackgroundColor(ContextCompat.getColor(npicontext, R.color.j_authetic));

        }
        if (activeReleModelItem.getStatus().equalsIgnoreCase("no_status")){
           // viewHolder.statusLinearLayout.setBackgroundColor(ContextCompat.getColor(npicontext, R.color.j_trust));
            viewHolder.statusLinearLayout.setBackgroundResource(R.drawable.square_border);

        }
       /* ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        int color = generator.getColor(activeReleModelItem.getNpiname());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(activeReleModelItem.getNpiname().charAt(0)), color);

        viewHolder.image.setImageDrawable(drawable);*/
        if(checkSelected(activeReleModelItem.getNpiid())){
            viewHolder.checkbox.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.checkbox.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
        return convertView;
    }
    private void dropTable() {
        int result= npicontext.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI,null,null);
        if (result!=0){
            //Log.i(TAG,"rows affected"+result);
        }
    }
    public boolean checkSelected(String npiId){

        for(String npi: allNpiId){
            if(npiId.equalsIgnoreCase(npi)){
                return true;
            }
        }

        return false;
    }
    private void sendRequest(final String s_npiid, final String plm, final String swLead, final String testLead) {

        customDialog=new CustomDialog(npicontext,"Loading...");
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();


        npiviewStringRequest = new StringRequest(ActiveRel_API+s_npiid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                         Log.i(TAG,s_npiid);
                        try {

                            JSONObject obj = new JSONObject(response);

                            String result = obj.getString("status");
                            if (result.equalsIgnoreCase("ok")){

                                ContentValues values = new ContentValues();
                                values.put(NPITrackerDBHelper.KEY_NPITRACKER_TEST_STATUS_API, response);

                                if(!obj.isNull("data")) {
                                    Uri uri = npicontext.getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI, values);
                                    if (uri != null) {
                                        if (ContentUris.parseId(uri) > 0) ;

                                        customDialog.cancel();
                                        Intent intent = new Intent(npicontext, NPIDetailsActivity.class);
                                        // intent.putExtra("releasenumber",activeReleModelItem.getNpiname());
                                        intent.putExtra("NPI_Id", s_npiid);
                                        npicontext.startActivity(intent);

                                    } else {
                                        customDialog.cancel();
                                        CustomTast ct = new CustomTast(npicontext);
                                        ct.showCustomAlert("Server Down", R.drawable.disconnect);
                                    }
                                }
                                else{
                                    // create alert dialog
                                    customDialog.cancel();
                                    open(plm, swLead, testLead);
                                }

                            }
                            else {
                                customDialog.cancel();
                                CustomTast ct=new CustomTast(npicontext);
                                ct.showCustomAlert("Server Down",R.drawable.disconnect);

                            }

                        } catch (JSONException e) {
                            customDialog.cancel();
                            CustomTast ct=new CustomTast(npicontext);
                            ct.showCustomAlert("no data for selected npi",R.drawable.disconnect);
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customDialog.cancel();

                        CustomTast ct=new CustomTast(npicontext);
                        ct.showCustomAlert("Server Delay",R.drawable.disconnect);
                    }
                });
        npiviewStringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        JdiAppController.getInstance().addToRequestQueue(npiviewStringRequest);

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<PhaseTwoModel> results = new ArrayList<PhaseTwoModel>();
                if (orig == null)
                    orig = _alnpitracker;
                if (charSequence != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final PhaseTwoModel g : orig) {
                            if (g.getNpiname().toLowerCase()
                                    .contains(charSequence.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                _alnpitracker = (ArrayList<PhaseTwoModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    static class ViewHolder{
        LinearLayout _linearLayout;
        CheckBox checkbox;
        TextView npiname;
        TextView swlead;
        TextView plm;
        TextView testlead;
        CardView cardView;
        LinearLayout statusLinearLayout;

    }

    public void open(String plm, String swLead, String testLead){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                npicontext);

        String message = "No RLIs tagged to the selected NPI.";

        if(!plm.isEmpty() || !swLead.isEmpty() || !testLead.isEmpty()){
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
        }


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
