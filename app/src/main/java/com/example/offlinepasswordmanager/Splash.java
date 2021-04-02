package com.example.offlinepasswordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Splash extends AppCompatActivity {
    TextView textView;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            password = EncryptedSharedPref.getPassword(this,"PASSWORD");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textView = findViewById(R.id.loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;
                if (password.equals("")) {
                    intent = new Intent(getApplicationContext(), CreatePassword.class);

                } else {
                    intent = new Intent(getApplicationContext(), EnterPassword.class);
                }
                startActivity(intent);
                finish();

            }
        }, 2000);


    }
}