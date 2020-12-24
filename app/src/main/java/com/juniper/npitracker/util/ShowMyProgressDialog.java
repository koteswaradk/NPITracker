package com.juniper.npitracker.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.juniper.npitracker.R;


/**
 * Created by koteswara on 23/08/16.
 */
public class ShowMyProgressDialog {
   public static ProgressDialog progressDialog;
   static public void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context, R.style.JditDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }
    static public void calncesProgressDialog(Context context){

        progressDialog.dismiss();
    }


}
