package com.jjoey.transporterdriver.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jjoey.transporterdriver.R;
import com.jjoey.transporterdriver.fragments.AccountFragment;
import com.jjoey.transporterdriver.fragments.EarningsFragment;
import com.jjoey.transporterdriver.fragments.HomeFragment;
import com.jjoey.transporterdriver.fragments.RatingsFragment;

public class DriverHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        initViews();

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

}
