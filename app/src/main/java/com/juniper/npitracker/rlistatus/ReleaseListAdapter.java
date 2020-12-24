package com.juniper.npitracker.rlistatus;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.juniper.npitracker.adapter.PhaseOneAdapter;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.model.NPIDetailsModel;
import com.juniper.npitracker.model.PhaseOneModel;
import com.juniper.npitracker.model.RLIRiskModel;
import com.juniper.npitracker.util.ConnectionDetector;
import com.juniper.npitracker.util.CustomDialog;
import com.juniper.npitracker.util.CustomTast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by koteswara on 15/02/17.
 */

public class ReleaseListAdapter extends BaseExpandableListAdapter {
    String TAG=getClass().getSimpleName();
    //    ArrayList<ReleaseListModel>rel_listmodel;
    //    Context rellistcontext;
    //    private LayoutInflater layoutInflater;
    CustomDialog customDialog;
    StringRequest rliistStringRequest;
    String s_rel_name;
    String ActiveRel_API="http://rbu.juniper.net/mobile_apps/RLI/read_json.php?release=";
    //    public ReleaseListAdapter(Context context,ArrayList<ReleaseListModel> relmodel){
    //        rel_listmodel=relmodel;
    //        rellistcontext=context;
    //    }
    //    @Override
    //    public int getCount() {
    //        return rel_listmodel.size();
    //    }
    //
    //    @Override
    //    public Object getItem(int position) {
    //        return rel_listmodel.get(position);
    //    }
    //
    //    @Override
    //    public long getItemId(int position) {
    //        return position;
    //    }
    //
    //    @Override
    //    public View getView(int position, View convertView, ViewGroup viewGroup) {
    //        ReleaseListAdapter.ViewHolder viewHolder=null;
    //        if (layoutInflater == null)
    //            layoutInflater = (LayoutInflater) rellistcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //        if(convertView == null){
    //            convertView = layoutInflater.inflate(R.layout.releaselist_row, null);
    //            viewHolder = new ReleaseListAdapter.ViewHolder();
    //
    //            viewHolder.relname=(TextView)convertView.findViewById(R.id.release_name);
    //            viewHolder.swlead=(TextView)convertView.findViewById(R.id.rel_sw_display);
    //            viewHolder.hwlead=(TextView)convertView.findViewById(R.id.rel_hw_display);
    //            viewHolder._linearLayout=(LinearLayout)convertView.findViewById(R.id.rli_rel_main);
    //            viewHolder.image = (ImageView) convertView.findViewById(R.id.color_layout_l2);
    //
    //            convertView.setTag(viewHolder);
    //        }else {
    //            viewHolder = (ReleaseListAdapter.ViewHolder) convertView.getTag();
    //        }
    //        final ReleaseListModel activeReleModelItem = (ReleaseListModel) rel_listmodel.get(position);
    //        viewHolder.relname.setText(activeReleModelItem.getRelname());
    //        viewHolder.swlead.setText(activeReleModelItem.getSwlead());
    //        viewHolder.hwlead.setText(activeReleModelItem.getHwlead());
    //
    //        viewHolder._linearLayout.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                Log.i(TAG,activeReleModelItem.getRelname());
    //                s_rel_name=activeReleModelItem.getRelname();
    //                ConnectionDetector conn = new ConnectionDetector(rellistcontext.getApplicationContext());
    //                if(conn.isConnectingToInternet()) {
    //
    //                    dropTable();
    //                    //new PhaseThreeAdapter.NPIDetailsAsychtask().execute(activeReleModelItem.getNpiid());
    //                    sendRequest(activeReleModelItem.getRelname());
    //                }else{
    //                    CustomTast ct=new CustomTast(rellistcontext);
    //                    ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
    //                }
    //            }
    //        });
    //
    //        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
    //
    //        int color = generator.getRandomColor();
    //
    //        TextDrawable drawable = TextDrawable.builder()
    //                .buildRound("R", color);
    //
    //        viewHolder.image.setImageDrawable(drawable);
    //
    //        notifyDataSetChanged();
    //        return convertView;
    //    }
    private void dropTable() {
        int result= mContext.getContentResolver().delete(NPIProvider.CONTENT_NPI_TRACKER_RLI_STATUS_URI,null,null);
        if (result!=0){
            //  Log.i(TAG,"rows affected"+result);
        }
    }
    private void sendRequest(final String s_npiid) {

        customDialog=new CustomDialog(mContext,"Loading...");
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();


        rliistStringRequest = new StringRequest(ActiveRel_API+s_npiid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i(TAG,response);
                        try {

                            JSONObject obj = new JSONObject(response);

                            String result = obj.getString("status");
                            if (result.equalsIgnoreCase("ok")){

                                ContentValues values = new ContentValues();
                                values.put(NPITrackerDBHelper.KEY_NPITRACKER_RLI_STATUS, response);

                                Uri uri = mContext.getContentResolver().insert(NPIProvider.CONTENT_NPI_TRACKER_RLI_STATUS_URI, values);
                                if (uri!=null){
                                    if (ContentUris.parseId(uri)>0);

                                    customDialog.cancel();
                                    Intent intent=new Intent(mContext,RLIListDetailActivity.class);
                                    // intent.putExtra("releasenumber",activeReleModelItem.getNpiname());
                                    intent.putExtra("releasename",s_rel_name);
                                    mContext.startActivity(intent);

                                }else{
                                    customDialog.cancel();
                                    CustomTast ct=new CustomTast(mContext);
                                    ct.showCustomAlert("check pulse conection",R.drawable.disconnect);
                                }

                            }
                            else {
                                customDialog.cancel();
                                CustomTast ct=new CustomTast(mContext);
                                ct.showCustomAlert("check pulse conection",R.drawable.disconnect);

                            }

                        } catch (JSONException e) {
                            customDialog.cancel();
                            CustomTast ct=new CustomTast(mContext);
                            ct.showCustomAlert("check pulse conection",R.drawable.disconnect);
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customDialog.cancel();

                        CustomTast ct=new CustomTast(mContext);
                        ct.showCustomAlert("check pulse conection",R.drawable.disconnect);
                    }
                });
        rliistStringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        JdiAppController.getInstance().addToRequestQueue(rliistStringRequest);

    }
    //    static class ViewHolder{
    //
    //        LinearLayout _linearLayout;
    //        TextView relname;
    //        TextView swlead;
    //        TextView hwlead;
    //        ImageView image;
    //
    //    }

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private ArrayList<ReleaseListModel> rel_listmodel;



    private ArrayList<ArrayList<ReleaseNPIDetailsModel>> mChilds = null;

    public ReleaseListAdapter(Context context, ArrayList<ReleaseListModel> rel_listmodel) {
        mContext = context;
        this.rel_listmodel = rel_listmodel;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mChilds = new ArrayList<ArrayList<ReleaseNPIDetailsModel>>();

        for(ReleaseListModel rliRiskModel: rel_listmodel){
            mChilds.add(rliRiskModel.getNpis());
        }

    }

    public void getData(){

    }

    @Override
    public int getGroupCount() {
        return rel_listmodel.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return rel_listmodel.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.releaselist_row, parent, false);
        }

        TextView relname=(TextView)convertView.findViewById(R.id.release_name);
        TextView swlead=(TextView)convertView.findViewById(R.id.rel_sw_display);
        TextView hwlead=(TextView)convertView.findViewById(R.id.rel_hw_display);
        LinearLayout _linearLayout=(LinearLayout)convertView.findViewById(R.id.rli_rel_main);
        LinearLayout image = (LinearLayout) convertView.findViewById(R.id.color_layout_l2);

        image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.j_trust));


        final ReleaseListModel activeReleModelItem = (ReleaseListModel) rel_listmodel.get(groupPosition);
        relname.setText(activeReleModelItem.getRelname());
        swlead.setText(activeReleModelItem.getSwlead());
        hwlead.setText(activeReleModelItem.getHwlead());

        //        _linearLayout.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                Log.i(TAG,activeReleModelItem.getRelname());
        //                s_rel_name=activeReleModelItem.getRelname();
        //                ConnectionDetector conn = new ConnectionDetector(rellistcontext.getApplicationContext());
        //                if(conn.isConnectingToInternet()) {
        //
        //                    dropTable();
        //                    //new PhaseThreeAdapter.NPIDetailsAsychtask().execute(activeReleModelItem.getNpiid());
        //                    sendRequest(activeReleModelItem.getRelname());
        //                }else{
        //                    CustomTast ct=new CustomTast(rellistcontext);
        //                    ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        //                }
        //            }
        //        });
        //


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChilds.get(groupPosition).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChilds.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.releaselist_row_inner, parent, false);
        }

        final ReleaseListModel activeReleModelItem = (ReleaseListModel) rel_listmodel.get(groupPosition);


        final TextView NpiName = (TextView) convertView.findViewById(R.id.t_npi_name_display);
        NpiName.setText(mChilds.get(groupPosition).get(childPosition).getNpiName());

        final TextView totalRlis = (TextView) convertView.findViewById(R.id.t_total_rlis_display);
        totalRlis.setText(String.valueOf(mChilds.get(groupPosition).get(childPosition).getTotalRLIs()));

        final TextView atRisk = (TextView) convertView.findViewById(R.id.t_at_risk_display);
        atRisk.setText(String.valueOf(mChilds.get(groupPosition).get(childPosition).getRLIsAtRisk()));

        final TextView processFollowed = (TextView) convertView.findViewById(R.id.t_process_followed_diaplay);
        processFollowed.setText(String.valueOf(mChilds.get(groupPosition).get(childPosition).getProcessFollowed()));

        final TextView processNotFollowed = (TextView) convertView.findViewById(R.id.t_process_followed_not_diaplay);
        processNotFollowed.setText(String.valueOf(mChilds.get(groupPosition).get(childPosition).getProcessNotFollowed()));

        final CardView npiLayout = (CardView) convertView.findViewById(R.id.card_view);

        npiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,activeReleModelItem.getRelname());
                s_rel_name=activeReleModelItem.getRelname();
                ConnectionDetector conn = new ConnectionDetector(mContext.getApplicationContext());
                if(conn.isConnectingToInternet()) {

                    dropTable();
                    //new PhaseThreeAdapter.NPIDetailsAsychtask().execute(activeReleModelItem.getNpiid());
                    sendRequest(activeReleModelItem.getRelname());
                }else{
                    CustomTast ct=new CustomTast(mContext);
                    ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
                }
            }
        });



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
