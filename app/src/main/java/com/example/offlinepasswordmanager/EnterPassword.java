package com.example.offlinepasswordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterPassword extends AppCompatActivity {

    EditText editText;
    Button enter;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        SharedPreferences sharedPreferences = getSharedPreferences("DEMO", 0);
        password = sharedPreferences.getString("PASSWORD", "");


        editText = (EditText) findViewById(R.id.password);
        enter = (Button) findViewById(R.id.enterbtn);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();

                if (text.equals(password)) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EnterPassword.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}