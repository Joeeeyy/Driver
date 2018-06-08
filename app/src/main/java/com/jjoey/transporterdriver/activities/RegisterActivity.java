package com.jjoey.transporterdriver.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoey.transporterdriver.R;
import com.jjoey.transporterdriver.models.DriverModel;
import com.jjoey.transporterdriver.utils.AppConstants;
import com.jjoey.transporterdriver.utils.SharedPrefsHelper;
import com.jjoey.transporterdriver.utils.Utils;

import java.util.HashMap;

public class RegisterActivity extends Activity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private ImageView facebookLoginIV, twitterLoginIV, googleLoginIV;
    private EditText et_fullname, et_email, et_pass, et_confirm_pass;
    private Button registerBtn;

    private boolean isValid = false;
    private String name, email, pwd, confirm_pwd, phone;

    private FirebaseAuth mAuth;
    private DatabaseReference driversRef;

    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        driversRef = FirebaseDatabase.getInstance().getReference(AppConstants.DRIVERS_REF);
        driversRef.keepSynced(true);

        prefsHelper = new SharedPrefsHelper(this);

        initViews();
        phone = getIntent().getStringExtra("phone_num");
        et_fullname.requestFocus();
        Log.d(TAG, "Phone:\t" + phone);

        facebookLoginIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(android.R.id.content), "Facebook Login Soon..", Snackbar.LENGTH_LONG).show();
            }
        });

        twitterLoginIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(android.R.id.content), "Twitter Login Soon..", Snackbar.LENGTH_LONG).show();
            }
        });

        googleLoginIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(android.R.id.content), "GooglePlus Login Soon..", Snackbar.LENGTH_LONG).show();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateViews()) {
                    if (pwd.length() < 6 || confirm_pwd.length() < 6) {
                        Toast.makeText(RegisterActivity.this, "Password Length Must be 6 or More ", Toast.LENGTH_SHORT).show();
                        if (!confirm_pwd.equals(pwd)) {
                            Snackbar.make(findViewById(android.R.id.content), "Passwords Don't Match", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        if (Utils.isNetwork(RegisterActivity.this)) {
                            createUser();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enter all Fields", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void createUser() {

        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            prefsHelper.setHasAccount(true);

                            DriverModel driver = new DriverModel();
                            driver.setEmailAddr(email);
                            driver.setFullName(name);
                            driver.setPhoneNumber(phone);

                            Log.d(TAG, "UID:\t" + mAuth.getCurrentUser().getUid());

                            HashMap<String, String> detailsMap = new HashMap<>();
                            detailsMap.put("userId", mAuth.getCurrentUser().getUid());
                            detailsMap.put("emailAddr", driver.getEmailAddr());
                            detailsMap.put("fullName", driver.getFullName());
                            detailsMap.put("phoneNumber", driver.getPhoneNumber());

                            driversRef.child(mAuth.getCurrentUser().getUid()).setValue(detailsMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Driver Saved");
                                                Intent intent = new Intent(RegisterActivity.this, DriverProfileActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.d(TAG, "Driver NOT Saved");
                                                Log.d(TAG, task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Failed:\t" + task.getException().getMessage());
                        }
                    }
                });
    }

    private boolean validateViews() {
        name = et_fullname.getText().toString();
        email = et_email.getText().toString();
        pwd = et_pass.getText().toString();
        confirm_pwd = et_confirm_pass.getText().toString();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(confirm_pwd)) {
            isValid = false;
            Log.d(TAG, "Fields Empty");
        } else {
            isValid = true;
            Log.d(TAG, "Fields Correct");
        }
        return isValid;
    }

    private void initViews() {
        facebookLoginIV = findViewById(R.id.facebookLoginIV);
        twitterLoginIV = findViewById(R.id.twitterLoginIV);
        googleLoginIV = findViewById(R.id.googleLoginIV);
        et_fullname = findViewById(R.id.et_fullname);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_confirm_pass = findViewById(R.id.et_confirm_pass);
        registerBtn = findViewById(R.id.loginBtn);
    }

}
