package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {

    Context context;
    EditText etEmail, etPassword;
    ImageView ivViewPass;
    CardView cardSign;
    TextView tvSign;
    Button bReset, bLogin;
    ProgressBar progressBar;

    boolean isLogin = true, passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        ivViewPass = findViewById(R.id.ivViewPass);
        tvSign = findViewById(R.id.tvSign);
        bReset = findViewById(R.id.bReset);
        bLogin = findViewById(R.id.bLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    public void switchLog(View view) {
        if (isLogin) {
            isLogin = false;
            tvSign.setText("SIGN UP");
            bLogin.setText("LOGIN");
            //sign
        } else {
            isLogin = true;
            tvSign.setText("LOGIN");
            bLogin.setText("SIGN UP");
            //log
        }
    }

    public void logIn(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        if (isLogin) {
            new XLogin(context, email, password, progressBar).login();
        } else {
            new XLogin(context, email, password, progressBar).signUp();
        }
    }

    public void visiblePass(View view) {
        if (passwordVisible) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordVisible = false;
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordVisible = true;
        }
    }

    public void reset(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        new XLogin(context, email, password, progressBar).reset();
    }

}
