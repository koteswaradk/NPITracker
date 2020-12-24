package com.juniper.npitracker.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by koteswara on 06/02/17.
 */

public class NPITrackerDBHelper extends SQLiteOpenHelper{

    //DB NAMES AND VERSION
    public static final String DATABASE_NAME="npiracker.db";
    public static final int VERSION=1;
    //TABLE NAMES
    public static final String NPI_TRACKER_TABLE="npitracker";
    public static final String NPI_TRACKER_SINGLE_API_TABLE="sigleapi";
    public static final String NPI_TRACKER_PHASEWISE_API_TABLE="pahsewiseapi";
    public static final String NPI_TRACKER_TEST_STATUS_API_TABLE="statusapi";
    public static final String NPI_TRACKER_RLI_STATUS_TABLE="rlistatusapi";
    public static final String NPI_TRACKER_PR_SUMMARY_TABLE="prsummaryapi";
    public static final String NPI_TRACKER_LAST_SYNCH_TABLE="lastsync";

    //public static final String NPI_TRACKER_DETAILS_TABLE="npidetails";

    public static final String NPI_TRACKER_USER_SELECTED_NPI_TABLE="usermonitoringnpi";

    //TABLE COLUMN
    public static final String KEY_ID="id";

    public static final String KEY_NPITRACKER_SINGLEAPI="singleapi";
    public static final String KEY_NPITRACKER_PHASEWISE_API="phasewiseapi";
    public static final String KEY_NPITRACKER_TEST_STATUS_API="statusapi";
    public static final String KEY_NPITRACKER_RLI_STATUS="rlistatusapi";
    public static final String KEY_NPITRACKER_PR_SUMMARY="prsummaryapi";
    public static final String KEY_NPITRACKER_LAST_SYNCH="lastsynch";
    public static final String KEY_NPITRACKER_DETAILS="npidetails";
    public static final String KEY_NPITRACKER_PHASE="phase";
    //TABLE COLUMN OF USER MONITORING NPI TABLE
    public static final String KEY_NPITRACKER_USER_SELECTED_NPINAME="selectednpiname";
    public static final String KEY_NPITRACKER_USER_SELECTED_NPI_ID="selectednpiid";
    public static final String KEY_NPITRACKER_SWLEAD="selectedswlead";
    public static final String KEY_NPITRACKER_PLM="selectedplm";
    public static final String KEY_NPITRACKER_TL="selectedtl";
    public static final String KEY_NPITRACKER_STATUS="status";


    //CREATE TABLE STRING
    private static final String CREATE_TABLE_NPITRACKER = "CREATE TABLE "
            + NPI_TRACKER_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NPITRACKER_SINGLEAPI
            + " TEXT," + KEY_NPITRACKER_PHASEWISE_API + " TEXT," + KEY_NPITRACKER_TEST_STATUS_API
            + " TEXT," + KEY_NPITRACKER_RLI_STATUS + " TEXT," + KEY_NPITRACKER_PR_SUMMARY
            + " TEXT," + KEY_NPITRACKER_LAST_SYNCH + " TEXT" + ")";

    private static final String CREATE_TABLE_NPITRACKER_SINGLE_API = "CREATE TABLE "
            + NPI_TRACKER_SINGLE_API_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NPITRACKER_SINGLEAPI
            + " TEXT" + ")";

    private static final String CREATE_TABLE_NPITRACKER_PHASEWISE_AP = "CREATE TABLE "
            + NPI_TRACKER_PHASEWISE_API_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NPITRACKER_PHASEWISE_API + " TEXT" + ")";

    private static final String CREATE_TABLE_NPITRACKER_TEST_STATUS_API = "CREATE TABLE "
            + NPI_TRACKER_TEST_STATUS_API_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NPITRACKER_TEST_STATUS_API + " TEXT" + ")";

    private static final String CREATE_TABLE_NPITRACKER_RLI_STATUS = "CREATE TABLE "
            + NPI_TRACKER_RLI_STATUS_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NPITRACKER_RLI_STATUS + " TEXT" + ")";

    private static final String CREATE_TABLE_NPITRACKER_PR_SUMMARY = "CREATE TABLE "
            + NPI_TRACKER_PR_SUMMARY_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NPITRACKER_PR_SUMMARY + " TEXT" + ")";

    private static final String CREATE_TABLE_NPITRACKER_LAST_SYNCH = "CREATE TABLE "
            + NPI_TRACKER_LAST_SYNCH_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NPITRACKER_LAST_SYNCH + " TEXT" + ")";

    private static final String CREATE_TABLE_NPI_TRACKER_USER_SELECTED_NPI = "CREATE TABLE "
            + NPI_TRACKER_USER_SELECTED_NPI_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NPITRACKER_USER_SELECTED_NPINAME + " TEXT,"
            + KEY_NPITRACKER_USER_SELECTED_NPI_ID + " TEXT,"
            + KEY_NPITRACKER_SWLEAD + " TEXT,"
            + KEY_NPITRACKER_PHASE + " TEXT,"
            + KEY_NPITRACKER_PLM + " TEXT,"
            + KEY_NPITRACKER_STATUS + " TEXT,"
            + KEY_NPITRACKER_TL + " TEXT " + ")";

    /*private static final String CREATE_NPI_TRACKER_DETAILS_TABLE = "CREATE TABLE "
            + NPI_TRACKER_DETAILS_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NPITRACKER_DETAILS + " TEXT " + ")";*/

    public NPITrackerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {

            sqLiteDatabase.execSQL(CREATE_TABLE_NPITRACKER);
            sqLiteDatabase.execSQL(CREATE_TABLE_NPITRACKER_SINGLE_API);
            sqLiteDatabase.execSQL(CREATE_TABLE_NPITRACKER_PHASEWISE_AP);
            sqLiteDatabase.execSQL(CREATE_TABLE_NPITRACKER_TEST_STATUS_API);
            sqLiteDatabase.execSQL(CREATE_TABLE_NPITRACKER_RLI_STATUS);
            sqLiteDatabase.execSQL(CREATE_TABLE_NPITRACKER_PR_SUMMARY);
            sqLiteDatabase.execSQL(CREATE_TABLE_NPITRACKER_LAST_SYNCH);
            sqLiteDatabase.execSQL(CREATE_TABLE_NPI_TRACKER_USER_SELECTED_NPI);
            /*sqLiteDatabase.execSQL(CREATE_NPI_TRACKER_DETAILS_TABLE);*/

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NPI_TRACKER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NPI_TRACKER_SINGLE_API_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NPI_TRACKER_PHASEWISE_API_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NPI_TRACKER_TEST_STATUS_API_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NPI_TRACKER_RLI_STATUS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NPI_TRACKER_PR_SUMMARY_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NPI_TRACKER_LAST_SYNCH_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NPI_TRACKER_USER_SELECTED_NPI_TABLE);
       /* sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NPI_TRACKER_DETAILS_TABLE);*/
        onCreate(sqLiteDatabase);
    }
}
