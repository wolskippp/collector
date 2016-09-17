package com.wolski_msk.collector.sample.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.fragments.AddingScreen.AddNewDebtFragment;
import com.wolski_msk.collector.sample.fragments.AddingScreen.AddingStage2;
import com.wolski_msk.collector.sample.fragments.NavBar.DebtsFragment;
import com.wolski_msk.collector.sample.transition.MorphTransition;
import com.wolski_msk.collector.sample.utils.SingletonSettings;
import com.wolski_msk.collector.sample.utils.utils;

import java.io.File;
import java.io.IOException;


public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TAKE_PHOTO_CODE = 2;
    private ViewGroup container;


    int RESULT_LOAD_IMAGE = 0;
    int RESULT_FILL_CONTACTS = 1;
    boolean new_phone = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);



        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new AddNewDebtFragment(), AddNewDebtFragment.class.getName()).commit();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            new_phone = true;


        utils.verifyStoragePermissions(this, 2);

        container = (ViewGroup) findViewById(R.id.container);


        container.findViewById(R.id.next).setOnClickListener(this);
        container.findViewById(R.id.close).setOnClickListener(this);
        container.findViewById(R.id.prev).setOnClickListener(this);

        utils.verifyStoragePermissions(this, 3);

        if (new_phone)
            setupSharedElementTransitions2();



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setupSharedElementTransitions2() {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(50f);
        arcMotion.setMinimumVerticalAngle(50f);

        Interpolator easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);


        MorphTransition sharedEnter = new MorphTransition(ContextCompat.getColor(this, R.color.fab_background_color),
                ContextCompat.getColor(this, R.color.dialog_background_color), 100, getResources().getDimensionPixelSize(R.dimen.dialog_corners), true);
        sharedEnter.setPathMotion(arcMotion);
        sharedEnter.setInterpolator(easeInOut);

        MorphTransition sharedReturn = new MorphTransition(ContextCompat.getColor(this, R.color.dialog_background_color),
                ContextCompat.getColor(this, R.color.fab_background_color), getResources().getDimensionPixelSize(R.dimen.dialog_corners), 100, false);
        sharedReturn.setPathMotion(arcMotion);
        sharedReturn.setInterpolator(easeInOut);

        if (container != null) {
            sharedEnter.addTarget(container);
            sharedReturn.addTarget(container);
        }
        getWindow().setBackgroundDrawableResource(R.color.carbon_white);
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void dismiss() {
        if (new_phone) {
            setResult(Activity.RESULT_CANCELED);
            finishAfterTransition();
        } else
            finish();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.addPictureButton:

                LinearLayout buttonsCamera = ((LinearLayout)findViewById(R.id.ImageButtonsLayout));

                buttonsCamera.setVisibility(buttonsCamera.getVisibility() == View.VISIBLE ? View.INVISIBLE:View.VISIBLE);

                break;
            case R.id.camera:
                getCameraShot();
                break;
            case R.id.gallery:

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;


            case R.id.close:
                dismiss();
                break;

            case R.id.next:

                Fragment currFragment =  getSupportFragmentManager().findFragmentByTag(AddNewDebtFragment.class.getName());

                if(currFragment != null && currFragment.isVisible()) {
                    View fragmentView = currFragment.getView();


                    boolean moneyObject = ((TabLayout) fragmentView.findViewById(R.id.money_object_tab)).getSelectedTabPosition() == 0;
                    boolean borrowLend = ((TabLayout) fragmentView.findViewById(R.id.borrow_lend_tab)).getSelectedTabPosition() == 0;
                    String topTitle = ((TextView) fragmentView.findViewById(R.id.initial_label)).getText().toString();



                    Bundle bundle = new Bundle();
                    bundle.putBoolean("moneyObject", moneyObject);
                    bundle.putBoolean("borrowLend", borrowLend);
                    bundle.putString("topTitle", topTitle);


                    AddingStage2 nextClass = new AddingStage2();
                    nextClass.setArguments(bundle);

                    ((Button) findViewById(R.id.prev)).setEnabled(true);
                    ((Button)v).setText(R.string.submit);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nextClass, nextClass.getClass().getName()).addToBackStack(null).commit();

                }
                else
                {
                    Toast.makeText(DialogActivity.this, "You added something", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.prev:
                ((Button) findViewById(R.id.next)).setText(R.string.next);
                v.setEnabled(false);
                AddNewDebtFragment nextClass = new AddNewDebtFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,nextClass , nextClass.getClass().getName()).commit();

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LinearLayout buttonsCamera = ((LinearLayout)findViewById(R.id.ImageButtonsLayout));

        buttonsCamera.setVisibility(buttonsCamera.getVisibility() == View.VISIBLE ? View.INVISIBLE:View.VISIBLE);

        if( resultCode == RESULT_OK) {



            if (requestCode == RESULT_LOAD_IMAGE)
                getImageFromStorage(resultCode, data);

            if (requestCode == TAKE_PHOTO_CODE)
                getImageFromStorage(resultCode, data);

        }
    }


    private void getCameraShot()
    {

        String file = "ELOMELOTESTO.jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    private void getImageFromStorage(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();


                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = (ImageView) findViewById(R.id.imageView);

                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
    }


}
