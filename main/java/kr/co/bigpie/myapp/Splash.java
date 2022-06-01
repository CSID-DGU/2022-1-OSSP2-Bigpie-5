package kr.co.bigpie.myapp;

import static kr.co.bigpie.myapp.Forms.LoginActivity.SHARED_PREFERENCES_NAME;
import static kr.co.bigpie.myapp.Forms.LoginActivity.USER_ID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.bigpie.myapp.Forms.LoginActivity;

public class Splash extends AppCompatActivity{
    SharedPreferences sharedPreferences;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(USER_ID, "");

        load();

    }

    public void load() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!user_id.equals("")) {
                    Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                    finish();
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        }, 2000);
    }
}

