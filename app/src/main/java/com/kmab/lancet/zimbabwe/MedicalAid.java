package com.kmab.lancet.zimbabwe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MedicalAid extends AppCompatActivity {


    CameraPhoto cameraPhoto;
    final int CAMERA_REQUEST = 123;
    GalleryPhoto galleryPhoto;
    final int GALLERY_REQUEST = 27277;
    private static final int REQUEST_WRITE_IMAGE = 1994;
    private static String MEDICAL_LINK = "";

    Context context;
    CardView cardMedical;
    ImageView ivImage;
    EditText etOther;
    ProgressBar progressBar;
    String specimenType = "";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    CheckBox checkBlood, checkUrine, checkSwab, checkFluid, checkStool, checkOther;


    String imageFilePath;
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_aid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        prefs = getSharedPreferences(AppInfo.USER_INFO, Context.MODE_PRIVATE);
        galleryPhoto = new GalleryPhoto(context);
        cameraPhoto = new CameraPhoto(context);
        cardMedical = findViewById(R.id.cardMedical);
        ivImage = findViewById(R.id.ivImage);
        etOther = findViewById(R.id.etOther);

        checkBlood = findViewById(R.id.checkBlood);
        checkUrine = findViewById(R.id.checkUrine);
        checkSwab = findViewById(R.id.checkSwab);
        checkFluid = findViewById(R.id.checkFluid);
        checkStool = findViewById(R.id.checkStool);
        checkOther = findViewById(R.id.checkOther);
        progressBar = findViewById(R.id.progressBar);

        insertBroadcastImage();
    }

    private void insertBroadcastImage() {
        if (!prefs.getString(AppInfo.MEDICAL_LINK, "").equals("")) {
            MEDICAL_LINK = prefs.getString(AppInfo.MEDICAL_LINK, "");

            try {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(MEDICAL_LINK);
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .crossFade()
                        .into(ivImage);
            } catch (Exception e) {
                //
            }
        }
    }

    public void nextStep(View view) {
        if (prefs.getString(AppInfo.MEDICAL_LINK, "").equals(""))
            Toast.makeText(context, "Upload your medical aid photo", Toast.LENGTH_SHORT).show();
        else {
            SetterForm setterForm = (SetterForm) SendForm.list.get(0);

            setterForm.setMedicalLink(prefs.getString(AppInfo.MEDICAL_LINK, ""));
            startActivity(new Intent(context, Form.class));
        }
    }

    public void checkItem(View view) {
        int id = view.getId();
        if (view.getId() == R.id.checkOther) {
            if (checkOther.isChecked()) {
                specimenType = "";
                doNotDisableThis(checkOther);
            } else
                specimenType = "";
        }
        else if (id == R.id.checkBlood) {
            if (checkBlood.isChecked()) {
                specimenType = checkBlood.getText().toString();
                doNotDisableThis(checkBlood);
            } else
                specimenType = "";
        }
        else if (id == R.id.checkUrine) {
            if (checkUrine.isChecked()) {
                specimenType = checkUrine.getText().toString();
                doNotDisableThis(checkUrine);
            } else
                specimenType = "";
        }
        else if (id == R.id.checkSwab) {
            if (checkSwab.isChecked()) {
                specimenType = checkSwab.getText().toString();
                doNotDisableThis(checkSwab);
            } else
                specimenType = "";
        }
        else if (id == R.id.checkFluid) {
            if (checkFluid.isChecked()) {
                specimenType = checkFluid.getText().toString();
                doNotDisableThis(checkFluid);
            } else
                specimenType = "";
        }
        else if (id == R.id.checkStool) {
            if (checkStool.isChecked()) {
                specimenType = checkStool.getText().toString();
                doNotDisableThis(checkStool);
            } else
                specimenType = "";
        }
    }

    private void doNotDisableThis(CheckBox checked) {
        CheckBox[] checkBoxes = {checkBlood, checkUrine, checkSwab, checkFluid, checkStool, checkOther};

        for (CheckBox checkBox : checkBoxes) {
            if (checkBox != checked)
                checkBox.setChecked(false);
        }
    }













    public void getImageFromStorage(View view) {
        openCameraIntent();
    }

    private void getImageFromCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

            if (!hasPermission) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
            } else {
                launchCamera();
            }
        } else {
            launchCamera();
        }
    }

    private void launchCamera() {
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
            //cameraPhoto.addToGallery();
        } catch (IOException e) {
            Toast.makeText(context, "Something went wrong while taking a photo", Toast.LENGTH_SHORT).show();
        }
    }




    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(context, ""  + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", photoFile);

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        } else
            getImageFromStorage();
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }





    private void getImageFromStorage() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_WRITE_IMAGE);
            } else {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        } else {
            startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            handlePosterResult(data);
        }
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            cameraPhoto.getPhotoPath();

            String photoPath = cameraPhoto.getPhotoPath();
            Bitmap bitmap = null;
            try {
                bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                //ivImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            handlePosterResult(photoPath);
        }
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {

            handlePosterResult(imageFilePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features that required the permission
                    startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                } else {
                    Toast.makeText(context, "Allow access to your storage.", Toast.LENGTH_LONG).show();
                }
                break;
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features that required the permission
                    launchCamera();
                } else {
                    Toast.makeText(context, "Allow access to your camera.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void handlePosterResult(Intent data) {
        Uri uri = data.getData();
        progressBar.setVisibility(View.VISIBLE);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child("meal-pics/"+ uri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(context, "Image UploadFailed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-accType, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        MEDICAL_LINK = uri.toString();

                        editor = prefs.edit();
                        editor.putString(AppInfo.MEDICAL_LINK, MEDICAL_LINK);
                        editor.apply();

                        try {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString());
                            Glide.with(context)
                                    .using(new FirebaseImageLoader())
                                    .load(storageReference)
                                    .crossFade()
                                    .into(ivImage);
                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            //
                        }
                    }
                });
            }
        });

    }

    private void handlePosterResult(String photoPath) {
        Uri uri = Uri.fromFile(new File(photoPath));
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(context, "Processing Image", Toast.LENGTH_SHORT).show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child("medical-aids/"+ uri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(context, "Image UploadFailed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-accType, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        MEDICAL_LINK = uri.toString();

                        editor = prefs.edit();
                        editor.putString(AppInfo.MEDICAL_LINK, MEDICAL_LINK);
                        editor.apply();

                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
                        circularProgressDrawable.setStrokeWidth(5f);
                        circularProgressDrawable.setCenterRadius(30f);
                        circularProgressDrawable.start();
                        try {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString());
                            Glide.with(context)
                                    .using(new FirebaseImageLoader())
                                    .load(storageReference)
                                    .placeholder(circularProgressDrawable)
                                    .into(ivImage);
                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            //
                        }
                    }
                });
            }
        });
    }

}
