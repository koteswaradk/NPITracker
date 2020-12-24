package com.juniper.npitracker.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by koteswara on 23/08/16.
 */
public class Util {


    public static String API_TOKEN;
    public static String PASSWORD;
    public static String UserName;
    public static String USER_ID;
    public static boolean IS_Manager;
    public static String DESIGNATION;
    /*Api Varialbes */
     /*Param Definitions:*/
    public static String action = "a";
    public static String page ="p" ;
    public static String from = "f";
    public static String to = "t";

    /* Actions:*/
    public static String getData="g";
    public static String insertData ="i";
    public static String updateData ="u";

    /* Pages:*/
    public static String activerelsummary = "a";
    public static String chart = "c";
    public static String scheduler = "s";
    public static String testreport = "t";

    public static String iD="id";
    public static String release_Number="rel";
    public static String type="type";
    public static String range="range";

    public static final String BASE_API_URI="http://jdiregression.juniper.net/mobile/api/?";
    //public static final String BASE_API_URI= "http://jdiregression.juniper.net/mobile/api/";
    /** A method to download json data from url */
    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        String TAG="exception during url download";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d(TAG, e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    public static String loadJSONFromAsset(Context context,String name){
        String json = null;
        try {
            InputStream is = context.getAssets().open(name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
