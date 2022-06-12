package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String firstPin = "";

    private String adminPin = "";

    Context context;
    ProgressBar progressBar;
    TextView tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);
        tvCancel = findViewById(R.id.tvCancel);

        checkPin();
        getPin();

        if (prefs.getBoolean(AppInfo.IS_ADMIN, false))
            tvCancel.setVisibility(View.VISIBLE);
    }

    private void getPin() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child(StaticVals.childPin)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        SetterPin setterPin = dataSnapshot.getValue(SetterPin.class);
                        adminPin = setterPin.getAdminPin();

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void checkPin() {
        TextView tvPassCode = (TextView) findViewById(R.id.tvPassCode);
        tvPassCode.setText("Admin Pin");
    }

    public void pinPressed(View view) {
        if (view.getId() == R.id.t0)
            checkPinAuthenticity();
        if (view.getId() == R.id.t1)
            keyPressed(1);
        if (view.getId() == R.id.t2)
            keyPressed(2);
        if (view.getId() == R.id.t3)
            keyPressed(3);
        if (view.getId() == R.id.t4)
            keyPressed(4);
        if (view.getId() == R.id.t5)
            keyPressed(5);
        if (view.getId() == R.id.t6)
            keyPressed(6);
        if (view.getId() == R.id.t7)
            keyPressed(7);
        if (view.getId() == R.id.t8)
            keyPressed(8);
        if (view.getId() == R.id.t9)
            keyPressed(9);
        if (view.getId() == R.id.tvCancel)
            keyPressed(1002);
        if (view.getId() == R.id.tvDelete)
            keyPressed(1001);
    }

    private void checkPinAuthenticity() {
        TextView tvFirst = findViewById(R.id.tvFirst);
        TextView tvSecond = findViewById(R.id.tvSecond);
        TextView tvThird = findViewById(R.id.tvThird);
        TextView tvForth = findViewById(R.id.tvForth);
        TextView tvPassCode = findViewById(R.id.tvPassCode);

        String newPin = tvFirst.getText().toString() + tvSecond.getText().toString() + tvThird.getText().toString() + tvForth.getText().toString();

        if (adminPin.equals(newPin)) {
            editor = prefs.edit();
            editor.putBoolean(AppInfo.IS_ADMIN, true);
            editor.putLong(AppInfo.LONG_LOGIN_TIME, System.currentTimeMillis());
            editor.apply();

            startActivity(new Intent(context, MainActivity.class));
            finish();
        } else {
            tvFirst.setText("");
            tvSecond.setText("");
            tvThird.setText("");
            tvForth.setText("");
            tvPassCode.setText("Wrong Pin: Re-enter pin");
        }
    }

    private void keyPressed(int i) {
        matchPin(i);
    }

    private void deleteValue() {
        TextView tvFirst = findViewById(R.id.tvFirst);
        TextView tvSecond = findViewById(R.id.tvSecond);
        TextView tvThird = findViewById(R.id.tvThird);
        TextView tvForth = findViewById(R.id.tvForth);

        if (!tvForth.getText().toString().trim().equals(""))
            tvForth.setText("");
        else if (!tvThird.getText().toString().trim().equals(""))
            tvThird.setText("");
        else if (!tvSecond.getText().toString().trim().equals(""))
            tvSecond.setText("");
        else if (!tvFirst.getText().toString().trim().equals(""))
            tvFirst.setText("");
    }

    private void matchPin(int i) {
        TextView tvFirst = findViewById(R.id.tvFirst);
        TextView tvSecond = findViewById(R.id.tvSecond);
        TextView tvThird = findViewById(R.id.tvThird);
        TextView tvForth = findViewById(R.id.tvForth);
        TextView tvPassCode = findViewById(R.id.tvPassCode);

        if (i == 1002) {
            changePin();
        } if (i == 1001)
            deleteValue();
        else {
            if (tvFirst.getText().toString().trim().equals(""))
                tvFirst.setText("" + i);
            else if (tvSecond.getText().toString().trim().equals(""))
                tvSecond.setText("" + i);
            else if (tvThird.getText().toString().trim().equals(""))
                tvThird.setText("" + i);
            else if (tvForth.getText().toString().trim().equals("")) {
                tvForth.setText("" + i);
            }
        }
    }

    private void changePin() {
        final TextView tvFirst = findViewById(R.id.tvFirst);
        final TextView tvSecond = findViewById(R.id.tvSecond);
        final TextView tvThird = findViewById(R.id.tvThird);
        final TextView tvForth = findViewById(R.id.tvForth);

        String newPin = tvFirst.getText().toString() + tvSecond.getText().toString() + tvThird.getText().toString() + tvForth.getText().toString();

        SetterPin setterPin = new SetterPin(newPin, System.currentTimeMillis());

        if (prefs.getBoolean(AppInfo.IS_ADMIN, false))
            FirebaseDatabase.getInstance().getReference()
                    .child(StaticVals.childPin)
                    .setValue(setterPin)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(context, "Changed", Toast.LENGTH_LONG).show();

                            tvFirst.setText("");
                            tvSecond.setText("");
                            tvThird.setText("");
                            tvForth.setText("");

                            getPin();
                        }
                    });
    }

    public void resetPin(View view) {
        changePin();
    }

}
