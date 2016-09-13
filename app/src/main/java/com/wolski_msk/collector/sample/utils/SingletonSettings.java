package com.wolski_msk.collector.sample.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.List;

/**
 * Created by wpiot on 13.09.2016.
 */
public class SingletonSettings {
    private static SingletonSettings instance = null;

    List<String> phoneContactsNames = null;

    protected SingletonSettings(Context context) {
        fillWithContactNames(context);
    }
    public static SingletonSettings getInstance(Context context) {
        if(instance == null) {
            instance = new SingletonSettings(context);
        }
        return instance;
    }


    private void fillWithContactNames(Context context)
    {
        phoneContactsNames.clear();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        assert cur != null;
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                phoneContactsNames.add(name);

            }
        }
    }




}