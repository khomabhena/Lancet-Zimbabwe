package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {

    Context context;
    EditText etName, etSurname, etRelationship, etTissueSample;
    TextView tvDateOfBirth;
    CheckBox checkMale, checkFemale;
    boolean isMale = false, isFemale = false, gender = false;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    static long dob = 0;
    String key, uid;
    String databaseRoot = StaticVals.childSubmissions;
    static List list;
    XEmptyFields isFieldEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        isFieldEmpty = new XEmptyFields();
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etRelationship = findViewById(R.id.etRelationship);
        tvDateOfBirth = findViewById(R.id.tvDateOfBirth);
        checkMale = findViewById(R.id.checkMale);
        etTissueSample = findViewById(R.id.etTissueSample);
    }

    @Override
    protected void onResume() {
        checkFemale = findViewById(R.id.checkFemale);
        super.onResume();
        if (list != null) {
            SetterForm setterForm = (SetterForm) list.get(0);
            etName.setText(setterForm.getPatientName());
            etSurname.setText(setterForm.getPatientSurname());
            etRelationship.setText(setterForm.getRelationshipToMember());
            etTissueSample.setText(setterForm.getTissueSample());
            if (setterForm.isMale()) {
                isMale = true;
                checkMale.setChecked(true);
            } else {
                isFemale = true;
                checkFemale.setChecked(true);
            }

            String date = new XTimestampDates().getDate(setterForm.getPatientDOB());
            tvDateOfBirth.setText(date);
        } else {
            etName.setText(prefs.getString(AppInfo.P_NAME, ""));
            etSurname.setText(prefs.getString(AppInfo.P_Surname,""));
            etRelationship.setText(prefs.getString(AppInfo.P_RALATIONSHIP, ""));
            etTissueSample.setText(prefs.getString(AppInfo.P_TISSUE_SAMPLE, ""));
            long date = prefs.getLong(AppInfo.P_DOB, System.currentTimeMillis());
            tvDateOfBirth.setText(new XDatePicker(context, tvDateOfBirth, tvDateOfBirth).getDate(date, false));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = prefs.edit();
        editor.putString(AppInfo.P_NAME, etName.getText().toString());
        editor.putString(AppInfo.P_Surname, etSurname.getText().toString());
        editor.putString(AppInfo.P_RALATIONSHIP, etRelationship.getText().toString());
        editor.putLong(AppInfo.P_DOB, dob);
        editor.putString(AppInfo.P_TISSUE_SAMPLE,  etTissueSample.getText().toString());
        editor.apply();
    }

    public void nextStep(View view) {
        if (isFieldEmpty.isFieldEmpty(etName)) {
            showToast("Enter patient's name");
        } else if (isFieldEmpty.isFieldEmpty(etSurname)) {
            showToast("Enter patient's surname");
        } else if (dob == 0) {
            showToast("Enter patient's age");
        } else if (!isMale && !isFemale) {
            showToast("Select patient's gender");
        }
        else {
            setupSetter();
        }
    }

    private void setupSetter() {
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        key = FirebaseDatabase.getInstance().getReference().child(databaseRoot).child(uid).push().getKey();

        String patientName = etName.getText().toString();
        String patientSurname = etSurname.getText().toString();
        String relationshipToMember = etRelationship.getText().toString();
        String tissueSample = etTissueSample.getText().toString();

        if (list == null) {
            list = new ArrayList();
            SetterForm setterForm = new SetterForm(key, uid,patientName, patientSurname, relationshipToMember, tissueSample,
                    null, null,null, null, null,null,
                    null, null, null, null, null, null,
                    dob, false, gender, 0);

            list.add(setterForm);
        } else {
            SetterForm setterForm = (SetterForm) list.get(0);
            setterForm.setUid(uid);
            setterForm.setKey(key);
            setterForm.setPatientName(patientName);
            setterForm.setPatientSurname(patientSurname);
            setterForm.setRelationshipToMember(relationshipToMember);
            setterForm.setMale(isMale);
        }
        startActivity(new Intent(context, Member.class));
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }



    public void pickDate(View view) {
        new XDatePicker(context, tvDateOfBirth, tvDateOfBirth).pickDate();
    }

    public void checkGender(View view) {
        if (view.getId() == R.id.checkMale) {
            if (checkMale.isChecked()) {
                isMale = true;
                gender = true;
            } else {
                if (!checkFemale.isChecked()) {
                    isMale = false;
                }
            }
            isFemale = false;
            checkFemale.setChecked(false);
        }
        if (view.getId() == R.id.checkFemale) {
            if (checkFemale.isChecked()) {
                isFemale = true;
                gender = false;
            } else {
                if (!checkMale.isChecked()) {
                    isFemale = false;
                }
            }
            isMale = false;
            checkMale.setChecked(false);
        }
    }

}
