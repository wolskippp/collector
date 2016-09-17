package com.wolski_msk.collector.sample.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.fragments.NavBar.DebtsFragment;
import com.wolski_msk.collector.sample.fragments.NavBar.LogOutFragment;
import com.wolski_msk.collector.sample.fragments.NavBar.ProfileFragment;
import com.wolski_msk.collector.sample.fragments.NavBar.SettingsFragment;
import com.wolski_msk.collector.sample.utils.SingletonSettings;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {


    private static int profileImageLoaded = 0;
    private static int profileUsernameLoaded = 0;
    private boolean doubleBackToExitPressedOnce = false;


    @BindView(R.id.chosen_option)
    TextView chosen_option;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.content_hamburger)
    View contentHamburger;
    @BindView(R.id.nav_view)
    NavigationView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        if (AppCompatDelegate.getDefaultNightMode()
//                == AppCompatDelegate.MODE_NIGHT_YES) {
//            setTheme(R.style.FeedActivityThemeDark);
//        }
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity);
        ButterKnife.bind(this);


        View headerViewNavBar = view.getHeaderView(0);


        InitializeNavBarHeader(headerViewNavBar);


        view.setNavigationItemSelectedListener(this);
        contentHamburger.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_layout, DebtsFragment.newInstance(intent.getStringExtra("jsondata")), DebtsFragment.class.getName()).commit();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    private void InitializeNavBarHeader(View headerViewNavBar) {
        Bitmap profile_pic = SingletonSettings.getInstance().getProfilePic();
        String username = SingletonSettings.getInstance().getProfileName();
        boolean profilePicReady = false;
        boolean profileUsernameReady = false;


        if (profile_pic != null) {
            setProfileImageFromClass(headerViewNavBar,profile_pic);
            profilePicReady = true;

        }
        if (!TextUtils.isEmpty(username)) {
            setProfileUsernameFromClass(headerViewNavBar,username);
            profileUsernameReady = true;
        }

        if (profilePicReady && profileUsernameReady)
            return ;



        boolean isFacebookLog = SingletonSettings.getInstance().getLoginMethod() == SingletonSettings.LoginMethod.facebook;

        if (isFacebookLog) {

            if (!profilePicReady)
                concurrentThreadToUpdateUIImageProfile();
            if (!profileUsernameReady)
                concurrentThreadToUpdateUIImageUsername();

        }
        else
        {
            if (!profilePicReady)
                setDefaultNavBarPicture();
            if (!profileUsernameReady)
                setDefaultNavBarUsername();
        }

    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            Fragment myFragment = getSupportFragmentManager().findFragmentByTag(DebtsFragment.class.getName());
            if (myFragment == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout, new DebtsFragment(), DebtsFragment.class.getName()).commit();
                chosen_option.setText(getString(R.string.feed));
                Menu nav_Menu = view.getMenu();
                nav_Menu.findItem(R.id.debt_group).setChecked(true);
            } else {

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

        int chosen_string = 0;
        Fragment next_class = null;

        int id = item.getItemId();


        if (item.isChecked()) {
            open_close_menu();
            return false;
        }


        switch (id) {
            case R.id.profile_group:
                chosen_string = R.string.profile;
                next_class = new ProfileFragment();
                break;
            case R.id.debt_group:
                chosen_string = R.string.feed;
                next_class = new DebtsFragment();
                break;
            case R.id.log_out_group:
                chosen_string = R.string.log_out;
                next_class = new LogOutFragment();
                break;
            case R.id.settings_group:
                chosen_string = R.string.settings;
                next_class = new SettingsFragment();
                break;



        }


        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout, next_class, next_class.getClass().getName()).commit();
        chosen_option.setText(getString(chosen_string));

        open_close_menu();
        item.setChecked(true);

        return false;

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.content_hamburger) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                mDrawerLayout.closeDrawer(GravityCompat.START);
            else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }

    }


    //TODO: polaczyc w jedna funkcje
    private void concurrentThreadToUpdateUIImageProfile() {
        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(5);
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                switch (profileImageLoaded) {
                    case 1:
                        correctUpdateOnNavBarProfilePic();
                        exec.shutdown();
                        break;
                    case -1:
                        errorUpdateOnNavBarProfilePic();
                        exec.shutdown();
                        break;
                    default:
                        break;
                }

            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    private void concurrentThreadToUpdateUIImageUsername() {
        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(5);
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                switch (profileUsernameLoaded) {
                    case 1:
                        correctUpdateOnNavBarUsername();
                        exec.shutdown();
                        break;
                    case -1:
                        errorUpdateOnNavBarUsername();
                        exec.shutdown();
                        break;
                    default:
                        break;
                }

            }
        }, 0, 3, TimeUnit.SECONDS);
    }


    private void setProfileImageFromClass(View headerView, Bitmap profilePicture) {
        ImageView myProfilePicNavBar = (ImageView) headerView.findViewById(R.id.imageView);
        myProfilePicNavBar.setImageBitmap(profilePicture);
    }


    private void setProfileUsernameFromClass(View headerView,String username) {
        ((TextView) headerView.findViewById(R.id.nameTextView)).setText(username);
    }

    private void correctUpdateOnNavBarProfilePic() {

        runOnUiThread(new Runnable() {
            public void run() {
                View headerView = view.getHeaderView(0);

                Bitmap profilePic = SingletonSettings.getInstance().getProfilePic();

                if (profilePic != null)
                    ((ImageView) headerView.findViewById(R.id.imageView)).setImageBitmap(profilePic);

            }
        });
    }

    private void errorUpdateOnNavBarProfilePic() {

        runOnUiThread(new Runnable() {
            public void run() {
                setDefaultNavBarPicture();

            }
        });
    }


    private void setDefaultNavBarPicture()
    {
        View headerView = view.getHeaderView(0);
        ((ImageView) headerView.findViewById(R.id.imageView)).setImageResource(R.drawable.default_profile);
    }




    private void errorUpdateOnNavBarUsername() {

        runOnUiThread(new Runnable() {
            public void run() {

                setDefaultNavBarUsername();
            }
        });
    }


    private void setDefaultNavBarUsername()
    {
        View headerView = view.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.nameTextView)).setText(R.string.no_name_nav_bar);
    }


    private void correctUpdateOnNavBarUsername() {

        runOnUiThread(new Runnable() {
            public void run() {
                View headerView = view.getHeaderView(0);

                String username = SingletonSettings.getInstance().getProfileName();

                ((TextView) headerView.findViewById(R.id.nameTextView)).setText(username);

            }
        });
    }


    private void open_close_menu() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    public static void profilePicture(int profilePictureResult) {
        profileImageLoaded = profilePictureResult;
    }

    public static void profileUsername(int profileUsernameResult) {
        profileUsernameLoaded = profileUsernameResult;
    }


}
