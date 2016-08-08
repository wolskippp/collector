package com.wolski_msk.collector.sample.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.transition.MorphDialogToFab;
import com.wolski_msk.collector.sample.transition.MorphFabToDialog;
import com.wolski_msk.collector.sample.transition.MorphTransition;

import org.w3c.dom.Text;


public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewGroup container;
    TextView borrow_money_label;
    ViewFlipper viewFlipper;
    int RESULT_LOAD_IMAGE =0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        container = (ViewGroup) findViewById(R.id.container);

        container.findViewById(R.id.next).setOnClickListener(this);
        container.findViewById(R.id.close).setOnClickListener(this);
        container.findViewById(R.id.prev).setOnClickListener(this);
        borrow_money_label = (TextView)findViewById(R.id.initial_label);

        ( (TabLayout)findViewById(R.id.borrow_lend_tab)).setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String mon_obj =  ((TabLayout)findViewById(R.id.money_object_tab)).getSelectedTabPosition() ==0?"money.":"object.";
                borrow_money_label.setText(tab.getPosition() == 0 ? "You borrowed " + mon_obj: "You lend " + mon_obj ) ;

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ( (TabLayout)findViewById(R.id.money_object_tab)).setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String bor_len =  ((TabLayout)findViewById(R.id.borrow_lend_tab)).getSelectedTabPosition() ==0?"borrowed ":"lend ";
                borrow_money_label.setText(tab.getPosition() == 0 ? "You "+ bor_len + "money.": "You " + bor_len + "object." ) ;

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupSharedEelementTransitions2();

         viewFlipper = (ViewFlipper)findViewById(R.id.flipper);
//
//        View.OnClickListener dismissListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if((viewFlipper != null ? viewFlipper.getDisplayedChild() : 0) ==0){
//                    viewFlipper.setInAnimation(getApplication(), R.anim.from_down_to_up);
//                    viewFlipper.setOutAnimation(getApplication(), R.anim.from_up_to_down);
//
//                    viewFlipper.showNext();
//                }
//                else {
//                    viewFlipper.setOutAnimation(getApplication(), R.anim.from_up_to_down);
//                    viewFlipper.setInAnimation(getApplication(), R.anim.from_down_to_up);
//
//                    viewFlipper.showPrevious();
//                }
//
//                //dismiss();
//            }
//        };
       // container.setOnClickListener(dismissListener);
      //  container.findViewById(R.id.close).setOnClickListener(dismissListener);




    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setupSharedEelementTransitions2() {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(50f);
        arcMotion.setMinimumVerticalAngle(50f);

        Interpolator easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);


        MorphTransition sharedEnter = new MorphTransition(ContextCompat.getColor(this, R.color.fab_background_color),
                ContextCompat.getColor(this, R.color.dialog_background_color), 100, getResources().getDimensionPixelSize(R.dimen.dialog_corners), true);
        sharedEnter.setPathMotion(arcMotion);
        sharedEnter.setInterpolator(easeInOut);

        MorphTransition sharedReturn = new MorphTransition(ContextCompat.getColor(this, R.color.dialog_background_color),
                ContextCompat.getColor(this, R.color.fab_background_color), getResources().getDimensionPixelSize(R.dimen.dialog_corners), 100,  false);
        sharedReturn.setPathMotion(arcMotion);
        sharedReturn.setInterpolator(easeInOut);

        if (container != null) {
            sharedEnter.addTarget(container);
            sharedReturn.addTarget(container);
        }
        getWindow().setBackgroundDrawableResource(R.color.guillotine_background);
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void dismiss() {
        setResult(Activity.RESULT_CANCELED);
        finishAfterTransition();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id)
        {
            case R.id.button:

                verifyStoragePermissions(this);

                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;

            case R.id.close:
                dismiss();
                break;

            case R.id.next:

                if(viewFlipper.getDisplayedChild() ==0) {

                    if(((TabLayout)findViewById(R.id.money_object_tab)).getSelectedTabPosition() ==0) {

                        ((TextView)findViewById(R.id.text_label_money)).setText(borrow_money_label.getText());
                        viewFlipper.showNext();
                    }
                    else {
                        ((TextView)findViewById(R.id.text_label_object)).setText(borrow_money_label.getText());
                        viewFlipper.setDisplayedChild(2);

                    }

                    findViewById(R.id.prev).setEnabled(true);
                    ((Button)v).setText("Submit");
                }
                else
                {
                    Toast.makeText(DialogActivity.this, "Congrats you added an item", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.prev:
                ((Button)findViewById(R.id.next)).setText("Next");
                v.setEnabled(false);
                viewFlipper.setDisplayedChild(0);

        }

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView);

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }
}
