package com.wolski_msk.collector.sample.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolski_msk.collector.animation.GuillotineAnimation;
import com.wolski_msk.collector.sample.R;

import com.wolski_msk.collector.sample.fragments.DebtsFragment;
import com.wolski_msk.collector.sample.fragments.LogOutFragment;
import com.wolski_msk.collector.sample.fragments.ProfileFragment;
import com.wolski_msk.collector.sample.fragments.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final long RIPPLE_DURATION = 100;

    @BindView(R.id.chosen_option) TextView chosen_option;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.root) FrameLayout root;
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





        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_layout,DebtsFragment.newInstance(intent.getStringExtra("jsondata"))).commit();

        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/canaro_extra_bold.otf");
        chosen_option.setTypeface(face1);



        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);


        prev_imageView = (ImageView) guillotineMenu.findViewById(R.id.debts_imageView);
        prev_textView = (TextView) guillotineMenu.findViewById(R.id.debts_textView);


        guillotineMenu.findViewById(R.id.profile_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.debt_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.log_out_group).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.guillotine_view).setOnClickListener(this);
        guillotineMenu.findViewById(R.id.settings_group).setOnClickListener(this);

        idToDrawableMap.put(R.id.profile_imageView,R.drawable.ic_profile);
        idToDrawableMap.put(R.id.debts_imageView,R.drawable.ic_feed);
        idToDrawableMap.put(R.id.log_out_imageView,R.drawable.ic_log_out);
        idToDrawableMap.put(R.id.settings_imageView,R.drawable.ic_settings);
        idToDrawableMap.put(R.id.profile_textView,R.drawable.ic_profile_active);
        idToDrawableMap.put(R.id.debts_textView,R.drawable.ic_feed_active);
        idToDrawableMap.put(R.id.log_out_textView,R.drawable.ic_log_out_active);
        idToDrawableMap.put(R.id.settings_textView,R.drawable.ic_settings_active);


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
        int chosen_string =0;
        Fragment next_class = null;
        int curr_imageView =0;
        int curr_textView =0;
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
                return;

        }

        if(chosen_option.getText().equals(getString(chosen_string))) {
            open_close_menu();
            return;
        }

        open_close_menu();


        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout,next_class).commit();
        chosen_option.setText(getString(chosen_string));

        change_chosen_color(idToDrawableMap.get(prev_imageView.getId()),
                idToDrawableMap.get(curr_textView),
                curr_imageView,
                curr_textView);
    }



    private void change_chosen_color(int prev_default_clicked_drawable,int curr_active_drawable, int curr_clicked_imageView, int curr_clicked_textView)
    {
        prev_imageView.setImageResource(prev_default_clicked_drawable); //change to normal from what you clicked
        prev_textView.setTextColor(Color.WHITE); // white all the way

        prev_imageView = (ImageView)guillotineMenu.findViewById(curr_clicked_imageView); //currently clicked image view
        prev_textView = (TextView) guillotineMenu.findViewById(curr_clicked_textView); //currently clicked textview

        prev_imageView.setImageResource(curr_active_drawable); //active drawable what you clicked
        prev_textView.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.selected_item_color)); //selected color all the way

    }

    private void open_close_menu()
    {
        guillotineMenu.findViewById(R.id.guillotine_hamburger).performClick();
    }


}
