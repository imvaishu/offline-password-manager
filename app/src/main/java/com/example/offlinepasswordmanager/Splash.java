package com.example.offlinepasswordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    TextView textView;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        password = EncryptedSharedPref.get(this, "PASSWORD");
        textView = findViewById(R.id.loading);
        //TODO: Write 100% unit test
        Handler handler = new Handler();
        handler.postDelayed(getRunnableHandler(), 2000);
    }

    private Runnable getRunnableHandler() {
        return () -> {
            Intent intent;
            if (password.equals("")) {
                intent = new Intent(getApplicationContext(), MasterPasswordCredential.class);

            } else {
                intent = new Intent(getApplicationContext(), MasterPassword.class);
            }
            startActivity(intent);
            finish();
        };
    }
}