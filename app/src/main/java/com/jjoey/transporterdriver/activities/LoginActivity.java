package com.jjoey.transporterdriver.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoey.transporterdriver.R;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView backIV;
    private TextView forgotPwdTV;
    private EditText emailET, passET;
    private Button loginBtn;

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
        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);
        loginBtn = findViewById(R.id.loginBtn);
        forgotPwdTV = findViewById(R.id.forgotPwdTV);
    }
}
