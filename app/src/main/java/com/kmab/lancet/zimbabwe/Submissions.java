package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Submissions extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;

    Context context;
    static List list;

    DBOperations dbOperations;
    SQLiteDatabase db;
    List listKeys;
    AdapterSubmissions adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        dbOperations = new DBOperations(this);
        db = dbOperations.getWritableDatabase();
        listKeys = dbOperations.getFormKeys(db);
        list = new ArrayList();
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        new BackTask().execute();
        loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childSubmissions)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            for (DataSnapshot snap: snapshot.getChildren()) {
                                SetterForm setterForm = snap.getValue(SetterForm.class);

                                new InsertForms(context, listKeys).execute(setterForm);
                            }
                        }

                        new BackTask().execute();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



    private class BackTask extends AsyncTask<Void, SetterForm, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor = dbOperations.getForms(db);
            int count = cursor.getCount();

            String key, uid, patientName, patientSurname, relationshipToMember, tissueSample,
                    memberName, memberSurname, medicalAid, email, suffix, no, address, employer, phone, specimenType,
                    medicalLink, formLink;
            long patientDOB, timestamp;
            boolean isSeen, isMale;

            list = new ArrayList();
            while (cursor.moveToNext()) {

                key = cursor.getString(cursor.getColumnIndex(DBContract.Forms.KEY));
                uid = cursor.getString(cursor.getColumnIndex(DBContract.Forms.UID));
                patientName = cursor.getString(cursor.getColumnIndex(DBContract.Forms.PATIENT_NAME));
                patientSurname = cursor.getString(cursor.getColumnIndex(DBContract.Forms.PATIENT_SURNAME));
                relationshipToMember = cursor.getString(cursor.getColumnIndex(DBContract.Forms.RELATIONSHIP_TO_MEMBER));
                tissueSample = cursor.getString(cursor.getColumnIndex(DBContract.Forms.TISSUE_SAMPLE));
                memberName = cursor.getString(cursor.getColumnIndex(DBContract.Forms.MEMBER_NAME));
                memberSurname = cursor.getString(cursor.getColumnIndex(DBContract.Forms.MEMBER_SURNAME));
                medicalAid = cursor.getString(cursor.getColumnIndex(DBContract.Forms.MEDICAL_AID));
                email = cursor.getString(cursor.getColumnIndex(DBContract.Forms.EMAIL));
                suffix = cursor.getString(cursor.getColumnIndex(DBContract.Forms.SUFFIX));
                no = cursor.getString(cursor.getColumnIndex(DBContract.Forms.NO));
                address = cursor.getString(cursor.getColumnIndex(DBContract.Forms.ADDRESS));
                employer = cursor.getString(cursor.getColumnIndex(DBContract.Forms.EMPLOYER));
                phone = cursor.getString(cursor.getColumnIndex(DBContract.Forms.PHONE));
                specimenType = cursor.getString(cursor.getColumnIndex(DBContract.Forms.SPECIMEN_TYPE));
                medicalLink = cursor.getString(cursor.getColumnIndex(DBContract.Forms.MEDICAL_LINK));
                formLink = cursor.getString(cursor.getColumnIndex(DBContract.Forms.FORM_LINK));

                patientDOB = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBContract.Forms.PATIENT_DOB)));
                timestamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBContract.Forms.TIMESTAMP)));

                String male = cursor.getString(cursor.getColumnIndex(DBContract.Forms.IS_MALE));
                String seen = cursor.getString(cursor.getColumnIndex(DBContract.Forms.IS_SEEN));

                isSeen = seen.equals("yes");
                isMale = male.equals("yes");

                SetterForm setter = new SetterForm(key, uid, patientName, patientSurname,
                        relationshipToMember, tissueSample, memberName, memberSurname,
                        medicalAid, email, suffix, no, address,
                        employer, phone, specimenType, medicalLink, formLink,
                        patientDOB, isSeen, isMale, timestamp);

                if (!isSeen)
                    publishProgress(setter);
            }

            return count;
        }

        @Override
        protected void onProgressUpdate(SetterForm... values) {
            list.add(values[0]);
        }

        @Override
        protected void onPostExecute(Integer count) {
            if (count != 0) {
                adapter = new AdapterSubmissions(list, context);
                recyclerView.setAdapter(adapter);
            }
        }
    }

}
