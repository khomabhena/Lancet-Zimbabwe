package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DBOperations extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1; //8
    private static final String DB_NAME = "lancet_zimbabwe.db";
    //private String localUid;

    Context context;

    DBOperations(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_12);
        db.execSQL(QUERY_13);
        db.execSQL(QUERY_KEYWORDS);
        db.execSQL(QUERY_QUEST);
        db.execSQL(QUERY_RESPONSES);
        db.execSQL(QUERY_ACTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Forms.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Messages.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Keywords.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Locations.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Responses.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions.TABLE_NAME);

        onCreate(db);
    }

    Cursor getForms(SQLiteDatabase db) {
        Cursor cursor;

        String[] projections = {
                DBContract.Forms.ID,
                DBContract.Forms.TIMESTAMP,
                DBContract.Forms.IS_SEEN,
                DBContract.Forms.IS_MALE,
                DBContract.Forms.KEY,
                DBContract.Forms.UID,
                DBContract.Forms.PATIENT_NAME,
                DBContract.Forms.PATIENT_SURNAME,
                DBContract.Forms.RELATIONSHIP_TO_MEMBER,
                DBContract.Forms.MEMBER_NAME,
                DBContract.Forms.MEMBER_SURNAME,
                DBContract.Forms.MEDICAL_AID,
                DBContract.Forms.EMAIL,
                DBContract.Forms.SUFFIX,
                DBContract.Forms.NO,
                DBContract.Forms.ADDRESS,
                DBContract.Forms.EMPLOYER,
                DBContract.Forms.PHONE,
                DBContract.Forms.SPECIMEN_TYPE,
                DBContract.Forms.MEDICAL_LINK,
                DBContract.Forms.FORM_LINK,
                DBContract.Forms.PATIENT_DOB,
                DBContract.Forms.TISSUE_SAMPLE
        };

// + DBContract.Event.TIMESTAMP + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.Forms.TABLE_NAME, projections,
                null,
                null,
                DBContract.Forms.KEY,
                null,
                DBContract.Forms.TIMESTAMP + " DESC",
                null
        );

        return cursor;
    }

    List<String> getFormKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Forms.KEY};
        cursor = db.query(true, DBContract.Forms.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Forms.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getLocations(SQLiteDatabase db) {
        Cursor cursor;
        String[] projections = {
                DBContract.Locations.ID,
                DBContract.Locations.KEY,
                DBContract.Locations.BUILDING,
                DBContract.Locations.ADDRESS,
                DBContract.Locations.CITY
        };

        cursor = db.query(
                true,
                DBContract.Locations.TABLE_NAME, projections,
                null,
                null,
                DBContract.Locations.KEY,
                null,
                DBContract.Locations.CITY + " DESC",
                null
        );

        return cursor;
    }

    List<String> getLocationKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Locations.KEY};
        cursor = db.query(true, DBContract.Locations.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Locations.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getMessages(SQLiteDatabase db) {
        Cursor cursor;

        String[] projections = {
                DBContract.Messages.ID,
                DBContract.Messages.KEY,
                DBContract.Messages.UID,
                DBContract.Messages.MESSAGE,
                DBContract.Messages.IS_SEEN,
                DBContract.Messages.IS_REPLIED,
                DBContract.Messages.TIMESTAMP
        };

        cursor = db.query(
                true,
                DBContract.Messages.TABLE_NAME, projections,
                null,
                null,
                DBContract.Messages.KEY,
                null,
                DBContract.Messages.TIMESTAMP + " DESC",
                null
        );

        return cursor;
    }

    List<String> getMessageKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Messages.KEY};
        cursor = db.query(true, DBContract.Messages.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Messages.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getExcluded(SQLiteDatabase db, String language) {
        Cursor cursor;
        String[] projections = {
                DBContract.Forms.ID,
                DBContract.Forms.KEY,
                DBContract.Forms.PATIENT_NAME,
                DBContract.Forms.PATIENT_SURNAME
        };

        cursor = db.query(
                true,
                DBContract.Forms.TABLE_NAME, projections,
                DBContract.Forms.RELATIONSHIP_TO_MEMBER + "='" + language + "' AND " +
                        DBContract.Forms.PATIENT_SURNAME + "='yes'",
                null,
                DBContract.Forms.KEY,
                null,
                DBContract.Forms.ID + " ASC",
                null
        );

        return cursor;
    }

    List<String> getExcludedKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Forms.KEY};
        cursor = db.query(true, DBContract.Forms.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Forms.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getKeywords(SQLiteDatabase db, String language, String type) {
        Cursor cursor;
        String[] projections = {
                DBContract.Keywords.ID,
                DBContract.Keywords.KEY,
                DBContract.Keywords.NAME,
                DBContract.Keywords.TYPE,
                DBContract.Keywords.ENABLED
        };

        cursor = db.query(
                true,
                DBContract.Keywords.TABLE_NAME, projections,
                DBContract.Keywords.LANGUAGE + "='" + language + "' AND " +
                        DBContract.Keywords.TYPE + "='" + type + "' ",
                null,
                DBContract.Keywords.KEY,
                null,
                DBContract.Keywords.ID + " ASC",
                null
        );

        return cursor;
    }

    List<String> getKeywordsKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Keywords.KEY};
        cursor = db.query(true, DBContract.Keywords.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Keywords.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getChats(SQLiteDatabase db, String language) {
        Cursor cursor;
        String[] projections = {
                DBContract.Messages.ID,
                DBContract.Messages.KEY,
                DBContract.Messages.UID,
                DBContract.Messages.MESSAGE,
                DBContract.Messages.IS_SEEN,
                DBContract.Messages.QUESTION,
                DBContract.Messages.TIMESTAMP
        };
// + DBContract.Event.TIMESTAMP + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.Messages.TABLE_NAME, projections,
                DBContract.Messages.LANGUAGE + "='" + language + "'",
                null,
                DBContract.Messages.ID,
                null,
                DBContract.Messages.TIMESTAMP + " ASC",
                null
        );

        return cursor;
    }



    private static final String QUERY_12 = "CREATE TABLE "+ DBContract.Forms.TABLE_NAME +"("+
            DBContract.Forms.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Forms.TIMESTAMP + " TEXT, " +
            DBContract.Forms.IS_SEEN + " TEXT, " +
            DBContract.Forms.IS_MALE + " TEXT, " +
            DBContract.Forms.KEY + " TEXT, " +
            DBContract.Forms.UID + " TEXT, " +
            DBContract.Forms.PATIENT_NAME + " TEXT, " +
            DBContract.Forms.PATIENT_SURNAME + " TEXT, " +
            DBContract.Forms.RELATIONSHIP_TO_MEMBER + " TEXT, " +
            DBContract.Forms.MEMBER_NAME + " TEXT, " +
            DBContract.Forms.MEMBER_SURNAME + " TEXT, " +
            DBContract.Forms.MEDICAL_AID + " TEXT, " +
            DBContract.Forms.EMAIL + " TEXT, " +
            DBContract.Forms.SUFFIX + " TEXT, " +
            DBContract.Forms.NO + " TEXT, " +
            DBContract.Forms.ADDRESS + " TEXT, " +
            DBContract.Forms.EMPLOYER + " TEXT, " +
            DBContract.Forms.PHONE + " TEXT, " +
            DBContract.Forms.SPECIMEN_TYPE + " TEXT, " +
            DBContract.Forms.MEDICAL_LINK + " TEXT, " +
            DBContract.Forms.FORM_LINK + " TEXT, " +
            DBContract.Forms.TISSUE_SAMPLE + " TEXT, " +
            DBContract.Forms.PATIENT_DOB +" TEXT);";

    private static final String QUERY_13 = "CREATE TABLE "+ DBContract.Messages.TABLE_NAME +"("+
            DBContract.Messages.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Messages.LOCAL_UID + " TEXT, " +
            DBContract.Messages.KEY + " TEXT, " +
            DBContract.Messages.UID + " TEXT, " +
            DBContract.Messages.MESSAGE + " TEXT, " +
            DBContract.Messages.IS_SEEN + " TEXT, " +
            DBContract.Messages.QUESTION + " TEXT, " +
            DBContract.Messages.TIMESTAMP + " TEXT, " +
            DBContract.Messages.LANGUAGE + " TEXT, " +
            DBContract.Messages.IS_REPLIED + " TEXT, " +
            DBContract.Messages.COl_9 + " TEXT, " +
            DBContract.Messages.COl_10 + " TEXT, " +
            DBContract.Messages.COl_11 + " TEXT, " +
            DBContract.Messages.COl_12 + " TEXT, " +
            DBContract.Messages.COl_13 + " TEXT, " +
            DBContract.Messages.COl_14 + " TEXT, " +
            DBContract.Messages.COl_15 + " TEXT, " +
            DBContract.Messages.COl_16 + " TEXT, " +
            DBContract.Messages.COl_17 + " TEXT, " +
            DBContract.Messages.COl_18 + " TEXT, " +
            DBContract.Messages.COl_19 + " TEXT, " +
            DBContract.Messages.COl_20 +" TEXT);";

    private static final String QUERY_QUEST = "CREATE TABLE "+ DBContract.Locations.TABLE_NAME +"("+
            DBContract.Locations.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Locations.LOCAL_UID + " TEXT, " +
            DBContract.Locations.KEY + " TEXT, " +
            DBContract.Locations.BUILDING + " TEXT, " +
            DBContract.Locations.ADDRESS + " TEXT, " +
            DBContract.Locations.CITY + " TEXT, " +
            DBContract.Locations.QUESTION + " TEXT, " +
            DBContract.Locations.TIMESTAMP + " TEXT, " +
            DBContract.Locations.LANGUAGE + " TEXT, " +
            DBContract.Locations.COl_8 + " TEXT, " +
            DBContract.Locations.COl_9 + " TEXT, " +
            DBContract.Locations.COl_10 + " TEXT, " +
            DBContract.Locations.COl_11 + " TEXT, " +
            DBContract.Locations.COl_12 + " TEXT, " +
            DBContract.Locations.COl_13 + " TEXT, " +
            DBContract.Locations.COl_14 + " TEXT, " +
            DBContract.Locations.COl_15 + " TEXT, " +
            DBContract.Locations.COl_16 + " TEXT, " +
            DBContract.Locations.COl_17 + " TEXT, " +
            DBContract.Locations.COl_18 + " TEXT, " +
            DBContract.Locations.COl_19 + " TEXT, " +
            DBContract.Locations.COl_20 +" TEXT);";

    private static final String QUERY_RESPONSES = "CREATE TABLE "+ DBContract.Responses.TABLE_NAME +"("+
            DBContract.Responses.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Responses.LOCAL_UID + " TEXT, " +
            DBContract.Responses.KEY + " TEXT, " +
            DBContract.Responses.PARENT_KEY + " TEXT, " +
            DBContract.Responses.NAME + " TEXT, " +
            DBContract.Responses.RESPONSE + " TEXT, " +
            DBContract.Responses.ENABLED + " TEXT, " +
            DBContract.Responses.TIMESTAMP + " TEXT, " +
            DBContract.Responses.LANGUAGE + " TEXT, " +
            DBContract.Responses.COl_8 + " TEXT, " +
            DBContract.Responses.COl_9 + " TEXT, " +
            DBContract.Responses.COl_10 + " TEXT, " +
            DBContract.Responses.COl_11 + " TEXT, " +
            DBContract.Responses.COl_12 + " TEXT, " +
            DBContract.Responses.COl_13 + " TEXT, " +
            DBContract.Responses.COl_14 + " TEXT, " +
            DBContract.Responses.COl_15 + " TEXT, " +
            DBContract.Responses.COl_16 + " TEXT, " +
            DBContract.Locations.COl_17 + " TEXT, " +
            DBContract.Responses.COl_18 + " TEXT, " +
            DBContract.Responses.COl_19 + " TEXT, " +
            DBContract.Responses.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS = "CREATE TABLE "+ DBContract.Actions.TABLE_NAME +"("+
            DBContract.Actions.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Actions.LOCAL_UID + " TEXT, " +
            DBContract.Actions.KEY + " TEXT, " +
            DBContract.Actions.PARENT_KEY + " TEXT, " +
            DBContract.Actions.NAME + " TEXT, " +
            DBContract.Actions.ACTION + " TEXT, " +
            DBContract.Actions.ENABLED + " TEXT, " +
            DBContract.Actions.TIMESTAMP + " TEXT, " +
            DBContract.Actions.LANGUAGE + " TEXT, " +
            DBContract.Actions.COl_8 + " TEXT, " +
            DBContract.Actions.COl_9 + " TEXT, " +
            DBContract.Actions.COl_10 + " TEXT, " +
            DBContract.Actions.COl_11 + " TEXT, " +
            DBContract.Actions.COl_12 + " TEXT, " +
            DBContract.Actions.COl_13 + " TEXT, " +
            DBContract.Actions.COl_14 + " TEXT, " +
            DBContract.Actions.COl_15 + " TEXT, " +
            DBContract.Actions.COl_16 + " TEXT, " +
            DBContract.Actions.COl_17 + " TEXT, " +
            DBContract.Actions.COl_18 + " TEXT, " +
            DBContract.Actions.COl_19 + " TEXT, " +
            DBContract.Actions.COl_20 +" TEXT);";

    private static final String QUERY_KEYWORDS = "CREATE TABLE "+ DBContract.Keywords.TABLE_NAME +"("+
            DBContract.Keywords.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Keywords.LOCAL_UID + " TEXT, " +
            DBContract.Keywords.KEY + " TEXT, " +
            DBContract.Keywords.NAME + " TEXT, " +
            DBContract.Keywords.TYPE + " TEXT, " +
            DBContract.Keywords.ENABLED + " TEXT, " +
            DBContract.Keywords.QUESTION + " TEXT, " +
            DBContract.Keywords.TIMESTAMP + " TEXT, " +
            DBContract.Keywords.LANGUAGE + " TEXT, " +
            DBContract.Keywords.COl_8 + " TEXT, " +
            DBContract.Keywords.COl_9 + " TEXT, " +
            DBContract.Keywords.COl_10 + " TEXT, " +
            DBContract.Keywords.COl_11 + " TEXT, " +
            DBContract.Keywords.COl_12 + " TEXT, " +
            DBContract.Keywords.COl_13 + " TEXT, " +
            DBContract.Keywords.COl_14 + " TEXT, " +
            DBContract.Keywords.COl_15 + " TEXT, " +
            DBContract.Keywords.COl_16 + " TEXT, " +
            DBContract.Keywords.COl_17 + " TEXT, " +
            DBContract.Keywords.COl_18 + " TEXT, " +
            DBContract.Keywords.COl_19 + " TEXT, " +
            DBContract.Keywords.COl_20 +" TEXT);";


}