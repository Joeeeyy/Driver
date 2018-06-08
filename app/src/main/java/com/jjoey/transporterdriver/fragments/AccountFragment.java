package com.jjoey.transporterdriver.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jjoey.transporterdriver.R;
import com.jjoey.transporterdriver.activities.EditProfileActivity;
import com.jjoey.transporterdriver.activities.EditVehicleActivity;
import com.jjoey.transporterdriver.models.DriverModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends BaseFragment {

    private static final String TAG = AccountFragment.class.getSimpleName();

    private CircleImageView profileCIV, carImg;
    private TextView profileNameTV, editProfileTV, carNameTV, changeCarTV;

    private ExpandableLayout driverProfileEL;

    public AccountFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View av = inflater.inflate(R.layout.fragment_account, container, false);
        initViews(av);

        getDriverDetails(); // TODO: 6/9/2018 Get Car Info Details

        editProfileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        changeCarTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditVehicleActivity.class));
            }
        });

        return av;
    }

    private void getDriverDetails() {
        driversRef.child(BaseFragment.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DriverModel model = dataSnapshot.getValue(DriverModel.class);

                        String name = model.getFullName();
                        profileNameTV.setText(name);

                        String imgUrl = model.getImgURL();
                        Picasso.with(getActivity())
                                .load(imgUrl)
                                .placeholder(R.drawable.profile_avatar)
                                .into(profileCIV);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void initViews(View av) {
        profileCIV = av.findViewById(R.id.profileCIV);
        carImg = av.findViewById(R.id.carImg);
        profileNameTV = av.findViewById(R.id.profileNameTV);
        editProfileTV = av.findViewById(R.id.editProfileTV);
        carNameTV = av.findViewById(R.id.carNameTV);
        changeCarTV = av.findViewById(R.id.changeCarTV);

        driverProfileEL = av.findViewById(R.id.driverProfileEL);

        final LinearLayout profileContentLayout = driverProfileEL.findViewById(R.id.profileContentLayout);

        driverProfileEL.getHeaderLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileContentLayout.setVisibility(View.VISIBLE);
            }
        });

    }

}
