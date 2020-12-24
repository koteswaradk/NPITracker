package com.juniper.npitracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by koteswara on 01/02/17.
 */

public class NPITrackerAdapter extends BaseAdapter{
    private static final String TAG= "NPITrackerAdapter";
    private LayoutInflater layoutInflater;
    public ArrayList<NPITrackerModel> _alnpitracker;

    Context npicontext;
    public NPITrackerAdapter(Context context,ArrayList<NPITrackerModel> alnpitracker ){
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
        NPITrackerAdapter.ViewHolder viewHolder=null;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) npicontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.npitrackermain_row, null);
            viewHolder = new NPITrackerAdapter.ViewHolder();


            viewHolder.npiname=(TextView)convertView.findViewById(R.id.npi_name);
            viewHolder.swlead=(TextView)convertView.findViewById(R.id.npiswlead_display);
            viewHolder.plm=(TextView)convertView.findViewById(R.id.npiplm_display);
            viewHolder.testlead=(TextView)convertView.findViewById(R.id.npi_testlead_display);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (NPITrackerAdapter.ViewHolder) convertView.getTag();
        }
        final NPITrackerModel activeReleModelItem = (NPITrackerModel) _alnpitracker.get(position);
        // View rowView = convertView;

        viewHolder.npiname.setText(activeReleModelItem.getNpiname());
        viewHolder.swlead.setText(activeReleModelItem.getSwlead());
        viewHolder.plm.setText(activeReleModelItem.getPlm());
        viewHolder.testlead.setText(activeReleModelItem.getTestlead());
       /* NPITrackerAdapter.ViewHolder viewHolder;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) npicontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.npitrackermain_row, null);
            viewHolder = new NPITrackerAdapter.ViewHolder();
            viewHolder.npiname=(TextView)convertView.findViewById(R.id.npi_name);
            viewHolder.swlead=(TextView)convertView.findViewById(R.id.npiswlead_display);
            viewHolder.plm=(TextView)convertView.findViewById(R.id.npiplm_display);
            viewHolder.testlead=(TextView)convertView.findViewById(R.id.npi_testlead_display);
            viewHolder.checkbox=(CheckBox)convertView.findViewById(R.id.select);

        }else{
            viewHolder = (NPITrackerAdapter.ViewHolder) convertView.getTag();
        }
         NPITrackerModel npimodel = (NPITrackerModel) _alnpitracker.get(position);
        viewHolder.npiname.setText(npimodel.getNpiname());
        viewHolder.swlead.setText(npimodel.getSwlead());
        viewHolder.plm.setText(npimodel.getPlm());
        viewHolder.testlead.setText(npimodel.getTestlead());*/

       /* viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });*/
        notifyDataSetChanged();
        return convertView;
    }

    static class ViewHolder{
        CheckBox checkbox;
        TextView npiname;
        TextView swlead;
        TextView plm;
        TextView testlead;

    }
}
