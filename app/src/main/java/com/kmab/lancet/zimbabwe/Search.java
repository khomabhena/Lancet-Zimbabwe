package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Search extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    EditText etSearch;

    Context context;
    static List list;

    DBOperations dbOperations;
    SQLiteDatabase db;
    List listKeys;
    AdapterSubmissions adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        etSearch = findViewById(R.id.etSearch);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        new BackTask().execute();
        loadData();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = etSearch.getText().toString();
                List listSearch = new ArrayList();
                for (int x = 0; x < list.size(); x++) {
                    SetterForm setterForm = (SetterForm) list.get(x);
                    if (setterForm.getPatientName().contains(searchText) || setterForm.getPatientSurname().contains(searchText) ||
                        setterForm.getMemberName().contains(searchText) || setterForm.getMemberSurname().contains(searchText) ||
                        setterForm.getEmail().contains(searchText) ||
                        setterForm.getPhone().contains(searchText)) {
                        listSearch.add(setterForm);
                    }
                }

                adapter = new AdapterSubmissions(listSearch, context);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                isMale= male.equals("yes");

                SetterForm setter = new SetterForm(key, uid, patientName, patientSurname,
                        relationshipToMember, tissueSample, memberName, memberSurname,
                        medicalAid, email, suffix, no, address,
                        employer, phone, specimenType, medicalLink, formLink,
                        patientDOB, isSeen, isMale, timestamp);

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
            /*if (count != 0) {
                adapter = new AdapterSubmissions(list, context);
                recyclerView.setAdapter(adapter);
            }*/
        }
    }

    class AdapterSubmissions extends RecyclerView.Adapter<AdapterSubmissions.Holder> {

        private List listAdapter;
        Context context;
        List<String> listTime;

        public AdapterSubmissions(List listAdapter, Context context) {
            this.listAdapter = listAdapter;
            this.context = context;
            listTime = new ArrayList<>();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_submissions;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterForm) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvTime, tvPatientName, tvSurname, tvDOB, tvRelationship, tvGender, tvSpecimenType, tvTissueSample;
            CardView cardRow, cardMedicalAid, cardReply, cardForm;

            Holder(View itemView) {
                super(itemView);
                tvTime = itemView.findViewById(R.id.tvTime);
                cardRow = itemView.findViewById(R.id.cardRow);
                tvPatientName = itemView.findViewById(R.id.tvPatientName);
                tvSurname = itemView.findViewById(R.id.tvSurname);
                tvDOB = itemView.findViewById(R.id.tvDOB);
                tvRelationship = itemView.findViewById(R.id.tvRelationship);
                tvGender = itemView.findViewById(R.id.tvGender);
                tvSpecimenType = itemView.findViewById(R.id.tvSpecimenType);
                cardMedicalAid = itemView.findViewById(R.id.cardMedicalAid);
                cardReply = itemView.findViewById(R.id.cardReply);
                cardForm = itemView.findViewById(R.id.cardForm);
                tvTissueSample = itemView.findViewById(R.id.tvTissueSample);
            }

            void bind(final SetterForm setter) {
                String gender;
                if (setter.isMale())
                    gender = "Male";
                else
                    gender = "Female";

                tvTime.setText(getSubmissionDate(setter.getTimestamp()));
                tvPatientName.setText(setter.getPatientName());
                tvSurname.setText(setter.getPatientSurname());
                tvDOB.setText(getDate(setter.getPatientDOB(), false));
                tvRelationship.setText(setter.getRelationshipToMember());
                tvGender.setText(gender);
                tvSpecimenType.setText(setter.getSpecimenType());
                tvTissueSample.setText(setter.getTissueSample());

                cardMedicalAid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchIntent(setter.getMedicalLink());
                    }
                });
                cardForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchIntent(setter.getFormLink());
                    }
                });
                cardReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, Reply.class);
                        intent.putExtra("position", getAdapterPosition());
                        context.startActivity(intent);
                    }
                });
                cardRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, MemberDetails.class);
                        intent.putExtra("isTimeline", false);
                        intent.putExtra("position", getAdapterPosition());
                        context.startActivity(intent);
                    }
                });
            }

            private void launchIntent(String medicalLink) {
                Intent intent = new Intent(context, DisplayImage.class);
                intent.putExtra("link", medicalLink);
                context.startActivity(intent);
            }

            private String getSubmissionDate(long timestamp) {
                if (getDate(timestamp, false).equals(getDate(System.currentTimeMillis(), false))) {
                    return "Today, " + getDate(timestamp, true);
                } else {
                    return getDate(timestamp, false) + ", " + getDate(timestamp, true);
                }
            }

            String getDate(long timeReceived, boolean isTime) {
                String[] monthsSmall = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timeReceived);


                if (isTime) {
                    return "" + getTheValue(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + getTheValue(calendar.get(Calendar.MINUTE));
                } else {
                    return "" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + monthsSmall[calendar.get(Calendar.MONTH) + 1] + "-" +
                            getYear(calendar.get(Calendar.YEAR));
                }
                //

            }

            private String getYear(int year) {
                String year2 = String.valueOf(year);
                return year2.substring(2, 4);
            }

            public String getTheValue(int value) {
                String theValue = String.valueOf(value);
                if (theValue.length() == 1) {
                    return "0" + theValue;
                } else {
                    return theValue;
                }
            }

        }

    }

}
