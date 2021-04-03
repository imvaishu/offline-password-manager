package com.example.offlinepasswordmanager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter adapter;
    ArrayList<String> listItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, listItems) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setOnClickListener(v -> {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.credential_popup, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                    TextView label = popupView.findViewById(R.id.credential_label);
                    String labelName = listItems.get(position);
                    label.setText("Label : " + labelName);

                    TextView usernameView = popupView.findViewById(R.id.credential_username);
                    String username = EncryptedSharedPref.getPassword(getApplicationContext(), labelName + "username");
                    usernameView.setText("Username : " + username);

                    TextView passwordView = popupView.findViewById(R.id.credential_password);
                    String password = EncryptedSharedPref.getPassword(getApplicationContext(), labelName + "password");
                    passwordView.setText("Password : " + password);
                });
                return view;
            }
        };

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_window, null);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.showAtLocation(fab, Gravity.CENTER, 0, 0);

            EditText label = popupView.findViewById(R.id.label);
            EditText username = popupView.findViewById(R.id.username);
            EditText password = popupView.findViewById(R.id.subPassword);

            Button save = popupView.findViewById(R.id.save);

            save.setOnClickListener(v1 -> {
                addItems(v1, label, username, password);
                popupWindow.dismiss();
            });
        });
    }

    public void addItems(View v, EditText labelName, EditText key, EditText value) {
        String label = labelName.getText().toString();
        String username = key.getText().toString();
        String password = value.getText().toString();

        EncryptedSharedPref.savePassword(getApplicationContext(), label + "username", username);
        EncryptedSharedPref.savePassword(getApplicationContext(), label + "password", password);

        listItems.add(label);
        assert adapter != null;
        adapter.notifyDataSetChanged();
    }
}