package com.wolski_msk.collector.sample.fragments.NavBar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.wolski_msk.collector.sample.R;

/**
 * Created by wpiot on 27.07.2016.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        Switch nightMode = (Switch) view.findViewById(R.id.nightMode);
        nightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b)
                {
                    AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }
                else
                {
                    AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
        return view;

    }

    @Override
    public void onClick(View v) {

    }
}
