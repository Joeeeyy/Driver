package com.jjoey.transporterdriver.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jjoey.transporterdriver.activities.LoginActivity;
import com.jjoey.transporterdriver.utils.AppConstants;

public class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    public static FirebaseAuth mAuth;
    public static DatabaseReference driversRef, usersRef;
    public static StorageReference sRef, imagesRef, vehiclesRef;
    public static GeoFire geoFire;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        usersRef = FirebaseDatabase.getInstance().getReference(AppConstants.USERS_REF);
        usersRef.keepSynced(true);

        driversRef = FirebaseDatabase.getInstance().getReference(AppConstants.DRIVERS_REF);
        driversRef.keepSynced(true);

        geoFire = new GeoFire(driversRef);

        sRef = FirebaseStorage.getInstance().getReference(AppConstants.STORAGE_REF);
        imagesRef = FirebaseStorage.getInstance().getReference(AppConstants.PROFILE_IMAGES);
        vehiclesRef = FirebaseStorage.getInstance().getReference(AppConstants.VEHICLE_IMAGE);

    }

    public static FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public static String getUid() {
        return mAuth.getCurrentUser().getUid();
    }

    public static void signOut(Context context) {
        if (mAuth != null) {
            mAuth.signOut();
            mAuth = null;
        }
        Intent signOutIntent = new Intent(context, LoginActivity.class);
        signOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(signOutIntent);
    }

}
