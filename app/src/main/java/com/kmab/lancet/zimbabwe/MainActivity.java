package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Context context;
    CardView cardAdmin, cardMessages, cardForm, cardPin, cardHistory, cardSearch;
    ImageView ivMessages, ivTimeline;
    TextView tvMessages, tvTimeline, tvAdmin;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    long loginTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        loginTime = System.currentTimeMillis() - prefs.getLong(AppInfo.LONG_LOGIN_TIME, 0);

        cardAdmin = findViewById(R.id.cardAdmin);
        cardMessages = findViewById(R.id.cardMessages);
        cardHistory = findViewById(R.id.cardHistory);
        cardSearch = findViewById(R.id.cardSearch);
        ivMessages = findViewById(R.id.ivMessages);
        ivTimeline = findViewById(R.id.ivTimeline);
        tvMessages = findViewById(R.id.tvMessages);
        tvTimeline = findViewById(R.id.tvTimeline);
        tvAdmin = findViewById(R.id.tvAdmin);
        cardForm = findViewById(R.id.cardForm);
        cardPin = findViewById(R.id.cardPin);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(context, SignIn.class));
            finish();
            return;
        }

        if (!prefs.getString(AppInfo.STRING_PIN, "").equals("") || prefs.getBoolean(AppInfo.IS_ADMIN, false))
            if (loginTime > (60000 * 3)) {
                if (prefs.getBoolean(AppInfo.IS_ADMIN, false))
                    startActivity(new Intent(MainActivity.this, Admin.class));
                else
                    startActivity(new Intent(MainActivity.this, Pin.class));

                finish();
                return;
            }

        if (prefs.getBoolean(AppInfo.IS_ADMIN, false)) {
            tvTimeline.setText("Submissions");
            tvAdmin.setVisibility(View.VISIBLE);
            cardForm.setVisibility(View.GONE);
            cardMessages.setVisibility(View.INVISIBLE);
            cardPin.setVisibility(View.INVISIBLE);
            cardHistory.setVisibility(View.VISIBLE);
            cardSearch.setVisibility(View.VISIBLE);
        } else {
            tvTimeline.setText("Timeline");
            tvAdmin.setVisibility(View.GONE);
            cardForm.setVisibility(View.VISIBLE);
            cardMessages.setVisibility(View.VISIBLE);
            cardPin.setVisibility(View.VISIBLE);
            cardHistory.setVisibility(View.INVISIBLE);
            cardSearch.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchActivity(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.cardPin:
                startActivity(new Intent(context, Pin.class));
                break;
            case R.id.cardLogout:
                editor = prefs.edit();
                editor.putBoolean(AppInfo.IS_ADMIN, false);
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
                break;
            case R.id.cardMessages:
                startActivity(new Intent(context, Messages.class));
                break;
            case R.id.cardForm:
                startActivity(new Intent(context, SendForm.class));
                break;
            case R.id.cardAdmin:
                startActivity(new Intent(context, Admin.class));
                break;
            case R.id.cardTimeline:
                if (prefs.getBoolean(AppInfo.IS_ADMIN, false))
                    startActivity(new Intent(context, Submissions.class));
                else
                    startActivity(new Intent(context, Timeline.class));
                break;
            case R.id.cardLocations:
                startActivity(new Intent(context, Locations.class));
                break;
            case R.id.cardHistory:
                startActivity(new Intent(context, History.class));
                break;
            case R.id.cardSearch:
                startActivity(new Intent(context, Search.class));
                break;
        }
    }
}
