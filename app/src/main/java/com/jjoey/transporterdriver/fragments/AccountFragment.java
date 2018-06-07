package com.jjoey.transporterdriver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.jjoey.transporterdriver.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends BaseFragment {

    private static final String TAG = AccountFragment.class.getSimpleName();

    private CircleImageView profileCIV, carImg;
    private TextView profileNameTV, editProfileTV, carNameTV, changeCarTV;

    private ExpandableLayout driverProfileEL;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View av = inflater.inflate(R.layout.fragment_account, container, false);
        initViews(av);
        return av;
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
