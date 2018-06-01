package com.jjoey.transporterdriver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoey.transporterdriver.R;

public class RatingsFragment extends BaseFragment {

    private static final String TAG = RatingsFragment.class.getSimpleName();

    public RatingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ratings, container, false);
    }

}
