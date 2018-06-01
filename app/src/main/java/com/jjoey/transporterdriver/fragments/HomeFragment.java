package com.jjoey.transporterdriver.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjoey.transporterdriver.R;
import com.jjoey.transporterdriver.utils.AppConstants;

public class HomeFragment extends BaseFragment implements OnMapReadyCallback {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private Toolbar toolbar;
    private TextView driverStatusTV;
    private RelativeLayout inputsLayout;

    public static MaterialAnimatedSwitch statusSwitch;

    private FusedLocationProviderClient providerClient;
    public static Location mLastLocation;
    public static LocationRequest locationRequest;
    public GoogleMap mGmap;

    public static Marker currentMarker;
    public static double latitude = 0f, longitude = 0f;
    private static boolean isLocationGranted = false;

    public static final int UPDATE_INTERVAL = 15000;
    public static final int FASTEST_INTERVAL = 8000;
    public static final int DISPLACEMENT = 10;

    public static final int PLAY_SERVICES_REQ_CODE = 9009;
    public static final int PLAY_SERVICES_RESOLUTION_REQ_CODE = 9090;

    private SupportMapFragment mapFragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    private void initViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        driverStatusTV = view.findViewById(R.id.driverStatusTV);
        inputsLayout = view.findViewById(R.id.inputsLayout);
        statusSwitch = view.findViewById(R.id.statusSwitch);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.nav_icon);

        checkPerms();
        providerClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        driverStatusTV.setText("OFFLINE");
        statusSwitch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean b) {
                if (b) {
                    inputsLayout.setVisibility(View.VISIBLE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "You are Now Online", Snackbar.LENGTH_LONG).show();
                    if (checkPerms()) {
                        startLocationListener();
                        driverStatusTV.setText("ONLINE");
                    }
                } else {
                    inputsLayout.setVisibility(View.GONE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "You are Now Offline", Snackbar.LENGTH_LONG).show();
                    mGmap.setIndoorEnabled(false);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mGmap.setMyLocationEnabled(false);
                    stopLocationListener();
                    driverStatusTV.setText("OFFLINE");
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                }
            }
        });

        return view;
    }

    private void stopLocationListener() {
        if (providerClient != null) {
            providerClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationListener();
    }

    private void startLocationListener() {
        locationRequest = LocationRequest.create();
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        LocationSettingsRequest settingsRequest = builder.build();

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        client.checkLocationSettings(settingsRequest);

        displayLocation();

    }

    private boolean checkPerms() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            reqPerms();
            isLocationGranted = false;
            Log.d(TAG, "Permission Value:\t" + isLocationGranted);
        } else {
            isLocationGranted = true;
            Log.d(TAG, "Permission Value:\t" + isLocationGranted);
        }
        return isLocationGranted;
    }

    private void reqPerms() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.LOC_PERM_CODE);
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            reqPerms();
        } else {
            if (statusSwitch.isChecked()) {
                inputsLayout.setVisibility(View.VISIBLE);
                providerClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                mGmap.setIndoorEnabled(true);
                mGmap.setMyLocationEnabled(true);
            } else {
                inputsLayout.setVisibility(View.GONE);
                mGmap.setIndoorEnabled(false);
                mGmap.setMyLocationEnabled(false);
            }
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            Location location = locationResult.getLastLocation();
            mLastLocation = location;
            if (currentMarker != null) {
                currentMarker.remove();
            }
            latitude = mLastLocation.getLatitude();
            Log.d(TAG, "Lat:\t" + latitude);
            longitude = mLastLocation.getLongitude();
            Log.d(TAG, "Long:\t" + longitude);

            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(latitude, longitude));
            options.title("Driver");
            //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)); // throws error

            currentMarker = mGmap.addMarker(options);
            rotateMarker(currentMarker, 360, mGmap);
            mGmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18.0f));

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.LOC_PERM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationGranted = true;
                    if (checkPlayServices()) {
                        if (statusSwitch.isChecked()) {
                            inputsLayout.setVisibility(View.VISIBLE);
                            startLocationListener();
                        } else {
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            inputsLayout.setVisibility(View.GONE);
                            mGmap.setIndoorEnabled(false);
                            mGmap.setMyLocationEnabled(false);
                        }

                    } else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Google Play Services Not Supported on Your Device", Snackbar.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), AppConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Play Services NOT Supported on Your Device", Snackbar.LENGTH_LONG).show();
                getActivity().finish();
                getActivity().moveTaskToBack(true);
            }
            return false;
        }
        return true;
    }

    private void rotateMarker(final Marker currentMarker, final float i, GoogleMap mGmap) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = currentMarker.getRotation();
        final int duration = 1500;

        final Interpolator interpolator = new LinearInterpolator();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.elapsedRealtime() - start;
                float t = interpolator.getInterpolation(elapsed / duration);
                float rot = t * i + (1 - t) * startRotation;
                currentMarker.setRotation(-rot > 180 ? rot / 2 : rot);

                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }

            }
        }, duration);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGmap = googleMap;

        View mapBtn = (View) ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapBtn.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.setMargins(0, 0, 30, 30);

        startLocationListener();
    }

}
