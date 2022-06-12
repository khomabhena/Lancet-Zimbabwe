package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class XLogin {

    private Context context;
    private String email, password;
    private ProgressBar progressBar;

    public XLogin(Context context, String email, String password, ProgressBar progressBar) {
        this.context = context;
        this.email = email;
        this.password = password;
        this.progressBar = progressBar;
    }

    public void login() {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        cancelProgressBar(false);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                Toast.makeText(context, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Authentication failed, check your email and password or sign up", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                        cancelProgressBar(true);
                    }
                });
    }

    public void reset() {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "Enter your registered email address!", Toast.LENGTH_LONG).show();
            return;
        }

        cancelProgressBar(false);
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "We have sent you instructions to reset your password",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Failed to send reset email", Toast.LENGTH_LONG).show();
                        }
                        cancelProgressBar(true);
                    }
                });
    }

    public void signUp() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(context, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //create user
        cancelProgressBar(false);
        auth.signInAnonymously();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Sign up failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            context.startActivity(new Intent(context, MainActivity.class));
                        }
                        cancelProgressBar(true);
                    }
                });
    }

    private void cancelProgressBar(boolean isTrue) {
        if (isTrue)
            progressBar.setVisibility(View.GONE);
        else
            progressBar.setVisibility(View.VISIBLE);
    }
}
