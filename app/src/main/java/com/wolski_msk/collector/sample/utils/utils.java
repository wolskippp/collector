package com.wolski_msk.collector.sample.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by wpiot on 08.08.2016.
 */
public class utils {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };

    private static String[] PERMISSIONS_STORAGE_CONTACT = {
            Manifest.permission.READ_CONTACTS

    };



    public static boolean checkSorting(String[] arraylist) {
        boolean isSorted = true;
        for (int i = 1; i < arraylist.length; i++) {
            if (arraylist[i - 1].compareTo(arraylist[i]) > 0) {
                isSorted = false;
                break;
            }
        }
        return isSorted;
    }


    public static Bitmap getImageBitmap(Context context, String name, String extension){
        name=name+"."+extension;
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
    }


    public static boolean fileExists(Context context, String filename, String extension) {
        File file = context.getFileStreamPath(filename + "." +extension);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }


    public static void verifyStoragePermissions(Activity activity,int CODE) {
        // Check if we have write permission
        int permission_images = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE );
        int permission_contacts = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS );


        switch(CODE)
        {
            case 1:
                if (permission_images != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            activity,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }
                break;
            case 2:

                if(permission_contacts!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }

                break;

        }

        }





}
