package com.jjoey.transporterdriver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.jjoey.transporterdriver.R;
import com.jjoey.transporterdriver.utils.FirebaseUtils;
import com.jjoey.transporterdriver.utils.SharedPrefsHelper;

public class SplashActivity extends FirebaseUtils {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private SharedPrefsHelper prefsHelper;

    private boolean hasAccount = false, loggedOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefsHelper = new SharedPrefsHelper(this);

        hasAccount = prefsHelper.getHasAccount();
        Log.d(TAG, "Driver hasAccount Value:\t" + hasAccount);

        loggedOut = prefsHelper.getLoggedOut();
        Log.d(TAG, "Driver Logged Out Value:\t" + loggedOut);

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkHasAccount();
                    }
                }, 5000);

    }

    private void checkLoginStatus() {
        if (loggedOut == true) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, DriverHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void checkHasAccount() {
        if (hasAccount == false) {
            Intent intent = new Intent(SplashActivity.this, LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            checkLoginStatus();
        }
    }

    private void startNextActivity() {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(this, Class.class);
                    }
                }, 5000);
    }

    private void startActivity(Runnable runnable, Class<Class> aClass) {

    }
}
