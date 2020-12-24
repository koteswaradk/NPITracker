package com.juniper.npitracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by koteswara on 06/02/17.
 */

public class NPIProvider extends ContentProvider {
    //CONTRACT DETAILS
    public static final String CONTENT_AUTHORITY = "com.juniper.npitracker.NpiTrackerProviderProvider";
    public static final String NPI_TRACKER_URL = "content://"+ CONTENT_AUTHORITY +"/"+ NPITrackerDBHelper.NPI_TRACKER_TABLE;
    public static final String NPI_TRACKER_SINGLE_API_URL = "content://"+ CONTENT_AUTHORITY +"/"+ NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE;
    public static final String NPI_TRACKER_PHASEWISE_API_URL = "content://"+ CONTENT_AUTHORITY +"/"+ NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE;
    public static final String NPI_TRACKER_TEST_STATUS_API_URL = "content://"+ CONTENT_AUTHORITY +"/"+ NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE;
    public static final String NPI_TRACKER_RLI_STATUS_URL = "content://"+ CONTENT_AUTHORITY +"/"+ NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE;
    public static final String NPI_TRACKER_PR_SUMMARY_URL = "content://"+ CONTENT_AUTHORITY +"/"+ NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE;
    public static final String NPI_TRACKER_LAST_SYNCH_URL = "content://"+ CONTENT_AUTHORITY +"/"+ NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE;
    public static final String NPI_TRACKER_USER_SELECTED_NPI_URL = "content://"+ CONTENT_AUTHORITY +"/"+ NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE;

   /* public static final String NPI_TRACKER_NPI_DETAILS_URL = "content://"+ CONTENT_AUTHORITY +"/"+ NPITrackerDBHelper.NPI_TRACKER_DETAILS_TABLE;*/

    public static final Uri CONTENT_NPI_TRACKER_URI = Uri.parse(NPI_TRACKER_URL);
    public static final Uri CONTENT_NPI_TRACKER_SINGLE_API_URI = Uri.parse(NPI_TRACKER_SINGLE_API_URL);
    public static final Uri CONTENT_NPI_TRACKER_PHASEWISE_API_URI = Uri.parse(NPI_TRACKER_PHASEWISE_API_URL);
    public static final Uri CONTENT_NPI_TRACKER_TEST_STATUS_URI = Uri.parse(NPI_TRACKER_TEST_STATUS_API_URL);
    public static final Uri CONTENT_NPI_TRACKER_RLI_STATUS_URI = Uri.parse(NPI_TRACKER_RLI_STATUS_URL);
    public static final Uri CONTENT_NPI_TRACKER_PR_SUMMARY_URI = Uri.parse(NPI_TRACKER_PR_SUMMARY_URL);
    public static final Uri CONTENT_NPI_TRACKER_LAST_SYNCH_URI = Uri.parse(NPI_TRACKER_LAST_SYNCH_URL);
    public static final Uri CONTENT_NPI_TRACKER_USER_SELECTED_NPI_URI = Uri.parse(NPI_TRACKER_USER_SELECTED_NPI_URL);

   /* public static final Uri CONTENT_NPI_DETAILS_URI = Uri.parse(NPI_TRACKER_NPI_DETAILS_URL);*/

    //URI MATCHER ID FOR DATA RETRIVE
    static final int NPI_TRACKER = 1;
    static final int NPI_TRACKER_ID = 2;

    static final int NPI_TRACKER_SINGLE = 3;
    static final int NPI_TRACKER_SINGLE_ID = 4;

    static final int NPI_TRACKER_PHASEWISE = 5;
    static final int NPI_TRACKER_PHASEWISE_ID = 6;

    static final int NPI_TRACKER_TEST_STATUS = 7;
    static final int NPI_TRACKER_TEST_STATUS_ID = 8;

    static final int NPI_TRACKER_RLI_STATUS = 9;
    static final int NPI_TRACKER_RLI_STATUS_ID = 10;

    static final int NPI_TRACKER_PR_SUMMARY = 11;
    static final int NPI_TRACKER_PR_SUMMARY_ID = 12;

