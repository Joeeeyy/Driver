package com.jjoey.transporterdriver.activities;

import android.app.Activity;
import android.os.Bundle;

import com.jjoey.transporterdriver.R;

public class RegisterActivity extends Activity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
