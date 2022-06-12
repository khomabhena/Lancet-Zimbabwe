package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class MemberDetails extends AppCompatActivity {

    int position;
    Context context;
    EditText etMemberName, etMemberSurname, etMedicalAidSociety, etEmail, etSuffix, etNo, etAddress, etEmployer, etPhone;
    ProgressBar progressBar;

    SetterForm setterForm;
    boolean isTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        context = this;
        position = getIntent().getExtras().getInt("position");
        isTimeline = getIntent().getExtras().getBoolean("isTimeline");

        etMemberName = findViewById(R.id.etMemberName);
        etMemberSurname = findViewById(R.id.etMemberSurname);
        etMedicalAidSociety = findViewById(R.id.etMedicalAidSociety);
        etEmail = findViewById(R.id.etEmail);
        etSuffix = findViewById(R.id.etSuffix);
        etNo = findViewById(R.id.etNo);
        etAddress = findViewById(R.id.etAddress);
        etEmployer = findViewById(R.id.etEmployer);
        etPhone = findViewById(R.id.etPhone);
        progressBar = findViewById(R.id.progressBar);

        if (isTimeline)
            setterForm = (SetterForm) Timeline.list.get(position);
        else
            setterForm = (SetterForm) Submissions.list.get(position);

        etMemberName.setText(setterForm.getMemberName());
        etMemberSurname.setText(setterForm.getMemberSurname());
        etMedicalAidSociety.setText(setterForm.getMedicalAid());
        etEmail.setText(setterForm.getEmail());
        etSuffix.setText(setterForm.getSuffix());
        etNo.setText(setterForm.getNo());
        etAddress.setText(setterForm.getAddress());
        etEmployer.setText(setterForm.getEmployer());
        etPhone.setText(setterForm.getPhone());
    }

    public void save(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String memberName = getText(etMemberName);
        String memberSurname= getText(etMemberSurname);
        String medicalAid = getText(etMedicalAidSociety);
        String email = getText(etEmail);
        String suffix = getText(etSuffix);
        String no = getText(etNo);
        String address = getText(etAddress);
        String employer = getText(etEmployer);
        String phone = getText(etPhone);


        setterForm.setMemberName(memberName);
        setterForm.setMemberSurname(memberSurname);
        setterForm.setMedicalAid(medicalAid);
        setterForm.setEmail(email);
        setterForm.setSuffix(suffix);
        setterForm.setNo(no);
        setterForm.setAddress(address);
        setterForm.setEmployer(employer);
        setterForm.setPhone(phone);

        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childSubmissions)
                .child(setterForm.getUid())
                .child(setterForm.getKey())
                .setValue(setterForm)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();

                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

}