    static final int NPI_TRACKER_LAST_SYNCH = 13;
    static final int NPI_TRACKER_LAST_SYNCH_ID = 14;

    static final int NPI_TRACKER_USER_SELECTED_NPI = 15;

    static final int NPI_TRACKER_USER_SELECTED_NPI_ID = 16;


    /**
     * Database specific constant declarations
     */
    public SQLiteDatabase db;
    NPITrackerDBHelper debugdbHelper;
    private static HashMap<String, String> values;
    //URI MARTECHER
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_TABLE, NPI_TRACKER);
        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_TABLE+"/#", NPI_TRACKER_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE, NPI_TRACKER_SINGLE);
        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE+"/#", NPI_TRACKER_SINGLE_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE, NPI_TRACKER_PHASEWISE);
        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE+"/#", NPI_TRACKER_PHASEWISE_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE, NPI_TRACKER_TEST_STATUS);
        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE+"/#", NPI_TRACKER_TEST_STATUS_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE, NPI_TRACKER_RLI_STATUS);
        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE+"/#", NPI_TRACKER_RLI_STATUS_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE, NPI_TRACKER_PR_SUMMARY);
        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE+"/#", NPI_TRACKER_PR_SUMMARY_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE, NPI_TRACKER_LAST_SYNCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE+"/#", NPI_TRACKER_LAST_SYNCH_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE, NPI_TRACKER_USER_SELECTED_NPI);
        uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE+"/#", NPI_TRACKER_USER_SELECTED_NPI_ID);

       /* uriMatcher.addURI(CONTENT_AUTHORITY, NPITrackerDBHelper.NPI_TRACKER_DETAILS_TABLE, NPI_TRACKER_NPIDETAILS);*/

    }

    @Override
    public boolean onCreate() {
        debugdbHelper=new NPITrackerDBHelper(getContext());

        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case NPI_TRACKER:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_TABLE);
                qb.setProjectionMap(values);
                break;
            case NPI_TRACKER_ID:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_TABLE);
                qb.appendWhere( NPITrackerDBHelper.KEY_NPITRACKER_SINGLEAPI + "=?" + uri.getPathSegments().get(1));
                break;
            case NPI_TRACKER_SINGLE:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE);
                qb.setProjectionMap(values);
                break;
            case NPI_TRACKER_SINGLE_ID:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE);
                qb.appendWhere( NPITrackerDBHelper.KEY_NPITRACKER_SINGLEAPI + "=?" + uri.getPathSegments().get(1));
                break;
            case NPI_TRACKER_PHASEWISE:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE);
                qb.setProjectionMap(values);
                break;
            case NPI_TRACKER_PHASEWISE_ID:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE);
                qb.appendWhere( NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API + "=?" + uri.getPathSegments().get(1));
                break;
            case NPI_TRACKER_TEST_STATUS:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE);
                qb.setProjectionMap(values);
                break;
            case NPI_TRACKER_TEST_STATUS_ID:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE);
                qb.appendWhere( NPITrackerDBHelper.KEY_NPITRACKER_TEST_STATUS_API + "=?" + uri.getPathSegments().get(1));
                break;
            case NPI_TRACKER_RLI_STATUS:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE);
                qb.setProjectionMap(values);
                break;
            case NPI_TRACKER_RLI_STATUS_ID:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE);
                qb.appendWhere( NPITrackerDBHelper.KEY_NPITRACKER_RLI_STATUS + "=?" + uri.getPathSegments().get(1));
                break;
            case NPI_TRACKER_PR_SUMMARY:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE);
                qb.setProjectionMap(values);
                break;
            case NPI_TRACKER_PR_SUMMARY_ID:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE);
                qb.appendWhere( NPITrackerDBHelper.KEY_NPITRACKER_PR_SUMMARY + "=?" + uri.getPathSegments().get(1));
                break;
            case NPI_TRACKER_LAST_SYNCH:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE);
                qb.setProjectionMap(values);
                break;
            case NPI_TRACKER_LAST_SYNCH_ID:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE);
                qb.appendWhere( NPITrackerDBHelper.KEY_NPITRACKER_LAST_SYNCH + "=?" + uri.getPathSegments().get(1));
                break;
            case NPI_TRACKER_USER_SELECTED_NPI:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE);
                qb.setProjectionMap(values);
                break;
            case NPI_TRACKER_USER_SELECTED_NPI_ID:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE);
                qb.appendWhere( NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPI_ID + "=?" + uri.getPathSegments().get(1));
                break;
          /*  case NPI_TRACKER_NPIDETAILS:
                qb.setTables(NPITrackerDBHelper.NPI_TRACKER_DETAILS_TABLE);
                qb.setProjectionMap(values);
                break;*/

            default:
                throw new IllegalArgumentException( "illegal uri: " + uri);

        }
        db = debugdbHelper.getWritableDatabase();
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        //   db.close();
        Log.d("inside query", "queried records: "+c.getCount());
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case NPI_TRACKER:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_TABLE;
            case NPI_TRACKER_ID:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_TABLE;

            case NPI_TRACKER_SINGLE:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE;
            case NPI_TRACKER_SINGLE_ID:
                return "vnd.android.cursor.item/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE;

            case NPI_TRACKER_PHASEWISE:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE;
            case NPI_TRACKER_PHASEWISE_ID:
                return "vnd.android.cursor.item/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE;

            case NPI_TRACKER_TEST_STATUS:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE;
            case NPI_TRACKER_TEST_STATUS_ID:
                return "vnd.android.cursor.item/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE;

            case NPI_TRACKER_RLI_STATUS:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE;
            case NPI_TRACKER_RLI_STATUS_ID:
                return "vnd.android.cursor.item/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE;

            case NPI_TRACKER_PR_SUMMARY:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE;
            case NPI_TRACKER_PR_SUMMARY_ID:
                return "vnd.android.cursor.item/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE;

            case NPI_TRACKER_LAST_SYNCH:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE;
            case NPI_TRACKER_LAST_SYNCH_ID:
                return "vnd.android.cursor.item/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE;

            case NPI_TRACKER_USER_SELECTED_NPI:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE;
            case NPI_TRACKER_USER_SELECTED_NPI_ID:
                return "vnd.android.cursor.item/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE;

           /* case NPI_TRACKER_NPIDETAILS:
                return "vnd.android.cursor.dir/vnd.com.juniper.jdi."+ NPITrackerDBHelper.NPI_TRACKER_DETAILS_TABLE;*/
            default:
                throw new IllegalArgumentException("Invalid URI: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        db=debugdbHelper.getWritableDatabase();
        long rowID=0;
        /**
         * Add a new  records
         */
        switch (uriMatcher.match(uri)){
            case NPI_TRACKER:
                rowID = db.insert(NPITrackerDBHelper.NPI_TRACKER_TABLE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case NPI_TRACKER_SINGLE:
                rowID = db.insert(NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case NPI_TRACKER_PHASEWISE:
                rowID = db.insert(NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case NPI_TRACKER_TEST_STATUS:
                rowID = db.insert(NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case NPI_TRACKER_RLI_STATUS:
                rowID = db.insert(NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case NPI_TRACKER_PR_SUMMARY:
                rowID = db.insert(NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case NPI_TRACKER_LAST_SYNCH:
                rowID = db.insert(NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case NPI_TRACKER_USER_SELECTED_NPI:
                rowID = db.insert(NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);

           /* case NPI_TRACKER_NPIDETAILS:
                rowID = db.insert(NPITrackerDBHelper.NPI_TRACKER_DETAILS_TABLE, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);*/
        }
        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(uri, rowID);

            Log.i("uri after insert",_uri.toString());
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count =0;
        db=debugdbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case NPI_TRACKER:
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_TABLE, null, null);
                break;
            case NPI_TRACKER_ID:
                String idStr1 = uri.getLastPathSegment();
                String where1 = NPI_TRACKER_ID + " = " + idStr1;
                if (!TextUtils.isEmpty(selection)) {
                    where1 += " AND " + selection;
                }
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_TABLE, where1, selectionArgs);

                break;
            case NPI_TRACKER_SINGLE:
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE, null, null);
                break;
            case NPI_TRACKER_SINGLE_ID:
                String idStr2 = uri.getLastPathSegment();
                String where2 = NPI_TRACKER_SINGLE_ID + " = " + idStr2;
                if (!TextUtils.isEmpty(selection)) {
                    where2 += " AND " + selection;
                }
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_SINGLE_API_TABLE, where2, selectionArgs);
            case NPI_TRACKER_PHASEWISE:
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE, null, null);
                break;
            case NPI_TRACKER_PHASEWISE_ID:
                String idStr3 = uri.getLastPathSegment();
                String where3 = NPI_TRACKER_PHASEWISE_ID + " = " + idStr3;
                if (!TextUtils.isEmpty(selection)) {
                    where3 += " AND " + selection;
                }
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_PHASEWISE_API_TABLE, where3, selectionArgs);
            case NPI_TRACKER_TEST_STATUS:
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE, null, null);
                break;
            case NPI_TRACKER_TEST_STATUS_ID:
                String idStr4 = uri.getLastPathSegment();
                String where4 = NPI_TRACKER_TEST_STATUS_ID + " = " + idStr4;
                if (!TextUtils.isEmpty(selection)) {
                    where4 += " AND " + selection;
                }
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_TEST_STATUS_API_TABLE, where4, selectionArgs);
            case NPI_TRACKER_RLI_STATUS:
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE, null, null);
                break;
            case NPI_TRACKER_RLI_STATUS_ID:
                String idStr5 = uri.getLastPathSegment();
                String where5 = NPI_TRACKER_RLI_STATUS + " = " + idStr5;
                if (!TextUtils.isEmpty(selection)) {
                    where5 += " AND " + selection;
                }
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_RLI_STATUS_TABLE, where5, selectionArgs);
            case NPI_TRACKER_PR_SUMMARY:
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE, null, null);
                break;
            case NPI_TRACKER_PR_SUMMARY_ID:
                String idStr6 = uri.getLastPathSegment();
                String where6 = NPI_TRACKER_PR_SUMMARY_ID + " = " + idStr6;
                if (!TextUtils.isEmpty(selection)) {
                    where6 += " AND " + selection;
                }
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_PR_SUMMARY_TABLE, where6, selectionArgs);
            case NPI_TRACKER_LAST_SYNCH:
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE, null, null);
                break;
            case NPI_TRACKER_LAST_SYNCH_ID:
                String idStr7 = uri.getLastPathSegment();
                String where7 = NPI_TRACKER_LAST_SYNCH_ID + " = " + idStr7;
                if (!TextUtils.isEmpty(selection)) {
                    where7 += " AND " + selection;
                }
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_LAST_SYNCH_TABLE, where7, selectionArgs);
            case NPI_TRACKER_USER_SELECTED_NPI:
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE,  selection, selectionArgs);

                break;
            case NPI_TRACKER_USER_SELECTED_NPI_ID:
                String idStr = uri.getLastPathSegment();
                String where = NPI_TRACKER_USER_SELECTED_NPI_ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                count = db.delete(NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException( "illegal uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String wheree, String[] whereArgs) {
        int count =0;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE);
        db=debugdbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case NPI_TRACKER_USER_SELECTED_NPI:
                count = db.update(NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE, contentValues, wheree, whereArgs);

                break;
            case NPI_TRACKER_USER_SELECTED_NPI_ID:
                String idStr = uri.getLastPathSegment();
                String where = NPI_TRACKER_USER_SELECTED_NPI_ID + " = " + idStr;
                if (!TextUtils.isEmpty(wheree)) {
                    where += " AND " + wheree;
                }
                count = db.update(NPITrackerDBHelper.NPI_TRACKER_USER_SELECTED_NPI_TABLE, contentValues, where, whereArgs);
                break;


            default:
                throw new IllegalArgumentException( "illegal uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
