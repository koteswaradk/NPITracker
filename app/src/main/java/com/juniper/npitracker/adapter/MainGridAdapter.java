package com.juniper.npitracker.adapter;

/**
 * Created by sarahcs on 2/14/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juniper.npitracker.R;


public class MainGridAdapter extends BaseAdapter{

    private Context mContext;
    private final int[] titles;
    private final int[] Imageid;
    private final int[][] colors;

    private int width, height;

    public MainGridAdapter(Context c,int[] titles,int[] Imageid, int[][] colors,int width, int height) {
        mContext = c;
        this.Imageid = Imageid;
        this.colors = colors;
        this.titles = titles;
        this.width = width;
        this.height = height;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.single_menu_tile, null);
            grid.setMinimumHeight(height/2);
            TextView textView = (TextView) grid.findViewById(R.id.grid_title);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_icon);
            RelativeLayout layoutImage = (RelativeLayout) grid.findViewById(R.id.layout_image);
            layoutImage.setBackgroundDrawable( mContext.getResources().getDrawable(colors[position][0]) );

            textView.setText(mContext.getResources().getString(titles[position]));
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
