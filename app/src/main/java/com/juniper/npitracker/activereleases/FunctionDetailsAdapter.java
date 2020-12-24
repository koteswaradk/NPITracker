package com.juniper.npitracker.activereleases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.juniper.npitracker.R;

import java.util.ArrayList;

/**
 * Created by koteswara on 18/01/17.
 */

public class FunctionDetailsAdapter extends BaseAdapter{
    private static final String TAG= "ActiveRelAdapter";
    private LayoutInflater layoutInflater;
    ArrayList<FunctionDetailsModel> functionmodel;
    Context _context;
    FunctionDetailsAdapter(Context context, ArrayList<FunctionDetailsModel> model){
        this._context=context;
        this.functionmodel=model;

    }
    @Override
    public int getCount() {
        return functionmodel.size();
    }

    @Override
    public Object getItem(int position) {
        return functionmodel.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        FunctionDetailsAdapter.ViewHolder viewHolder=null;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
           // convertView = layoutInflater.inflate(R.layout.functiondetailsrow, null);
            convertView = layoutInflater.inflate(R.layout.function_details_row, null);
            viewHolder = new FunctionDetailsAdapter.ViewHolder();

            /*viewHolder.number=(TextView)convertView.findViewById(R.id.functionumber_display);
            viewHolder.arrivaldate=(TextView)convertView.findViewById(R.id.arrivaldate_display);
            viewHolder.reportedin=(TextView)convertView.findViewById(R.id.reportedin_display);
            viewHolder.state=(TextView)convertView.findViewById(R.id.state_display);
            viewHolder.problem_level=(TextView)convertView.findViewById(R.id.problem_level_display);
            viewHolder.hit=(TextView)convertView.findViewById(R.id.hit_display);*/

            viewHolder.number=(TextView)convertView.findViewById(R.id.t_number_display);
            viewHolder.arrivaldate=(TextView)convertView.findViewById(R.id.t_arrivaldate_display);
            viewHolder.reportedin=(TextView)convertView.findViewById(R.id.t_reportedin_display);
            viewHolder.state=(TextView)convertView.findViewById(R.id.t_state_display);
            viewHolder.problem_level=(TextView)convertView.findViewById(R.id.t_problem_level_display);
            viewHolder.hit=(TextView)convertView.findViewById(R.id.t_hit_display);



            convertView.setTag(viewHolder);
        }else {
            viewHolder = (FunctionDetailsAdapter.ViewHolder) convertView.getTag();
        }
        final FunctionDetailsModel activeReleModelItem = (FunctionDetailsModel) functionmodel.get(position);
        viewHolder.number.setText(activeReleModelItem.getNumber());
        viewHolder.arrivaldate.setText(activeReleModelItem.getArrivaldate());
        viewHolder.reportedin.setText(activeReleModelItem.getReportedin());
        viewHolder.state.setText(activeReleModelItem.getState());
        viewHolder.problem_level.setText(activeReleModelItem.getProblemlevel());
        viewHolder.hit.setText(activeReleModelItem.getHit());


        notifyDataSetChanged();
        return convertView;
    }
    static class ViewHolder{

        TextView number;
        TextView arrivaldate;
        TextView reportedin;
        TextView state;
        TextView hit;
        TextView problem_level;

    }
}
