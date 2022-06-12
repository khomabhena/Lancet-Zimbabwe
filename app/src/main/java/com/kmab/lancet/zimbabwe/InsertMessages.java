package com.kmab.lancet.zimbabwe;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertMessages extends AsyncTask<SetterMessage, Void, Void> {

    private SQLiteDatabase db;
    DBOperations dbOperations;
    List listKeys;

    public InsertMessages(Context context, List listKeys) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.listKeys = listKeys;
    }

    @Override
    protected Void doInBackground(SetterMessage... params) {
        SetterMessage setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.Messages.KEY, setter.getKey());
        values.put(DBContract.Messages.UID, setter.getUid());
        values.put(DBContract.Messages.MESSAGE, setter.getMessage());
        values.put(DBContract.Messages.TIMESTAMP, setter.getTimestamp());
        values.put(DBContract.Messages.IS_SEEN, setter.isSeen() ? "yes": "no");
        values.put(DBContract.Messages.IS_REPLIED, setter.isReplied() ? "yes": "no");

        if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.Messages.TABLE_NAME, null, values);
        else
            db.update(DBContract.Messages.TABLE_NAME, values, DBContract.Messages.KEY + "='" + setter.getKey() + "'", null);

        listKeys = dbOperations.getMessageKeys(db);

        return null;
    }

}
