package com.wolski_msk.collector.sample.fragments.AddingScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.utils.utils;

/**
 * Created by wpiot on 15.09.2016.
 */
public class AddNewDebtFragment extends Fragment  implements View.OnClickListener{


    private TextView BottomHintChoose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    public static Fragment newInstance()
    {



        return new AddNewDebtFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.first_add_screen, container, false);

        BottomHintChoose = (TextView) view.findViewById(R.id.initial_label);

        BottomHintChoose.setText(utils.CapitalizeFirstLetter(getString(R.string.stage_1_adding, getString(R.string.borrow_label), getString(R.string.money_label)).toLowerCase()));

        ((TabLayout) view.findViewById(R.id.borrow_lend_tab)).setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String bor_len = tab.getPosition() == 0 ? getString(R.string.borrow_label).toLowerCase() : getString(R.string.lend_label).toLowerCase();
                String mon_obj = ((TabLayout) view.findViewById(R.id.money_object_tab)).getSelectedTabPosition() == 0 ? getResources().getString(R.string.money_label).toLowerCase() : getResources().getString(R.string.object_label).toLowerCase();
                BottomHintChoose.setText(utils.CapitalizeFirstLetter(getString(R.string.stage_1_adding, bor_len, mon_obj)));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        ((TabLayout) view.findViewById(R.id.money_object_tab)).setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String bor_len = ((TabLayout) view.findViewById(R.id.borrow_lend_tab)).getSelectedTabPosition() == 0 ? getString(R.string.borrow_label).toLowerCase() : getString(R.string.lend_label).toLowerCase();
                String mon_obj = (tab.getPosition() == 0 ? getString(R.string.money_label).toLowerCase() : getString(R.string.object_label).toLowerCase());
                BottomHintChoose.setText(utils.CapitalizeFirstLetter(getString(R.string.stage_1_adding, bor_len, mon_obj)));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();




    }
}
