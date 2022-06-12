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

public class SendForm extends AppCompatActivity {

    Context context;
    EditText etName, etSurname, etRelationship, etTissueSample, etOther;
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

    String specimenType = "";

    EditText etMemberName, etMemberSurname, etMedicalAidSociety, etEmail, etSuffix, etNo, etAddress, etEmployer, etPhone;
    CheckBox checkBlood, checkUrine, checkSwab, checkFluid, checkStool, checkOther;

    XEmptyFields xEmptyFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        etOther = findViewById(R.id.etOther);

        xEmptyFields = new XEmptyFields();
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        etMemberName = findViewById(R.id.etMemberName);
        etMemberSurname = findViewById(R.id.etMemberSurname);
        etMedicalAidSociety = findViewById(R.id.etMedicalAidSociety);
        etEmail = findViewById(R.id.etEmail);
        etSuffix = findViewById(R.id.etSuffix);
        etNo = findViewById(R.id.etNo);
        etAddress = findViewById(R.id.etAddress);
        etEmployer = findViewById(R.id.etEmployer);
        etPhone = findViewById(R.id.etPhone);

        checkBlood = findViewById(R.id.checkBlood);
        checkUrine = findViewById(R.id.checkUrine);
        checkSwab = findViewById(R.id.checkSwab);
        checkFluid = findViewById(R.id.checkFluid);
        checkStool = findViewById(R.id.checkStool);
        checkOther = findViewById(R.id.checkOther);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFemale = findViewById(R.id.checkFemale);

        etName.setText(prefs.getString(AppInfo.P_NAME, ""));
        etSurname.setText(prefs.getString(AppInfo.P_Surname, ""));
        etRelationship.setText(prefs.getString(AppInfo.P_RALATIONSHIP, ""));
        etTissueSample.setText(prefs.getString(AppInfo.P_TISSUE_SAMPLE, ""));
        long date = prefs.getLong(AppInfo.P_DOB, System.currentTimeMillis());
        dob = prefs.getLong(AppInfo.P_DOB, 0);
        tvDateOfBirth.setText(new XDatePicker(context, tvDateOfBirth, tvDateOfBirth).getDate(date, false));

        etMemberName.setText(prefs.getString(AppInfo.M_NAME, ""));
        etMemberSurname.setText(prefs.getString(AppInfo.M_SURNAME, ""));
        etMedicalAidSociety.setText(prefs.getString(AppInfo.M_MEDICAL_AID, ""));
        etEmail.setText(prefs.getString(AppInfo.M_EMAIL, ""));
        etSuffix.setText(prefs.getString(AppInfo.M_SUFFIX, ""));
        etNo.setText(prefs.getString(AppInfo.M_NO, ""));
        etAddress.setText(prefs.getString(AppInfo.M_RES, ""));
        etEmployer.setText(prefs.getString(AppInfo.M_EMPLOYER, ""));
        etPhone.setText(prefs.getString(AppInfo.M_PHONE, ""));
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

        editor.putString(AppInfo.M_NAME, etMemberName.getText().toString());
        editor.putString(AppInfo.M_SURNAME, etMemberSurname.getText().toString());
        editor.putString(AppInfo.M_MEDICAL_AID, etMedicalAidSociety.getText().toString());
        editor.putString(AppInfo.M_EMAIL, etEmail.getText().toString());
        editor.putString(AppInfo.M_SUFFIX, etSuffix.getText().toString());
        editor.putString(AppInfo.M_NO, etNo.getText().toString());
        editor.putString(AppInfo.M_RES, etAddress.getText().toString());
        editor.putString(AppInfo.M_EMPLOYER, etEmployer.getText().toString());
        editor.putString(AppInfo.M_PHONE, etPhone.getText().toString());
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
        else
            nextStep();
    }

    public void nextStep() {
        if (xEmptyFields.isFieldEmpty(etMemberName))
            showToast("Enter member's name");
        else if (xEmptyFields.isFieldEmpty(etMemberSurname))
            showToast("Enter member's surname");
        else if (xEmptyFields.isFieldEmpty(etMemberSurname))
            showToast("Enter medical aid society");
        else if (xEmptyFields.isFieldEmpty(etEmail))
            showToast("Enter email address");
        else if (xEmptyFields.isFieldEmpty(etAddress))
            showToast("Enter residential address");
        else if (xEmptyFields.isFieldEmpty(etPhone))
            showToast("Enter phone number");
        else if (checkOther.isChecked() && etOther.getText().toString().trim().equals(""))
            Toast.makeText(context, "Specify specimen type", Toast.LENGTH_SHORT).show();
        else if (!checkOther.isChecked() && specimenType.equals(""))
            Toast.makeText(context, "Select specimen type", Toast.LENGTH_SHORT).show();
        else
            setupSetter();
    }

    public void checkItem(View view) {
        int id = view.getId();
        if (view.getId() == R.id.checkOther) {
            /*if (checkOther.isChecked()) {
                specimenType = "";
            } else
                specimenType = "";*/
        }
        else if (id == R.id.checkBlood) {
            String type = checkBlood.getText().toString();
            if (checkBlood.isChecked())
                addCheck(type);
            else
                removeCheck(type);
        }
        else if (id == R.id.checkUrine) {
            String type = checkUrine.getText().toString();
            if (checkUrine.isChecked())
                addCheck(type);
            else
                removeCheck(type);
        }
        else if (id == R.id.checkSwab) {
            String type = checkSwab.getText().toString();
            if (checkSwab.isChecked())
                addCheck(type);
            else
                removeCheck(type);
        }
        else if (id == R.id.checkFluid) {
            String type = checkFluid.getText().toString();
            if (checkFluid.isChecked())
                addCheck(type);
            else
                removeCheck(type);
        }
        else if (id == R.id.checkStool) {
            String type = checkStool.getText().toString();
            if (checkStool.isChecked())
                addCheck(type);
            else
                removeCheck(type);
        }
    }

    private void addCheck(String type) {
        specimenType += type + ", ";
    }

    private void removeCheck(String type) {
        specimenType = specimenType.replace(type + ", ", "");
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
            setterForm.setTissueSample(tissueSample);
        }
        setMemberSetter();
    }

    private void setMemberSetter() {
        String memberName = getText(etMemberName);
        String memberSurname = getText(etMemberSurname);
        String medicalAid = getText(etMedicalAidSociety);
        String email = getText(etEmail);
        String suffix = getText(etSuffix);
        String no = getText(etNo);
        String address = getText(etAddress);
        String employer = getText(etEmployer);
        String phone = getText(etPhone);

        SetterForm setterForm = (SetterForm) list.get(0);
        setterForm.setMemberName(memberName);
        setterForm.setMemberSurname(memberSurname);
        setterForm.setMedicalAid(medicalAid);
        setterForm.setEmail(email);
        setterForm.setSuffix(suffix);
        setterForm.setNo(no);
        setterForm.setAddress(address);
        setterForm.setEmployer(employer);
        setterForm.setPhone(phone);

        if (!etOther.getText().toString().trim().equals(""))
            if (checkOther.isChecked())
                addCheck(etOther.getText().toString());
            else
                removeCheck(etOther.getText().toString());

        setterForm.setSpecimenType(specimenType);

        startActivity(new Intent(context, MedicalAid.class));
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

    private String getText(EditText editText) {
        return editText.getText().toString().trim();
    }


}
