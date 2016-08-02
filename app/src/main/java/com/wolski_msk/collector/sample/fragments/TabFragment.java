package com.wolski_msk.collector.sample.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.adapters.ListItemAdapter;

/**
 * Created by Admin on 11-12-2015.
 */
public class TabFragment extends Fragment {

    private ListView lv;
    private static final String DESCRIBABLE_KEY = "describable_key";

    public static TabFragment newInstance(String[] describable) {
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, describable);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs, container, false);

        String [] names  = (String[]) getArguments().getSerializable(
                DESCRIBABLE_KEY);

        lv=(ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new ListItemAdapter(getContext(), names));


        return view;
    }


}