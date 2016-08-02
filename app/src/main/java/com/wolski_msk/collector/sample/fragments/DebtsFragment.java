package com.wolski_msk.collector.sample.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.adapters.ViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wpiot on 27.07.2016.
 */
public class DebtsFragment extends Fragment implements View.OnClickListener{

    public static String [] lend ={"Android dfd","PHP fdsf","Jquery fsfsd","JavaScript fdsfs"};
    public static String [] borrowed ={"Piotr Wolski","Wojtek","Kuba Piorek","Magda kruk","Elo Melo"};

    ListView lv_sort;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public static Fragment newInstance(String jsondata)
    {


//        JSONArray friendslist;
//        ArrayList<String> friends = new ArrayList <>();
//        try {
//            friendslist = new JSONArray(jsondata);
//
//            if(friendslist.length() == 0)
//                return new DebtsFragment();
//
//            for (int l=0; l< friendslist.length(); l++) {
//                friends.add(friendslist.getJSONObject(l).getString("name"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        lend = (String[]) friends.toArray();
//



        return new DebtsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.debts, container, false);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(),getChildFragmentManager(), borrowed, lend);
        viewPager.setAdapter(viewPagerAdapter);


        tabLayout.setupWithViewPager(viewPager);


        lv_sort = (ListView) view.findViewById(R.id.listView2);

        final ArrayList<String> listItems = new ArrayList<>();
        listItems.add(this.getString(R.string.date));
        listItems.add("Name");
        listItems.add("Amount");
        listItems.add("Title");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>( getContext(), android.R.layout.simple_list_item_1, listItems );
        lv_sort.setAdapter( adapter );
        lv_sort.setVisibility(View.INVISIBLE);


        view.findViewById(R.id.imageButton).setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.imageButton:
                if(lv_sort.getVisibility() == View.INVISIBLE) {
                    lv_sort.setVisibility(View.VISIBLE);
                }
                else {
                    lv_sort.setVisibility(View.INVISIBLE);
                }
                break;

        }
    }
}
