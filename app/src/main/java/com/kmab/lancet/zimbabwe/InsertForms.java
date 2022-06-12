package com.kmab.lancet.zimbabwe;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertForms extends AsyncTask<SetterForm, Void, Void> {

    private SQLiteDatabase db;
    DBOperations dbOperations;
    List listKeys;

    public InsertForms(Context context, List listKeys) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.listKeys = listKeys;
    }

    @Override
    protected Void doInBackground(SetterForm... params) {
        SetterForm setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.Forms.KEY, setter.getKey());
        values.put(DBContract.Forms.UID, setter.getUid());
        values.put(DBContract.Forms.PATIENT_NAME, setter.getPatientName());
        values.put(DBContract.Forms.PATIENT_SURNAME, setter.getPatientSurname());
        values.put(DBContract.Forms.RELATIONSHIP_TO_MEMBER, setter.getRelationshipToMember());
        values.put(DBContract.Forms.TISSUE_SAMPLE, setter.getTissueSample());
        values.put(DBContract.Forms.MEMBER_NAME, setter.getMemberName());
        values.put(DBContract.Forms.MEMBER_SURNAME, setter.getMemberSurname());
        values.put(DBContract.Forms.MEDICAL_AID, setter.getMedicalAid());
        values.put(DBContract.Forms.EMAIL, setter.getEmail());
        values.put(DBContract.Forms.SUFFIX, setter.getSuffix());
        values.put(DBContract.Forms.NO, setter.getNo());
        values.put(DBContract.Forms.ADDRESS, setter.getAddress());
        values.put(DBContract.Forms.EMPLOYER, setter.getEmployer());
        values.put(DBContract.Forms.PHONE, setter.getPhone());
        values.put(DBContract.Forms.SPECIMEN_TYPE, setter.getSpecimenType());
        values.put(DBContract.Forms.MEDICAL_LINK, setter.getMedicalLink());
        values.put(DBContract.Forms.FORM_LINK, setter.getFormLink());
        values.put(DBContract.Forms.PATIENT_DOB, setter.getPatientDOB());
        values.put(DBContract.Forms.TIMESTAMP, setter.getTimestamp());
        values.put(DBContract.Forms.IS_SEEN, setter.isSeen() ? "yes": "no");
        values.put(DBContract.Forms.IS_MALE, setter.isMale() ? "yes": "no");

        if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.Forms.TABLE_NAME, null, values);
        else
            db.update(DBContract.Forms.TABLE_NAME, values, DBContract.Forms.KEY + "='" + setter.getKey() + "'", null);

        listKeys = dbOperations.getFormKeys(db);

        return null;
    }

}
