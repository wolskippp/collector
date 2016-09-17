package com.wolski_msk.collector.sample.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.fragments.TabFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String [] borrow;
    private String [] lend;
    private String [] borrowObj;
    private String [] lendObj;
    private Context context;

    public ViewPagerAdapter(Context context, FragmentManager fm, String []namesBorrow, String[] namesLend, String []borrowObj, String [] lendObj) {
        super(fm);
        this.borrow = namesBorrow;
        this.lend = namesLend;
        this.borrowObj = borrowObj;
        this.lendObj = lendObj;
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
          TabFragment.newInstance(this.borrow,0):
          TabFragment.newInstance(this.lend,1);



    }





    @Override
    public int getCount() {
        return 2;
    }

}