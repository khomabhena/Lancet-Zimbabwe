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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Pin extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String firstPin = "";

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);

        checkPin();
    }

    private void checkPin() {
        String pin = prefs.getString(AppInfo.STRING_PIN, "");
        TextView tvPassCode = (TextView) findViewById(R.id.tvPassCode);
        if (pin.equals("")) {
            //New user
            tvPassCode.setText("Choose your Pin");
        } else {
            //Existing account
            tvPassCode.setText("Enter your Pin");
        }

    }

    public void pinPressed(View view) {
        if (view.getId() == R.id.t0)
            keyPressed(0);
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

    private void keyPressed(int i) {
        TextView tvFirst = findViewById(R.id.tvFirst);
        TextView tvSecond = findViewById(R.id.tvSecond);
        TextView tvThird = findViewById(R.id.tvThird);
        TextView tvForth = findViewById(R.id.tvForth);
        TextView tvPassCode = findViewById(R.id.tvPassCode);

        if (prefs.getString(AppInfo.STRING_PIN, "").equals("")) {
            //New Pin
            if (i == 1002)
                resetAndLogout();
            if (i == 1001)
                deleteValue();
            else {
                if (firstPin.equals("")) {
                    if (tvFirst.getText().toString().trim().equals(""))
                        tvFirst.setText("" + i);
                    else if (tvSecond.getText().toString().trim().equals(""))
                        tvSecond.setText("" + i);
                    else if (tvThird.getText().toString().trim().equals(""))
                        tvThird.setText("" + i);
                    else if (tvForth.getText().toString().trim().equals("")) {
                        tvForth.setText("" + i);
                        tvPassCode.setText("Confirm your Pin");
                        firstPin = tvFirst.getText().toString() + tvSecond.getText().toString() + tvThird.getText().toString() + tvForth.getText().toString();
                        tvFirst.setText(""); tvSecond.setText(""); tvThird.setText(""); tvForth.setText("");
                    }
                } else {
                    if (tvFirst.getText().toString().trim().equals(""))
                        tvFirst.setText("" + i);
                    else if (tvSecond.getText().toString().trim().equals(""))
                        tvSecond.setText("" + i);
                    else if (tvThird.getText().toString().trim().equals(""))
                        tvThird.setText("" + i);
                    else if (tvForth.getText().toString().trim().equals("")) {
                        tvForth.setText("" + i);
                        String newPin = tvFirst.getText().toString() + tvSecond.getText().toString() + tvThird.getText().toString() + tvForth.getText().toString();
                        if (newPin.equals(firstPin)) {
                            editor = prefs.edit();
                            editor.putString(AppInfo.STRING_PIN, newPin);
                            editor.putLong(AppInfo.LONG_LOGIN_TIME, System.currentTimeMillis());
                            editor.apply();
                            finish();
                        }
                    }
                }
            }
        } else {
            //Match Pins
            matchPin(i);
        }
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
            resetAndLogout();
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
                String newPin = tvFirst.getText().toString() + tvSecond.getText().toString() + tvThird.getText().toString() + tvForth.getText().toString();
                if (prefs.getString(AppInfo.STRING_PIN, "").equals(newPin)) {

                    editor = prefs.edit();
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
        }
    }

    private void resetAndLogout() {
        editor = prefs.edit();
        editor.putString(AppInfo.STRING_PIN, "");
        editor.apply();

        FirebaseAuth.getInstance().signOut();
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(context, SignIn.class));
                    finish();
                }
            }
        });
    }

    public void resetPin(View view) {
        resetAndLogout();
    }

}
