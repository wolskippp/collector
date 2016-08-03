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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.transition.MorphDialogToFab;
import com.wolski_msk.collector.sample.transition.MorphFabToDialog;
import com.wolski_msk.collector.sample.transition.MorphTransition;


public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewGroup container;

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


        verifyStoragePermissions(this);



        setupSharedEelementTransitions2();

        final ViewFlipper viewFlipper = (ViewFlipper)findViewById(R.id.flipper);

        View.OnClickListener dismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((viewFlipper != null ? viewFlipper.getDisplayedChild() : 0) ==0){
                    viewFlipper.setInAnimation(getApplication(), R.anim.from_down_to_up);
                    viewFlipper.setOutAnimation(getApplication(), R.anim.from_up_to_down);

                    viewFlipper.showNext();
                }
                else {
                    viewFlipper.setOutAnimation(getApplication(), R.anim.from_up_to_down);
                    viewFlipper.setInAnimation(getApplication(), R.anim.from_down_to_up);

                    viewFlipper.showPrevious();
                }

                //dismiss();
            }
        };
        container.setOnClickListener(dismissListener);
        container.findViewById(R.id.close).setOnClickListener(dismissListener);




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
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

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

            Bitmap outputFile = null;
            try {
                 outputFile = BitmapFactory.decodeFile(picturePath);
            }
            catch (Exception exp)
            {
                exp.printStackTrace();
            }
            imageView.setImageBitmap(outputFile);

        }

    }
}
