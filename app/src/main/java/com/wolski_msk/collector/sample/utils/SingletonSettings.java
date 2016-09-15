package com.wolski_msk.collector.sample.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpiot on 13.09.2016.
 */
public class SingletonSettings {
    private static SingletonSettings instance = null;

    public  enum LoginMethod { facebook,google,collector,not_at_all}


    LoginMethod curr_method ;
    Bitmap profilePic = null;
    String profileName = null;
    List<String> phoneContactsNames = null;


    protected SingletonSettings( ) {
    }


    public static SingletonSettings getInstance( ) {
        if(instance == null) {

            instance = new SingletonSettings();
        }
        return instance;
    }




    public List<String> getPhoneContactsNames(Context context) {

        return phoneContactsNames !=null && !phoneContactsNames.isEmpty()  ?phoneContactsNames: fillWithContactNames(context) ;
    }


    public LoginMethod getLoginMethod() {
        return curr_method;
    }

    public void setLoginMethod(LoginMethod curr_method) {
        this.curr_method = curr_method;
    }



    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    private List<String> fillWithContactNames(Context context)
    {


        if(phoneContactsNames != null)
            phoneContactsNames.clear();
        else
            phoneContactsNames = new ArrayList<>();


        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);


            if ((cur != null ? cur.getCount() : 0) > 0) {
                assert cur != null;
                while (cur.moveToNext()) {

                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    phoneContactsNames.add(name);

                }

                cur.close();
            }

        return phoneContactsNames;

    }




}