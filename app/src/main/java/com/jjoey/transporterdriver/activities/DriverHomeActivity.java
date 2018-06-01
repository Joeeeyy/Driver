package com.jjoey.transporterdriver.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.jjoey.transporterdriver.R;
import com.jjoey.transporterdriver.fragments.AccountFragment;
import com.jjoey.transporterdriver.fragments.EarningsFragment;
import com.jjoey.transporterdriver.fragments.HomeFragment;
import com.jjoey.transporterdriver.fragments.RatingsFragment;
import com.jjoey.transporterdriver.utils.AppConstants;

public class DriverHomeActivity extends AppCompatActivity {

    private static final String TAG = DriverHomeActivity.class.getSimpleName();

    private boolean isLocationGranted = false;
    private BottomNavigationView bottomTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        initViews();
        checkPerms();

        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.fragContainer, new HomeFragment()).commit();

        bottomTabs.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = null;
                switch (item.getItemId()) {
                    case R.id.tabs_home:
                        ft = getSupportFragmentManager()
                                .beginTransaction();
                        ft.replace(R.id.fragContainer, new HomeFragment()).commit();
                        break;
                    case R.id.tabs_ratings:
                        ft = getSupportFragmentManager()
                                .beginTransaction();
                        ft.replace(R.id.fragContainer, new RatingsFragment()).commit();
                        break;
                    case R.id.tabs_earnings:
                        ft = getSupportFragmentManager()
                                .beginTransaction();
                        ft.replace(R.id.fragContainer, new EarningsFragment()).commit();
                        break;
                    case R.id.tabs_account:
                        ft = getSupportFragmentManager()
                                .beginTransaction();
                        ft.replace(R.id.fragContainer, new AccountFragment()).commit();
                        break;
                }
                return true;
            }
        });

    }

    private void initViews() {
        bottomTabs = findViewById(R.id.bottomTabs);
    }

    private boolean checkPerms() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isLocationGranted = true;
            Log.d(TAG, "Permission Value:\t" + isLocationGranted);
        } else {
            reqPerms();
            isLocationGranted = false;
            Log.d(TAG, "Permission Value:\t" + isLocationGranted);
        }
        return isLocationGranted;
    }

    private void reqPerms() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.LOC_PERM_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.LOC_PERM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationGranted = true;
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Google Play Services Not Supported on Your Device", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

}
