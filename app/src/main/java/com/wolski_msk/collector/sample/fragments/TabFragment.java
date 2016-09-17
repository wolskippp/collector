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
    private static final String DEBT_TYPE = "DebtType";

    public static TabFragment newInstance(String[] describable, int type) {
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DEBT_TYPE,type);
        bundle.putSerializable(DESCRIBABLE_KEY, describable);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs, container, false);

        String [] names  = (String[]) getArguments().getSerializable(
                DESCRIBABLE_KEY);
        int type = (int)getArguments().getSerializable(DEBT_TYPE);

        if(type==0) {
            lv = (ListView) view.findViewById(R.id.listViewMoney);
            lv.setAdapter(new ListItemAdapter(getContext(), names));
        }
        else
        {
            lv = (ListView) view.findViewById(R.id.listViewObjects);
            lv.setAdapter(new ListItemAdapter(getContext(), names));
        }

        return view;
    }


}