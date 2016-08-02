package com.wolski_msk.collector.sample.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolski_msk.collector.sample.R;

/**
 * Created by wpiot on 27.07.2016.
 */
public class LogOutFragment extends Fragment implements View.OnClickListener{




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout, container, false);

        return view;

    }

    @Override
    public void onClick(View v) {

    }
}

