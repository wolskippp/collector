package com.wolski_msk.collector.sample.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by wpiot on 08.08.2016.
 */
public class utils {

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





}
