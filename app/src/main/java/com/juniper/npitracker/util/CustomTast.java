package com.juniper.npitracker.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juniper.npitracker.R;


/**
 * Created by koteswara on 23/11/16.
 */

public class CustomTast {

    Context context;
   public CustomTast(Context context){
        this.context=context;

    }

    public void showCustomAlert(String text,int imageid)
    {

        context.getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater li = LayoutInflater.from(context);


        // Call toast.xml file for toast layout
        View toastRoot = li.inflate(R.layout.customtoast,null);

        TextView tv=(TextView) toastRoot.findViewById(R.id.custom_toast);
        tv.setText(text);
        ImageView iv=(ImageView)toastRoot.findViewById(R.id.cusomtoast_image);
        iv.setBackgroundResource(imageid);
        Toast toast = new Toast(context);
        // Set layout to toast
        toast.setView(toastRoot);
        /*toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);*/
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);

        toast.show();

    }

}
