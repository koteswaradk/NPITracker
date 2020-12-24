package com.juniper.npitracker.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


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

import com.juniper.npitracker.model.PhaseThreeModel;
import com.juniper.npitracker.model.SelectedNPIModel;
import com.juniper.npitracker.util.ConnectionDetector;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by koteswara on 14/02/17.
 */

public class SelectedNPIAdapter extends BaseAdapter implements Filterable {

    private static final String TAG= "NPITrackerAdapter";
    private LayoutInflater layoutInflater;
    public ArrayList<SelectedNPIModel> _alselectednpi;
    Context sNpiContext;
    public ArrayList<SelectedNPIModel> orig;
    CustomDialog customDialog;
    StringRequest npiviewStringRequest;

    String ActiveRel_API ="http://rbu.juniper.net/mobile_apps/json_page3.php?npi=";
    public SelectedNPIAdapter(Context context,ArrayList<SelectedNPIModel> alselected){
        sNpiContext=context;
        this._alselectednpi=alselected;

    }

    @Override
    public int getCount() {
        return _alselectednpi.size();
    }

    @Override
    public Object getItem(int position) {
        return _alselectednpi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        SelectedNPIAdapter.ViewHolder viewHolder=null;

        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) sNpiContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.selected_npi_row, null);
            viewHolder = new SelectedNPIAdapter.ViewHolder();

            viewHolder.selecteddnpiname=(TextView)convertView.findViewById(R.id.s_npi_name);
            viewHolder.selectedswlead=(TextView)convertView.findViewById(R.id.s_npiswlead_display);
            viewHolder.selectedplm=(TextView)convertView.findViewById(R.id.s_npiplm_display);
            viewHolder.selectedtestlead=(TextView)convertView.findViewById(R.id.s_npi_testlead_display);
            viewHolder._linearLayout=(CardView)convertView.findViewById(R.id.s_npi_id);
            viewHolder.phase=(TextView)convertView.findViewById(R.id.t_phase);
            viewHolder.statusLinearLayout = (LinearLayout) convertView.findViewById(R.id.color_layout_l2);
            viewHolder.checkbox=(CheckBox)convertView.findViewById(R.id.deselect);
            //viewHolder.phasetext2=(TextView)convertView.findViewById(R.id.t_phase2);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (SelectedNPIAdapter.ViewHolder) convertView.getTag();
        }
        final SelectedNPIModel activeReleModelItem = (SelectedNPIModel) _alselectednpi.get(position);
        // View rowView = convertView;

        viewHolder.selecteddnpiname.setText(activeReleModelItem.getNpiname());
        viewHolder.selectedswlead.setText(activeReleModelItem.getSwlead());
        viewHolder.selectedplm.setText(activeReleModelItem.getPlm());
        viewHolder.selectedtestlead.setText(activeReleModelItem.getTestlead());
       // String str=activeReleModelItem.getPhase();
        //viewHolder.phase.setText(str.substring(0,3));
        viewHolder.phase.setText(activeReleModelItem.getPhase());
        Log.i(TAG,activeReleModelItem.getPhase());
        viewHolder._linearLayout.setId(Integer.parseInt(activeReleModelItem.getNpiid()));

       /* RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(sNpiContext, R.anim.textvertical);
        ranim.setFillAfter(true); //For the textview to remain at the same place after the rotation
        viewHolder.phasetext2.setAnimation(ranim);*/

        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                   // activeReleModelItem.getNpiid();
                  //  Log.i(TAG,activeReleModelItem.getNpiid());
                    String[] selectionArgs = new String[]{ activeReleModelItem.getNpiid() };
                    int result= sNpiContext.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_USER_SELECTED_NPI_URI, NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPI_ID+" =?", selectionArgs);
                    if (result!=0){
                      //  Log.i(TAG,"rows affected"+result);
                    }
                }if (b){
                    ContentValues values = new ContentValues();
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPINAME,activeReleModelItem.getNpiname());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPI_ID, activeReleModelItem.getNpiid());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_SWLEAD, activeReleModelItem.getSwlead());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_PLM, activeReleModelItem.getPlm());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_STATUS, activeReleModelItem.getStatus());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_PHASE, activeReleModelItem.getPhase());
                    values.put(NPITrackerDBHelper.KEY_NPITRACKER_TL, activeReleModelItem.getTestlead());
                    Uri uri = sNpiContext.getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_USER_SELECTED_NPI_URI, values);
                    if (uri!=null){
                        if (ContentUris.parseId(uri)>0);
                       /* CustomTast ct=new CustomTast(npicontext);
                        ct.showCustomAlert("Inserted",R.drawable.disconnect);*/

                    }else{
                       /* CustomTast ct=new CustomTast(npicontext);
                        ct.showCustomAlert("not inserted",R.drawable.disconnect);*/

                    }
                }
            }
        });
        viewHolder._linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Log.i(TAG,activeReleModelItem.getNpiname());
               // Log.i(TAG,activeReleModelItem.getPlm());*/
               //  Log.i(TAG,activeReleModelItem.getNpiid());
                ConnectionDetector conn = new ConnectionDetector(sNpiContext.getApplicationContext());
                if(conn.isConnectingToInternet()) {

                    dropTable();
                    //new PhaseThreeAdapter.NPIDetailsAsychtask().execute(activeReleModelItem.getNpiid());
                    sendRequest(activeReleModelItem.getNpiid());
                }else{
                    CustomTast ct=new CustomTast(sNpiContext);
                    ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                }

            }
        });
        if (activeReleModelItem.getStatus().equalsIgnoreCase("Critical")){
            viewHolder.statusLinearLayout.setBackgroundColor(ContextCompat.getColor(sNpiContext, R.color.j_aspiration));

        }
        if (activeReleModelItem.getStatus().equalsIgnoreCase("On Track")){
            viewHolder.statusLinearLayout.setBackgroundColor(ContextCompat.getColor(sNpiContext, R.color.j_excellence));

        }
        if (activeReleModelItem.getStatus().equalsIgnoreCase("At Risk")){
            viewHolder.statusLinearLayout.setBackgroundColor(ContextCompat.getColor(sNpiContext, R.color.j_authetic));

        }
        if (activeReleModelItem.getStatus().equalsIgnoreCase("no_status")){
           // viewHolder.statusLinearLayout.setBackgroundColor(ContextCompat.getColor(sNpiContext, R.color.transparent));
            //viewHolder.statusLinearLayout.setBackground(sNpiContext.getResources().getDrawable(R.drawable.square_border));
            viewHolder.statusLinearLayout.setBackgroundResource(R.drawable.square_border);

        }
        /*ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        int color = generator.getColor(activeReleModelItem.getNpiname());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(activeReleModelItem.getNpiname().charAt(0)), color);

        viewHolder.image.setImageDrawable(drawable);*/

        notifyDataSetChanged();
        return convertView;
    }
    private void dropTable() {
        int result= sNpiContext.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI,null,null);
        if (result!=0){
            //  Log.i(TAG,"rows affected"+result);
        }
    }
    private void sendRequest(final String s_npiid) {

        customDialog=new CustomDialog(sNpiContext,"Loading...");
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
                                if(!obj.isNull("data")) {
                                    Uri uri = sNpiContext.getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_TEST_STATUS_URI, values);
                                    if (uri != null) {
                                        if (ContentUris.parseId(uri) > 0) ;

                                        customDialog.cancel();
                                        Intent intent = new Intent(sNpiContext, NPIDetailsActivity.class);
                                        // intent.putExtra("releasenumber",activeReleModelItem.getNpiname());
                                        intent.putExtra("NPI_Id", s_npiid);
                                        Log.i(TAG,s_npiid);
                                        sNpiContext.startActivity(intent);

                                    } else {
                                        customDialog.cancel();
                                        CustomTast ct = new CustomTast(sNpiContext);
                                        ct.showCustomAlert("Server Down", R.drawable.disconnect);
                                    }
                                }else {
                                    customDialog.cancel();
                                    open();
                                }

                            }


                            else {
                                customDialog.cancel();
                                CustomTast ct=new CustomTast(sNpiContext);
                                ct.showCustomAlert("Server Down",R.drawable.disconnect);

                            }

                        } catch (JSONException e) {
                            customDialog.cancel();
                            CustomTast ct=new CustomTast(sNpiContext);
                            ct.showCustomAlert("no data for selected npi",R.drawable.disconnect);
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customDialog.cancel();

                        CustomTast ct=new CustomTast(sNpiContext);
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
                final ArrayList<SelectedNPIModel> results = new ArrayList<SelectedNPIModel>();
                if (orig == null)
                    orig = _alselectednpi;
                if (charSequence != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final SelectedNPIModel g : orig) {
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
                _alselectednpi = (ArrayList<SelectedNPIModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder{
        CheckBox checkbox;
        CardView _linearLayout;
        TextView selecteddnpiname;
        TextView selectedswlead;
        TextView selectedplm;
        TextView selectedtestlead;
        TextView phase;
        TextView phasetext2;
        LinearLayout statusLinearLayout;

    }
    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                sNpiContext);

        // set dialog message
        alertDialogBuilder
                .setMessage("No RLIs available for given NPI.")
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
