package com.wolski_msk.collector.sample.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bumptech.glide.load.engine.Resource;
import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.fragments.TabFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String [] borrow;
    private String [] lend;
    private Context context;

    public ViewPagerAdapter(Context context, FragmentManager fm, String []namesBorrow, String[] namesLend) {
        super(fm);
        this.borrow = namesBorrow;
        this.lend = namesLend;
        this.context = context;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return (position ==0) ?
                context.getString(R.string.borrow_label):
                context.getString(R.string.lend_label);
    }


    @Override
    public Fragment getItem(int position) {
       return (position ==0) ?
          TabFragment.newInstance(this.borrow):
          TabFragment.newInstance(this.lend);

    }

    @Override
    public int getCount() {
        return 2;
    }

}