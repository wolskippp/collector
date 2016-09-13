package com.wolski_msk.collector.sample.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wolski_msk.collector.animation.GuillotineAnimation;
import com.wolski_msk.collector.sample.R;

import com.wolski_msk.collector.sample.fragments.DebtsFragment;
import com.wolski_msk.collector.sample.fragments.LogOutFragment;
import com.wolski_msk.collector.sample.fragments.ProfileFragment;
import com.wolski_msk.collector.sample.fragments.SettingsFragment;
import com.wolski_msk.collector.sample.utils.utils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {
    private static final long RIPPLE_DURATION = 100;

    private boolean doubleBackToExitPressedOnce = false;
    private String[] mPlanetTitles;
    private ListView mDrawerList;

    @BindView(R.id.chosen_option) TextView chosen_option;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.content_hamburger) View contentHamburger;



    ImageView prev_imageView;
    TextView prev_textView;
    View guillotineMenu;
    GuillotineAnimation.GuillotineBuilder guilotineBuilder;

    SparseIntArray idToDrawableMap =  new SparseIntArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent intent = getIntent();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity);
         ButterKnife.bind(this);





        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.nav_view);

        View headerView = view.getHeaderView(0);

        String name = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("my_name", "no name found");

        ImageView myProfilePicNavBar = (ImageView)headerView.findViewById(R.id.imageView);
        ((TextView)headerView.findViewById(R.id.nameTextView)).setText(name);


        myProfilePicNavBar.setImageBitmap(utils.getImageBitmap(getBaseContext(),"profile_pic","bmp"));

        view.setNavigationItemSelectedListener(this);


        contentHamburger.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_layout,DebtsFragment.newInstance(intent.getStringExtra("jsondata")),DebtsFragment.class.getName()).commit();

        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/canaro_extra_bold.otf");
//        chosen_option.setTypeface(face1);



        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }






    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else{

            Fragment myFragment = getSupportFragmentManager().findFragmentByTag(DebtsFragment.class.getName());
            if (myFragment == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout,new DebtsFragment(),DebtsFragment.class.getName()).commit();
                chosen_option.setText(getString(R.string.feed));
            }
            else {

                if (doubleBackToExitPressedOnce) {


                    super.onBackPressed();
                } else {
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Press twice to exit", Toast.LENGTH_SHORT).show();
                    new CountDownTimer(2500, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            doubleBackToExitPressedOnce = false;
                        }

                    }.start();


                }
            }


        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int chosen_string =0;
        Fragment next_class = null;
        int curr_imageView =0;
        int curr_textView =0;
        int id = item.getItemId();
        int selectedItem =0;
        switch (id)
        {

            case R.id.profile_group:

                chosen_string = R.string.profile;
                next_class = new ProfileFragment();
                curr_imageView = R.id.profile_imageView;
                curr_textView = R.id.profile_textView;
                break;
            case R.id.debt_group:
                chosen_string = R.string.feed;
                next_class = new DebtsFragment();
                curr_imageView = R.id.debts_imageView;
                curr_textView = R.id.debts_textView;
                break;
            case R.id.log_out_group:
                chosen_string = R.string.log_out;
                next_class = new LogOutFragment();
                curr_imageView = R.id.log_out_imageView;
                curr_textView = R.id.log_out_textView;
                break;
            case R.id.settings_group:
                chosen_string = R.string.settings;
                next_class = new SettingsFragment();
                curr_imageView = R.id.settings_imageView;
                curr_textView = R.id.settings_textView;
                break;
            case R.id.guillotine_view:


        }

        if(chosen_option.getText().equals(getString(chosen_string))) {
          open_close_menu();
            return false;
        }



        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout,next_class,next_class.getClass().getName()).commit();
        chosen_option.setText(getString(chosen_string));

        open_close_menu();
        item.setChecked(true);

            return false;

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if( id == R.id.content_hamburger)
        {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                mDrawerLayout.closeDrawer(GravityCompat.START);
            else
            {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }

    }
//        int chosen_string =0;
//        Fragment next_class = null;
//        int curr_imageView =0;
//        int curr_textView =0;
//        switch (id)
//        {
//
//            case R.id.profile_group:
//
//                chosen_string = R.string.profile;
//                next_class = new ProfileFragment();
//                curr_imageView = R.id.profile_imageView;
//                curr_textView = R.id.profile_textView;
//                break;
//            case R.id.debt_group:
//                chosen_string = R.string.feed;
//                next_class = new DebtsFragment();
//                curr_imageView = R.id.debts_imageView;
//                curr_textView = R.id.debts_textView;
//                break;
//            case R.id.log_out_group:
//                chosen_string = R.string.log_out;
//                next_class = new LogOutFragment();
//                curr_imageView = R.id.log_out_imageView;
//                curr_textView = R.id.log_out_textView;
//                break;
//            case R.id.settings_group:
//                chosen_string = R.string.settings;
//                next_class = new SettingsFragment();
//                curr_imageView = R.id.settings_imageView;
//                curr_textView = R.id.settings_textView;
//                break;
//            case R.id.guillotine_view:
//                return;
//
//        }
//
//        AccessibilityNodeInfo chosen_option;
//        if(chosen_option.getText().equals(getString(chosen_string))) {
//            open_close_menu();
//            return;
//        }
//
//        open_close_menu();
//
//
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout,next_class).commit();
//        chosen_option.setText(getString(chosen_string));
//
//        change_chosen_color(idToDrawableMap.get(prev_imageView.getId()),
//                idToDrawableMap.get(curr_textView),
//                curr_imageView,
//                curr_textView);
//    }
//
//
//
//    private void change_chosen_color(int prev_default_clicked_drawable,int curr_active_drawable, int curr_clicked_imageView, int curr_clicked_textView)
//    {
//        prev_imageView.setImageResource(prev_default_clicked_drawable); //change to normal from what you clicked
//        prev_textView.setTextColor(Color.WHITE); // white all the way
//
//        prev_imageView = (ImageView)guillotineMenu.findViewById(curr_clicked_imageView); //currently clicked image view
//        prev_textView = (TextView) guillotineMenu.findViewById(curr_clicked_textView); //currently clicked textview
//
//        prev_imageView.setImageResource(curr_active_drawable); //active drawable what you clicked
//        prev_textView.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.selected_item_color)); //selected color all the way
//
//    }
//
    private void open_close_menu()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
    }


}
