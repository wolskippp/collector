package com.wolski_msk.collector.sample.activity;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.wolski_msk.collector.animation.GuillotineAnimation;
import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.Utils.SampleView;
import com.wolski_msk.collector.sample.fragments.DebtsFragment;
import com.wolski_msk.collector.sample.fragments.LogOutFragment;
import com.wolski_msk.collector.sample.fragments.ProfileFragment;
import com.wolski_msk.collector.sample.fragments.SettingsFragment;


import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final long RIPPLE_DURATION = 250;

    @BindView(R.id.chosen_option) TextView chosen_option;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.root) FrameLayout root;
    @BindView(R.id.content_hamburger) View contentHamburger;


    View guillotineMenu;
    GuillotineAnimation.GuillotineBuilder guilotineBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent intent = getIntent();
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        setContentView(R.layout.activity);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_layout,DebtsFragment.newInstance(intent.getStringExtra("jsondata"))).commit();

        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/canaro_extra_bold.otf");
        chosen_option.setTypeface(face1);



        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);


        guillotineMenu.findViewById(R.id.profile_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.debt_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.log_out_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.guillotine_view).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.settings_group).setOnClickListener(this);

        guilotineBuilder = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger);

        guilotineBuilder
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();




    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {

            case R.id.profile_group:

                if(chosen_option.getText().equals(getString(R.string.profile))) {
                    open_close_menu();
                    return;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout,new ProfileFragment()).commit();
                chosen_option.setText(getString(R.string.profile));
                open_close_menu();

                break;
            case R.id.debt_group:
                if(chosen_option.getText().equals(getString(R.string.feed)))
                {
                    open_close_menu();
                    return;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout,new DebtsFragment()).commit();
                chosen_option.setText(getString(R.string.feed));
                open_close_menu();
                break;
            case R.id.log_out_group:
                if(chosen_option.getText().equals(getString(R.string.log_out)))
                {
                    open_close_menu();
                    return;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout,new LogOutFragment()).commit();
                chosen_option.setText(getString(R.string.log_out));
                open_close_menu();
                break;
            case R.id.settings_group:
                if(chosen_option.getText().equals(getString(R.string.settings)))
                {
                    open_close_menu();
                    return;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout,new SettingsFragment()).commit();
                chosen_option.setText(getString(R.string.settings));
                open_close_menu();
                break;
            case R.id.guillotine_view:

                break;
        }

    }



    private void open_close_menu()
    {
        guillotineMenu.findViewById(R.id.guillotine_hamburger).performClick();
    }

    private void initDebts()
    {
        SampleView sampleView = new SampleView();

        sampleView.setDecs("Desc desc desc desc desc desc desc ");
        sampleView.setWho("Piotr Wolski");
        sampleView.setWhen("19-09-2015");
        sampleView.setWhat("Pizza");
    }
}
