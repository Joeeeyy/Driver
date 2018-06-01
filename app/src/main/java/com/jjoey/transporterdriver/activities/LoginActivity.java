package com.jjoey.transporterdriver.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jjoey.transporterdriver.R;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView backIV;
    private TextInputEditText emailTET, pwdTET;
    private Button loginBtn, forgotPwdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, AuthActivity.class));
            }
        });

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        backIV = findViewById(R.id.backIV);
        emailTET = findViewById(R.id.emailTET);
        pwdTET = findViewById(R.id.pwdTET);
        loginBtn = findViewById(R.id.loginBtn);
        forgotPwdBtn = findViewById(R.id.forgotPwdBtn);
    }
}
