package com.juniper.npitracker.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.juniper.npitracker.R;
import com.juniper.npitracker.model.PRSeverityGroupModel;

import java.util.ArrayList;

/**
 * Created by sarahcs on 2/9/2017.
 */

public class PRSeverityAdpater extends ArrayAdapter<PRSeverityGroupModel> {

    // declaring our ArrayList of items
    private Context context;
    private ArrayList<PRSeverityGroupModel> objects;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public PRSeverityAdpater(Context context, ArrayList<PRSeverityGroupModel> objects) {
        super(context, R.layout.pr_severity_summary_list_item, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.pr_severity_summary_list_item, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        PRSeverityGroupModel prSeverityGroup = objects.get(position);

        if (prSeverityGroup != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            LinearLayout groupBg = (LinearLayout) v.findViewById(R.id.layout_group);
            if (position % 2 == 1) {
                groupBg.setBackgroundColor(context.getResources().getColor(R.color.subheading_grey));
            } else {
                groupBg.setBackgroundColor(context.getResources().getColor(R.color.light_subheading_grey));
            }

            TextView prGroupName = (TextView) v.findViewById(R.id.pr_severity_group_name);
            prGroupName.setText(prSeverityGroup.getGroupName());
            prGroupName.setPaintFlags(prGroupName.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

            TextView prGroupOpen = (TextView) v.findViewById(R.id.pr_severity_group_open);
            prGroupOpen.setText(String.valueOf(prSeverityGroup.getOpen()));

            TextView prGroupVerified = (TextView) v.findViewById(R.id.pr_severity_group_verified);
            prGroupVerified.setText(String.valueOf(prSeverityGroup.getVerified()));

            TextView prGroupClosed = (TextView) v.findViewById(R.id.pr_severity_group_closed);
            prGroupClosed.setText(String.valueOf(prSeverityGroup.getClosed()));

            TextView prGroupTotal = (TextView) v.findViewById(R.id.pr_severity_group_total);
            prGroupTotal.setText(String.valueOf(prSeverityGroup.getTotal()));

            TextView prGroupLast7days = (TextView) v.findViewById(R.id.pr_severity_group_last_7_days);
            prGroupLast7days.setText(String.valueOf(prSeverityGroup.getLast7Days()));

        }

        // the view must be returned to our activity
        return v;

    }

}
