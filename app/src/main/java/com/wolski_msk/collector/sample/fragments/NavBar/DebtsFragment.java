package com.wolski_msk.collector.sample.fragments.NavBar;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.activity.DialogActivity;
import com.wolski_msk.collector.sample.adapters.ViewPagerAdapter;
import com.wolski_msk.collector.sample.utils.utils;

import java.util.Arrays;

/**
 * Created by wpiot on 27.07.2016.
 */
public class DebtsFragment extends Fragment implements View.OnClickListener {

    public static String [] lend ={"ZZAndroid dfd","BPBBHP fdsf","JavaScript fdsfs"};
    public static String [] borrowed ={"Wojtek","Kuba Piorek","Magda kruk","Elo Melo"};
    public static String [] objLen ={"Piotr Wolski","Wojtek","Kuba Piorek","Elo Melo"};
    public static String [] objBor ={"Wojtek","Kuba Piorek","Elo Melo"};

    FloatingActionButton addFab;
    ListView lv_sort;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;


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

        addFab = (FloatingActionButton)view.findViewById(R.id.add_new_debt_button) ;
        addFab.setOnClickListener(this);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

         viewPagerAdapter = new ViewPagerAdapter(getContext(),getChildFragmentManager(), borrowed, lend,objBor,objLen);
        viewPager.setAdapter(viewPagerAdapter);


        tabLayout.setupWithViewPager(viewPager);


        lv_sort = (ListView) view.findViewById(R.id.listView2);


        view.findViewById(R.id.sortDebtsImageButton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Animation mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_reveal);
        addFab.setVisibility(View.VISIBLE);
        addFab.startAnimation(mAnimation);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.sortDebtsImageButton:

                showDeviceMenu(v);

            //    lv_sort.setVisibility(lv_sort.getVisibility() == View.INVISIBLE?View.VISIBLE:View.INVISIBLE);
                break;

            case R.id.add_new_debt_button:



                Intent intent = new Intent(getActivity(), DialogActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !((PowerManager) getActivity().getSystemService(Context.POWER_SERVICE)).isPowerSaveMode()) {
                    options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), addFab, getString(R.string.transition_dialog));
                    startActivityForResult(intent, 100, options.toBundle());
                }
                else
                    startActivity(intent);





        }
    }


    public void showDeviceMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.inflate(R.menu.sort_menu);



        getActivity().invalidateOptionsMenu();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.date:

                        if(viewPagerAdapter != null && !utils.checkSorting(lend)) {
                            Arrays.sort(lend);
                        Toast.makeText(getContext(), "jeden", Toast.LENGTH_SHORT).show();
                            viewPager.setAdapter(viewPagerAdapter);
                            viewPagerAdapter.notifyDataSetChanged();

                        }
                        return true;
                    case R.id.name:

                        if(viewPagerAdapter != null && !utils.checkSorting(borrowed)) {

                            Arrays.sort(borrowed);

                            Toast.makeText(getContext(), "dwa", Toast.LENGTH_SHORT).show();
                            viewPager.setAdapter( viewPagerAdapter );
                            viewPagerAdapter.notifyDataSetChanged();

                        }
                        return true;
                    default:
                        return false;
                }
            }

        });
        popup.show();
    }


}
