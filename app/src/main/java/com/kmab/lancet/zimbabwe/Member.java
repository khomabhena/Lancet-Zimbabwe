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
import android.widget.EditText;
import android.widget.Toast;

public class Member extends AppCompatActivity {

    Context context;
    EditText etMemberName, etMemberSurname, etMedicalAidSociety, etEmail, etSuffix, etNo, etAddress, etEmployer, etPhone;

    XEmptyFields xEmptyFields;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Details.list != null) {
            SetterForm setterForm = (SetterForm) Details.list.get(0);
            etMemberName.setText(setterForm.getMemberName());
            etMemberSurname.setText(setterForm.getMemberSurname());
            etMedicalAidSociety.setText(setterForm.getMedicalAid());
            etEmail.setText(setterForm.getEmail());
            etSuffix.setText(setterForm.getSuffix());
            etNo.setText(setterForm.getNo());
            etAddress.setText(setterForm.getAddress());
            etEmployer.setText(setterForm.getEmployer());
            etPhone.setText(setterForm.getPhone());
        } else {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = prefs.edit();
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
        else
            setupSetter();
    }

    private void setupSetter() {
        String memberName = getText(etMemberName);
        String memberSurname= getText(etMemberSurname);
        String medicalAid = getText(etMedicalAidSociety);
        String email = getText(etEmail);
        String suffix = getText(etSuffix);
        String no = getText(etNo);
        String address = getText(etAddress);
        String employer = getText(etEmployer);
        String phone = getText(etPhone);

        SetterForm setterForm = (SetterForm) Details.list.get(0);
        setterForm.setMemberName(memberName);
        setterForm.setMemberSurname(memberSurname);
        setterForm.setMedicalAid(medicalAid);
        setterForm.setEmail(email);
        setterForm.setSuffix(suffix);
        setterForm.setNo(no);
        setterForm.setAddress(address);
        setterForm.setEmployer(employer);
        setterForm.setPhone(phone);

        startActivity(new Intent(context, MedicalAid.class));
    }

    private String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
