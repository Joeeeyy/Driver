package com.jjoey.transporterdriver.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.jjoey.transporterdriver.R;
import com.jjoey.transporterdriver.utils.AppConstants;

public class LandingActivity extends AppCompatActivity {

    private ProgressBar landing_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        landing_progress = findViewById(R.id.landing_progress);
        landing_progress.setIndeterminate(true);

        if (Build.VERSION.SDK_INT >= 23) {
            checkPerms();
        } else {
            startActivity(new Intent(LandingActivity.this, AuthActivity.class));
        }

    }

    @TargetApi(23)
    private void checkPerms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.LOC_PERM_CODE);
        } else {
            landing_progress.setVisibility(View.GONE);
            startActivity(new Intent(LandingActivity.this, AuthActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.LOC_PERM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    landing_progress.setVisibility(View.GONE);
                    startActivity(new Intent(LandingActivity.this, AuthActivity.class));
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.LOC_PERM_CODE);
                }
                break;
        }
    }

}
