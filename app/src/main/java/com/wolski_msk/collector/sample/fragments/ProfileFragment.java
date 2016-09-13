package com.wolski_msk.collector.sample.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.utils.utils;

import java.io.FileInputStream;

/**
 * Created by wpiot on 27.07.2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

       String name = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("my_name", "no name found");

        ((ImageView)view.findViewById(R.id.profile_image)).setImageBitmap(utils.getImageBitmap(getContext(),"profile_pic","bmp"));
        ((TextView)view.findViewById(R.id.profile_name)).setText(name);

        return view;

        }

    @Override
    public void onClick(View v) {

    }



}


