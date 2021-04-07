package com.example.offlinepasswordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MasterPasswordCredential extends AppCompatActivity {
    EditText editText1, editText2;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        editText1 = (EditText) findViewById(R.id.password1);
        editText2 = (EditText) findViewById(R.id.password2);
        confirm = (Button) findViewById(R.id.confirmbtn);

        confirm.setOnClickListener(getOnClickListener());
    }

    private View.OnClickListener getOnClickListener() {
        return v -> {
            String password1 = editText1.getText().toString();
            String password2 = editText2.getText().toString();

            if (password1.equals("") || password2.equals("")) {
                Toast.makeText(MasterPasswordCredential.this, "Please Enter Password!", Toast.LENGTH_SHORT).show();

            } else {
                if (password1.equals(password2)) {
                    EncryptedSharedPref.save(MasterPasswordCredential.this.getApplicationContext(), "PASSWORD", password1);
                    Intent intent = new Intent(MasterPasswordCredential.this.getApplicationContext(), MainActivity.class);
                    MasterPasswordCredential.this.startActivity(intent);
                    MasterPasswordCredential.this.finish();
                } else {
                    Toast.makeText(MasterPasswordCredential.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();

                }
            }
        };
    }
}