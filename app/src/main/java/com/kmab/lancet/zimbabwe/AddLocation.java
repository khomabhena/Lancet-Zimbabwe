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

public class AddLocation extends AppCompatActivity {

    FloatingActionButton fabSave;
    EditText etBuildingName, etAddress, etCity;
    ProgressBar progressBar;

    Context context;
    XEmptyFields isFieldEmpty;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        isFieldEmpty = new XEmptyFields();
        key = FirebaseDatabase.getInstance().getReference().child(StaticVals.childLocations).push().getKey();
        fabSave = findViewById(R.id.fabSave);
        etBuildingName = findViewById(R.id.etBuildingName);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        progressBar = findViewById(R.id.progressBar);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save() {
        if (isFieldEmpty.isFieldEmpty(etBuildingName))
            showToast("Enter Building Name");
        else if (isFieldEmpty.isFieldEmpty(etAddress))
            showToast("Enter street address");
        else if (isFieldEmpty.isFieldEmpty(etCity))
            showToast("Enter city of town");
        else {
            String building = etBuildingName.getText().toString();
            String address = etAddress.getText().toString();
            String city = etCity.getText().toString();
            SetterLocation setterLocation = new SetterLocation(key, building, address, city);

            progressBar.setVisibility(View.VISIBLE);
            FirebaseDatabase.getInstance().getReference()
                    .child(StaticVals.childLocations)
                    .child(key)
                    .setValue(setterLocation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showToast("Saved");
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
