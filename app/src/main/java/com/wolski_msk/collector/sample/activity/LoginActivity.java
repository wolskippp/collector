package com.wolski_msk.collector.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.utils.SingletonSettings;
import com.wolski_msk.collector.sample.utils.utils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.Button;

import static com.wolski_msk.collector.sample.R.layout.login;

/**
 * Created by wpiot on 01.08.2016.
 */


public class LoginActivity extends Activity implements View.OnClickListener {

    CallbackManager callbackManager;
    String PROFILE_PICTURE_NAME = "_profile_image";
    String PROFILE_NAME = "_profile_name";
    String PROFILE_PICTURE_EXTENSION = ".bmp";


    @BindView(R.id.back_to_app) Button backToAppButton;
    @BindView(R.id.login_button) LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(login);
        ButterKnife.bind(this);

        loginButton.setReadPermissions("user_friends");
        backToAppButton.setOnClickListener(this);

        if (isLoggedIn())
            startActivity(new Intent(this, MainActivity.class));


        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                        String userId = getUserID(loginResult);

                        SingletonSettings.getInstance().setLoginMethod(SingletonSettings.LoginMethod.facebook);


                        //Profile Picture in internal memory
                        if (!checkFileExistence(userId))
                            getFacebookProfilePicture(userId, getBaseContext());

                        //Username in internal memory
                       if(!checkSharedPropertyExistence(userId))
                            getFaceBookUsername(loginResult.getAccessToken(), PROFILE_NAME);

                    }


                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code


                    }
                });
    }


    public boolean isLoggedIn() {

        AccessToken currToken = AccessToken.getCurrentAccessToken();
        String currID = null;
        if(currToken !=null) {
            SingletonSettings.getInstance().setLoginMethod(SingletonSettings.LoginMethod.facebook);
            currID = currToken.getUserId();
            checkFileExistence(currID);
            checkSharedPropertyExistence(currID);

        }





        return true;

    }


    private boolean checkSharedPropertyExistence(String userId)
    {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (sharedpreferences.contains(userId + PROFILE_NAME)) {

            String username = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(userId + PROFILE_NAME, "no name found");
            SingletonSettings.getInstance().setProfileName(username);
            return true;
        }
        else
        return false;
    }

    private boolean checkFileExistence(String userId) {
        if (utils.fileExists(getBaseContext(), userId + PROFILE_PICTURE_NAME, PROFILE_PICTURE_EXTENSION)) {

            Bitmap profile_pic = utils.getImageBitmap(getBaseContext(), userId + PROFILE_PICTURE_NAME, PROFILE_PICTURE_EXTENSION);
            SingletonSettings.getInstance().setProfilePic(profile_pic);
            return true;
        }
        else
            return false;
    }

    private String getUserID(LoginResult loginResult) {
        return loginResult.getAccessToken().getUserId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        switch (resultCode) {
            case -1:
            startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                break;

        }

    }


    private void getFaceBookUsername(final AccessToken accessToken, final String profile_name_const) {
        new GraphRequest(
                accessToken, "/" + accessToken.getUserId(), null, HttpMethod.GET,

                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            String username = (String) response.getJSONObject().get("name");
                            SingletonSettings.getInstance().setProfileName(username);
                            MainActivity.profileUsername(1);
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(accessToken.getUserId()+profile_name_const, username).commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                            MainActivity.profileUsername(-1);
                        }

                    }
                }).executeAsync();
    }


    public void getFacebookProfilePicture(String userID, Context context) {
        URL imageUrl = null;
        try {
            imageUrl = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert imageUrl != null;
            new DownloadImage(context, PROFILE_PICTURE_NAME, PROFILE_PICTURE_EXTENSION, userID).execute(imageUrl.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.back_to_app:
                startActivity(new Intent(this, MainActivity.class));

        }
    }


    private static class DownloadImage extends AsyncTask<String, Void, Bitmap> {


        Context context;
        String PROFILE_PIC_NAME;
        String PROFILE_PIC_EXTENSION;
        String USER_ID;


        public DownloadImage(Context context, String PROFILE_PIC_NAME, String PROFILE_PIC_EXTENSION, String USER_ID) {
            this.context = context;
            this.PROFILE_PIC_NAME = PROFILE_PIC_NAME;
            this.PROFILE_PIC_EXTENSION = PROFILE_PIC_EXTENSION;
            this.USER_ID = USER_ID;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            Bitmap bitmap;

            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                SingletonSettings.getInstance().setProfilePic(bitmap);
                MainActivity.profilePicture(1);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            if (result == null)
                MainActivity.profilePicture(-1);
            else {
                saveImage(context, result, USER_ID + PROFILE_PIC_NAME, PROFILE_PIC_EXTENSION);
            }
        }


        public void saveImage(Context context, Bitmap b, String name, String extension) {
            name = name + "." + extension;
            FileOutputStream out;
            try {
                out = context.openFileOutput(name, Context.MODE_PRIVATE);
                b.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}

