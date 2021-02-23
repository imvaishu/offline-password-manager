package com.example.offlinepasswordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Loading extends AppCompatActivity {

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);

        SharedPreferences sharedPreferences = getSharedPreferences("DEMO", 0);
        password = sharedPreferences.getString("PASSWORD", "");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (password.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), CreatePassword.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), EnterPassword.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000);
    }
}