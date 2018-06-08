package com.jjoey.transporterdriver.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjoey.transporterdriver.R;
import com.jjoey.transporterdriver.models.DriverModel;
import com.jjoey.transporterdriver.utils.AppConstants;
import com.jjoey.transporterdriver.utils.Utils;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DriverProfileActivity.class.getSimpleName();

    private CircleImageView driverProfileCIV;
    private EditText nameET, emailET, calendarET, emergencyContactNameET, emergencyContactPhoneETT;
    private LinearLayout nextLayout;

    private FirebaseAuth mAuth;
    private DatabaseReference driversRef;
    private StorageReference sRef, imageRef;
    private UploadTask uploadTask;

    private String name, email, dob, downloadURL = null;
    private boolean isValid = false;

    private static final int REQ_CODE = 201;
    private static final int CAM_CODE = 301;
    private static final int GAL_CODE = 302;

    private boolean isCameraImage = false, isGalleryImage = false;
    private Bitmap cameraBitmap = null;
    private Uri galleryUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_driver_profile);

        initViews();

        mAuth = FirebaseAuth.getInstance();
        driversRef = FirebaseDatabase.getInstance().getReference(AppConstants.DRIVERS_REF);
        driversRef.keepSynced(true);
        sRef = FirebaseStorage.getInstance().getReference();

        getDriverDetails();

        driverProfileCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPerms();
                } else {
                    showChooserDialog();
                }
            }
        });

        calendarET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarDialog();
            }
        });

        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    updateProfile();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enter all BioData", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getDriverDetails() {
        driversRef.child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DriverModel model = dataSnapshot.getValue(DriverModel.class);
                        Log.d(TAG, "Driver email:\t" + model.getEmailAddr());
                        Log.d(TAG, "Driver name:\t" + model.getFullName());

                        emailET.setText(model.getEmailAddr());
                        nameET.setText(model.getFullName());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void updateProfile() {

        Map<String, Object> dobMap = new HashMap<>();
        dobMap.put("dob", dob);

        if (Utils.isNetwork(this)){
            driversRef.child(mAuth.getCurrentUser().getUid())
                    .updateChildren(dobMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "Update Successful");
                                startActivity(new Intent(DriverProfileActivity.this, DriverHomeActivity.class));
                                finish();
                            } else {
                                Log.d(TAG, "Update Failed");
                            }
                        }
                    });
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Operation Failed...Check Network", Snackbar.LENGTH_LONG).show();
        }

    }

    @TargetApi(23)
    private void checkPerms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_CODE);
        } else {
            showChooserDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    showChooserDialog();
                } else {
                    Snackbar sk = Snackbar.make(findViewById(android.R.id.content), "Some permissions are Required. Go Into Settings and GRANT these permission", Snackbar.LENGTH_LONG);
                    sk.setAction("GRANT", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent settingsIntent = new Intent();
                            Uri uri = Uri.fromParts("package", getPackageName(), "ProfileSettingsActivity");
                            settingsIntent.setData(uri);
                            startActivityForResult(settingsIntent, REQ_CODE);
                        }
                    });
                    sk.show();
                }
                break;
        }
    }

    private void showChooserDialog() {
        final CharSequence[] items = {"Open From Camera", "Select from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Open From Camera")) {
                    Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camIntent, CAM_CODE);
                    isCameraImage = true;
                    isGalleryImage = false;
                } else if (items[i].equals("Select from Gallery")) {
                    Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    pickIntent.setType("image/*");
                    startActivityForResult(pickIntent, GAL_CODE);
                    isCameraImage = false;
                    isGalleryImage = true;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAM_CODE:
                cameraBitmap = (Bitmap) data.getExtras().get("data");
                driverProfileCIV.setImageBitmap(cameraBitmap);
                uploadImage(cameraBitmap, galleryUri);
                break;
            case GAL_CODE:
                galleryUri = data.getData();
                driverProfileCIV.setImageURI(galleryUri);
                uploadImage(cameraBitmap, galleryUri);
                break;
        }
    }

    private void uploadImage(Bitmap cameraBitmap, Uri galleryUri) {
        if (isCameraImage) {
            byte[] imageBytes = Utils.bitmapToByteArray(cameraBitmap);

            imageRef = sRef.child(AppConstants.DRIVER).child(AppConstants.DRIVER_PROFILE_IMAGES).child(mAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = imageRef.putBytes(imageBytes);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded Successfully", Snackbar.LENGTH_LONG).show();
                        downloadURL = task.getResult().getDownloadUrl().toString();
                        isValid = true;
                        driversRef.child(mAuth.getCurrentUser().getUid())
                                .child("imgURL")
                                .setValue(downloadURL)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Image Saved in DB");
                                        } else {
                                            Log.d(TAG, "Image NOT Saved in DB");
                                        }
                                    }
                                });
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Image Upload Failed...Re-Select Image", Snackbar.LENGTH_LONG).show();
                        isValid = false;
                    }
                }
            });

        } else if (isGalleryImage) {
            Bitmap bitmap = null;
            byte[] bytes = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), galleryUri);
                bytes = Utils.bitmapToByteArray(bitmap);

                imageRef = sRef.child(AppConstants.DRIVER).child(AppConstants.DRIVER_PROFILE_IMAGES).child(mAuth.getCurrentUser().getUid() + ".jpg");
                uploadTask = imageRef.putBytes(bytes);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(findViewById(android.R.id.content), "Image Uploaded Successfully", Snackbar.LENGTH_LONG).show();
                            downloadURL = task.getResult().getDownloadUrl().toString();
                            isValid = true;
                            driversRef.child(mAuth.getCurrentUser().getUid())
                                    .child("imgURL")
                                    .setValue(downloadURL)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Image Saved in DB");
                                            } else {
                                                Log.d(TAG, "Image NOT Saved in DB");
                                            }
                                        }
                                    });
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Image Upload Failed...Re-Select Image", Snackbar.LENGTH_LONG).show();
                            isValid = false;
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean validate() {
        dob = calendarET.getText().toString();
        if (dob.isEmpty() && downloadURL.isEmpty()){
            if (downloadURL.isEmpty()){
                Toast.makeText(this, "Add Profile Image", Toast.LENGTH_SHORT).show();
            }
            isValid = false;
        } else if (!dob.isEmpty() && downloadURL.isEmpty()){
            isValid = true;
        }
        return isValid;
    }

    private void showCalendarDialog() {

        Calendar calendar = Calendar.getInstance();
//        String year = calendar.get

        new SpinnerDatePickerDialogBuilder()
                .context(this)
                .callback(this)
                .spinnerTheme(R.style.DatePickerSpinner)
                .showTitle(true)
                .showDaySpinner(true)
                .defaultDate(2000, 01, 01)
                .maxDate(2045, 12, 31)
                .build()
                .show();
    }

    private void initViews() {
        driverProfileCIV = findViewById(R.id.driverProfileCIV);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        calendarET = findViewById(R.id.calendarET);
        emergencyContactNameET = findViewById(R.id.emergencyContactNameET);
        emergencyContactPhoneETT = findViewById(R.id.emergencyContactPhoneET);
        nextLayout = findViewById(R.id.nextLayout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN) {
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d(TAG, "Sel Year:\t" + year);
        Log.d(TAG, "Sel Month:\t" + monthOfYear);
        Log.d(TAG, "Sel Day:\t" + dayOfMonth);
    }
}
