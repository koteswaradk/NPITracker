package com.juniper.npitracker.activereleases;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.juniper.npitracker.R;

import java.util.ArrayList;

/**
 * Created by koteswara on 26/12/16.
 */

public class ActiveRelAdapter extends BaseAdapter implements Filterable {
    private static final String TAG= "ActiveRelAdapter";
    private LayoutInflater layoutInflater;
    ArrayList<ActiveRelModel> _al_activerelmodel;
    public ArrayList<ActiveRelModel> orig;
    Context _context;
    public ActiveRelAdapter(Context contex,ArrayList<ActiveRelModel> al_activerelmodel){
        this._context=contex;
        this._al_activerelmodel=al_activerelmodel;
    }
    @Override
    public int getCount() {
        return _al_activerelmodel.size();
    }

    @Override
    public Object getItem(int position) {
        return _al_activerelmodel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ActiveRelAdapter.ViewHolder viewHolder=null;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.active_rel_row, null);
            viewHolder = new ActiveRelAdapter.ViewHolder();
            viewHolder._active_rel_main=(LinearLayout)convertView.findViewById(R.id.active_rel_main);
            viewHolder._color_layout_l2=(LinearLayout)convertView.findViewById(R.id.color_layout_l2);

            viewHolder.relname=(TextView)convertView.findViewById(R.id.active_rel_name);
            viewHolder.overallpass=(TextView)convertView.findViewById(R.id.active_rel_overall_display);
            viewHolder.firstpass=(TextView)convertView.findViewById(R.id.active_rel_firstpass_display);
            viewHolder.openblocker=(TextView)convertView.findViewById(R.id.active_rel_openblocker_display);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ActiveRelAdapter.ViewHolder) convertView.getTag();
        }
        final ActiveRelModel activeReleModelItem = (ActiveRelModel) _al_activerelmodel.get(position);
        // View rowView = convertView;

        viewHolder.relname.setText(activeReleModelItem.getRelname().substring(0, Math.min(activeReleModelItem.getRelname().length(), 40)));
        viewHolder.firstpass.setText(activeReleModelItem.getFirstpass().concat("%"));
        viewHolder.overallpass.setText(activeReleModelItem.getOverallpas().concat("%"));
        viewHolder.openblocker.setText(activeReleModelItem.getOpenblocker());

       /* viewHolder._active_rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(_context,ActiveRelReport.class);
                intent.putExtra("releasenumber",activeReleModelItem.getRelname().toString());
                intent.putExtra("firstpass",activeReleModelItem.getFirstpass().toString());
                _context.startActivity(intent);

                ((Activity)_context).overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
            }
        });*/


       /* if ((Integer.parseInt(activeReleModelItem.getOverallpas())==99)&&(Integer.parseInt(activeReleModelItem.getOpenblocker())>0)){
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_aspiration));
        }
        if ((Integer.parseInt(activeReleModelItem.getOverallpas())<99)&&(Integer.parseInt(activeReleModelItem.getOpenblocker())==0)){
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_authetic));
        }
        if ((Integer.parseInt(activeReleModelItem.getOverallpas())==99)&&(Integer.parseInt(activeReleModelItem.getOpenblocker())==0)){
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_excellence));
        }*/
       /* if ((Integer.parseInt(activeReleModelItem.getOverallpas())==99)&&(Integer.parseInt(activeReleModelItem.getOpenblocker())>0)){
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_aspiration));
        }
        if ((Integer.parseInt(activeReleModelItem.getOverallpas())<99)&&(Integer.parseInt(activeReleModelItem.getOpenblocker())==0)){
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_authetic));
        }*/

        if ((Integer.parseInt(activeReleModelItem.getOverallpas())>=99)&&(Integer.parseInt(activeReleModelItem.getOpenblocker())==0)){
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_excellence));
            viewHolder.openblocker.setTextColor(ContextCompat.getColor(_context, R.color.textcolor));
        }
        if ((Integer.parseInt(activeReleModelItem.getOverallpas())<99)&&(Integer.parseInt(activeReleModelItem.getOpenblocker())==0)){
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_authetic));
            viewHolder.openblocker.setTextColor(ContextCompat.getColor(_context, R.color.textcolor));
        }
        /*else  {
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_authetic));
        }*/
        if ((Integer.parseInt(activeReleModelItem.getOverallpas())==99)&&(Integer.parseInt(activeReleModelItem.getOpenblocker())>0)){
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_aspiration));
            viewHolder.openblocker.setTextColor(ContextCompat.getColor(_context, R.color.j_aspiration));
        }
        if ((Integer.parseInt(activeReleModelItem.getOverallpas())<99)&&(Integer.parseInt(activeReleModelItem.getOpenblocker())>0)){
            viewHolder._color_layout_l2.setBackgroundColor(ContextCompat.getColor(_context, R.color.j_aspiration));
            viewHolder.openblocker.setTextColor(ContextCompat.getColor(_context, R.color.j_aspiration));
        }

        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ActiveRelModel> results = new ArrayList<ActiveRelModel>();
                if (orig == null)
                    orig = _al_activerelmodel;
                if (charSequence != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ActiveRelModel g : orig) {
                            if (g.getRelname().toLowerCase()
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
                _al_activerelmodel = (ArrayList<ActiveRelModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder{

        LinearLayout _active_rel_main;
        LinearLayout _color_layout_l2;
        TextView relname;
        TextView overallpass;
        TextView firstpass;
        TextView openblocker;

    }
}
