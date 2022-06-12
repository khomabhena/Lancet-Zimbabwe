package com.kmab.lancet.zimbabwe;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DisplayImage extends AppCompatActivity {

    ImageView ivProfile;
    Context context;

    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        link = getIntent().getExtras().getString("link");

        ivProfile = findViewById(R.id.ivProfile);

        insertBroadcastImage();
    }

    private void insertBroadcastImage() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        circularProgressDrawable.setColorSchemeColors(R.color.white, R.color.colorAccent);
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(link);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .placeholder(circularProgressDrawable)
                    .into(ivProfile);
        } catch (Exception e) {
            //
        }
    }

}
