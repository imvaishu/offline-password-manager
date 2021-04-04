package com.example.offlinepasswordmanager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CredentialAdapter adapter;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "offline-password-manager").build();
        CredentialDAO credentialDao = db.credentialDAO();
        List<Credential> defaultCredentials = new ArrayList<>();

        AsyncTask.execute(() -> {
            List<Credential> credentials = credentialDao.getAll();
            defaultCredentials.addAll(credentials);
        });

        adapter = new CredentialAdapter(getApplicationContext(), R.layout.activity_listview, defaultCredentials, getPopupView(R.layout.credential_popup));

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            View popupView = getPopupView(R.layout.popup_window);

            final PopupWindow popupWindow = getPopupWindow(fab, popupView);

            EditText label = popupView.findViewById(R.id.label);
            EditText username = popupView.findViewById(R.id.username);
            EditText password = popupView.findViewById(R.id.subPassword);

            Button save = popupView.findViewById(R.id.save);

            save.setOnClickListener(v1 -> {
                AsyncTask.execute(() -> {
                    Credential credential = new Credential(label.getText().toString(), username.getText().toString(), password.getText().toString());
                    adapter.add(credential);
                    assert adapter != null;
                    credentialDao.insertAll(credential);
                });
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
            });
        });
    }

    private PopupWindow getPopupWindow(View fab, View popupView) {
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(fab, Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    private View getPopupView(int p) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(p, null);
    }
}