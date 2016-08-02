package com.wolski_msk.collector.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.wolski_msk.collector.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
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


public class LoginActivity extends Activity implements View.OnClickListener{

    CallbackManager callbackManager;

    @BindView(R.id.back_to_app) Button backToAppButton;
    @BindView(R.id.login_button)LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(login);
        ButterKnife.bind(this);
        loginButton.setReadPermissions("user_friends");
        backToAppButton.setOnClickListener(this);

       if(isLoggedIn() != null)
       {

           getFaceBookFriends(isLoggedIn());
           startActivity(new Intent(this,MainActivity.class));
       }

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        getFaceBookFriends(loginResult.getAccessToken());

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


    public AccessToken isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(this,MainActivity.class));
    }


    private void getFaceBookFriends(final AccessToken accessToken)
    {
        new GraphRequest(
                accessToken,"/"+accessToken.getUserId(),null, HttpMethod.GET,

                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        try {

                            getFacebookProfilePicture(accessToken.getUserId(),getBaseContext());

                            String rawrawName = (String) response.getJSONObject().get("name");
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("my_name", rawrawName).commit();

                            JSONArray rawName = response.getJSONObject().getJSONArray("data");
                            intent.putExtra("jsondata", rawName.toString());
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();
    }


    public static Bitmap getFacebookProfilePicture(String userID,Context context){
        URL imageUrl = null;
        try {
            imageUrl = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            new DownloadImage(context).execute(imageUrl.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.back_to_app:
                startActivity(new Intent(this,MainActivity.class));

        }
    }


    private static class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        Bitmap bitmap = null;
        Context context;
        DownloadImage(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];


            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            saveImage(context,bitmap,"profile_pic","bmp");
        }


        public void saveImage(Context context, Bitmap b,String name,String extension){
            name=name+"."+extension;
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
