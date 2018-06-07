package com.jjoey.transporterdriver.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jjoey.transporterdriver.R;

public class RegisterActivity extends Activity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private ImageView facebookLoginIV, twitterLoginIV, googleLoginIV;
    private EditText et_fullname, et_email, et_pass, et_confirm_pass;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

    }

    private void initViews() {
        facebookLoginIV = findViewById(R.id.facebookLoginIV);
        twitterLoginIV = findViewById(R.id.twitterLoginIV);
        googleLoginIV = findViewById(R.id.googleLoginIV);
        et_fullname = findViewById(R.id.et_fullname);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_confirm_pass = findViewById(R.id.et_confirm_pass);
        loginBtn = findViewById(R.id.loginBtn);
    }

}
