package com.kmab.lancet.zimbabwe;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertLocations extends AsyncTask<SetterLocation, Void, Void> {

    private SQLiteDatabase db;
    DBOperations dbOperations;
    List listKeys;

    public InsertLocations(Context context, List listKeys) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.listKeys = listKeys;
    }

    @Override
    protected Void doInBackground(SetterLocation... params) {
        SetterLocation setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.Locations.KEY, setter.getKey());
        values.put(DBContract.Locations.BUILDING, setter.getBuilding());
        values.put(DBContract.Locations.ADDRESS, setter.getAddress());
        values.put(DBContract.Locations.CITY, setter.getCity());

        if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.Locations.TABLE_NAME, null, values);
        else
            db.update(DBContract.Locations.TABLE_NAME, values, DBContract.Locations.KEY + "='" + setter.getKey() + "'", null);

        listKeys = dbOperations.getLocationKeys(db);

        return null;
    }

}
