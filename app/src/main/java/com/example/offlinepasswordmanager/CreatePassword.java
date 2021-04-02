package com.example.offlinepasswordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class CreatePassword extends AppCompatActivity {

    EditText editText1, editText2;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        editText1 = (EditText) findViewById(R.id.password1);
        editText2 = (EditText) findViewById(R.id.password2);
        confirm = (Button) findViewById(R.id.confirmbtn);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = editText1.getText().toString();
                String password2 = editText2.getText().toString();

                if (password1.equals("") || password2.equals("")) {
                    Toast.makeText(CreatePassword.this, "Please Enter Password!", Toast.LENGTH_SHORT).show();

                } else {
                    if (password1.equals(password2)) {
                        try {
                            EncryptedSharedPref.savePassword(getApplicationContext(), "PASSWORD",password1);
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CreatePassword.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}