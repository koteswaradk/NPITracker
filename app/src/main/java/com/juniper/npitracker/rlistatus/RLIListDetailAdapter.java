package com.juniper.npitracker.rlistatus;

import android.content.Context;
import android.graphics.Typeface;
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

import com.juniper.npitracker.R;
import com.juniper.npitracker.adapter.PhaseOneAdapter;
import com.juniper.npitracker.model.PhaseOneModel;

import java.util.ArrayList;

/**
 * Created by koteswara on 15/02/17.
 */

public class RLIListDetailAdapter extends BaseAdapter implements Filterable{
    private LayoutInflater layoutInflater;
    ArrayList<RLILIstDetailsModel> listmodel;
    public ArrayList<RLILIstDetailsModel> orig;
    Context context;
    Typeface typeface;
    public RLIListDetailAdapter(Context context, ArrayList<RLILIstDetailsModel> listmodel){
        this.context=context;
        this.listmodel=listmodel;
        typeface = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

    }
    @Override
    public int getCount() {
        return listmodel.size();
    }

    @Override
    public Object getItem(int position) {
        return listmodel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        RLIListDetailAdapter.ViewHolder viewHolder=null;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.rlilist_row, null);
            viewHolder = new RLIListDetailAdapter.ViewHolder();

            viewHolder.rliid=(TextView)convertView.findViewById(R.id.t_rli_id_display);
            viewHolder.synopsis=(TextView)convertView.findViewById(R.id.t_synopsis_display);
            viewHolder.tests_executed_percentage=(TextView)convertView.findViewById(R.id.t_executed_display);
            viewHolder.tests_passed_percentage=(TextView)convertView.findViewById(R.id.t_pass_rate_display);
            viewHolder.tests_script_percentage=(TextView)convertView.findViewById(R.id.t_scripted_display);
            viewHolder.process_followed_checkbox=(CheckBox)convertView.findViewById(R.id.t_process_followed_diaplay);
            viewHolder.process_followed_text=(TextView)convertView.findViewById(R.id.image_process_followed_diaplay);
            viewHolder.npiProgram = (TextView) convertView.findViewById(R.id.t_npi_program_display);


            convertView.setTag(viewHolder);
        }else {
            viewHolder = (RLIListDetailAdapter.ViewHolder) convertView.getTag();
        }

        final RLILIstDetailsModel rliItemmodel = (RLILIstDetailsModel) listmodel.get(position);
        viewHolder.rliid.setText(rliItemmodel.getRliid());
        viewHolder.synopsis.setText(rliItemmodel.getSynopsis());
        viewHolder.tests_executed_percentage.setText(rliItemmodel.getTestExecutedPercentage());
        viewHolder.tests_passed_percentage.setText(rliItemmodel.getTestPassedPercentage());
        viewHolder.tests_script_percentage.setText(rliItemmodel.getTestScriptsPercentage());
        if (rliItemmodel.getProcessfollowed().equalsIgnoreCase("yes")){
            //viewHolder.process_followed_checkbox.setChecked(true);
            viewHolder.process_followed_text.setText(R.string.font_awsome_check);
            viewHolder.process_followed_text.setTextColor(context.getResources().getColor(R.color.j_excellence));
            viewHolder.process_followed_text.setTypeface(typeface);
        }
        if (rliItemmodel.getProcessfollowed().equalsIgnoreCase("no")){
          //  viewHolder.process_followed_checkbox.setChecked(false);
            viewHolder.process_followed_text.setText(R.string.font_awsome_cross);
            viewHolder.process_followed_text.setTextColor(context.getResources().getColor(R.color.j_aspiration));
            viewHolder.process_followed_text.setTypeface(typeface);

        }

        viewHolder.npiProgram.setText(rliItemmodel.getNpiprogram());

        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<RLILIstDetailsModel> results = new ArrayList<RLILIstDetailsModel>();
                if (orig == null)
                    orig = listmodel;
                if (charSequence != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final RLILIstDetailsModel g : orig) {
                            if (g.getSynopsis().concat(g.getRliid()).concat(g.getRliid()).toLowerCase().contains(charSequence.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listmodel = (ArrayList<RLILIstDetailsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder{

        LinearLayout _linearLayout;
        CheckBox process_followed_checkbox;
        TextView process_followed_text;
        TextView tests_executed_percentage;
        TextView tests_passed_percentage;
        TextView tests_script_percentage;
        TextView synopsis;
        TextView rliid;
        TextView npiProgram;

    }
}
