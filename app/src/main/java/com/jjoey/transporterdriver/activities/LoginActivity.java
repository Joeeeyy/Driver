package com.jjoey.transporterdriver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jjoey.transporterdriver.R;
import com.jjoey.transporterdriver.utils.Utils;

import static com.jjoey.transporterdriver.fragments.BaseFragment.mAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private Toolbar toolbar;
    private ImageView backIV;

    private TextView forgotPwdTV;
    private EditText emailET, passET;
    private Button loginBtn;

    private String email, pass;
    private boolean isValid = false;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initViews();
        setSupportActionBar(toolbar);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, AuthActivity.class));
            }
        });

        forgotPwdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetwork(LoginActivity.this)){
                    showPasswordDialog();
                } else {
                    Toast.makeText(LoginActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateViews()){
                    signIn();
                } else {
                    Toast.makeText(LoginActivity.this, "Use Proper Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateViews() {
        email = emailET.getText().toString();
        pass = passET.getText().toString();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass) ) {
            isValid = false;
            Log.d(TAG, "Fields Empty");
        } else {
            isValid = true;
            Log.d(TAG, "Fields Correct");
        }
        return isValid;
    }

    private void showPasswordDialog() {
        String resetMail = emailET.getText().toString();
        if (resetMail.isEmpty()){
            emailET.requestFocus();
            Snackbar.make(findViewById(android.R.id.content), "Enter Email from Input Field And Try Again", Snackbar.LENGTH_LONG).show();
        } else {
            if (Utils.isNetwork(this)){
                if (mAuth != null){
                    mAuth.sendPasswordResetEmail(resetMail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Snackbar.make(findViewById(android.R.id.content), "Check Email for Instructions", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        Snackbar.make(findViewById(android.R.id.content), "Try Again..Process Failed", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void signIn() {
        if (Utils.isNetwork(LoginActivity.this)){
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, DriverHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d(TAG, "Login Failed:\t" + task.getException().getMessage());
                            }
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
        }
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
