package com.juniper.npitracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juniper.npitracker.NPITrackerAdapter;
import com.juniper.npitracker.NPITrackerModel;
import com.juniper.npitracker.R;
import com.juniper.npitracker.model.NPIModel;

import java.util.ArrayList;

/**
 * Created by koteswara on 06/02/17.
 */

public class NPIAdapter extends BaseAdapter{
    private static final String TAG= "NPITrackerAdapter";
    private LayoutInflater layoutInflater;
    public ArrayList<NPIModel> _alnpitracker;

    Context npicontext;
    public NPIAdapter(Context context,ArrayList<NPIModel> alnpitracker ){
        this.npicontext=context;
        this._alnpitracker=alnpitracker;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        NPIAdapter.ViewHolder viewHolder=null;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) npicontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.npitrackermain_row, null);
            viewHolder = new NPIAdapter.ViewHolder();


            viewHolder.npiname=(TextView)convertView.findViewById(R.id.npi_name);
            viewHolder.swlead=(TextView)convertView.findViewById(R.id.npiswlead_display);
            viewHolder.plm=(TextView)convertView.findViewById(R.id.npiplm_display);
            viewHolder.testlead=(TextView)convertView.findViewById(R.id.npi_testlead_display);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (NPIAdapter.ViewHolder) convertView.getTag();
        }
        final NPIModel activeReleModelItem = (NPIModel) _alnpitracker.get(position);
        // View rowView = convertView;

        viewHolder.npiname.setText(activeReleModelItem.getNpiname());
        viewHolder.swlead.setText(activeReleModelItem.getSwlead());
        viewHolder.plm.setText(activeReleModelItem.getPlm());
        viewHolder.testlead.setText(activeReleModelItem.getTestlead());

        notifyDataSetChanged();
        return convertView;
    }

    static class ViewHolder{
        LinearLayout _linearLayout;
        CheckBox checkbox;
        TextView npiname;
        TextView swlead;
        TextView plm;
        TextView testlead;


    }
}
