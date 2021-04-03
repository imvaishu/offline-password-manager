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

        adapter = getAdapter();

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
                addItems(v1, label, username, password);
                popupWindow.dismiss();
            });
        });
    }

    private ArrayAdapter<String> getAdapter() {
        return new ArrayAdapter<String>(this, R.layout.activity_listview, listItems) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setOnClickListener(v -> {
                    View popupView = getPopupView(R.layout.credential_popup);

                    final PopupWindow popupWindow = getPopupWindow(view, popupView);
                    String labelName = listItems.get(position);
                    String username = EncryptedSharedPref.get(getApplicationContext(), labelName + "username");
                    String password = EncryptedSharedPref.get(getApplicationContext(), labelName + "password");

                    setLabel(popupView, "Label : " + labelName, R.id.credential_label);
                    setLabel(popupView, "Username : " + username, R.id.credential_username);
                    setLabel(popupView, "Password : " + password, R.id.credential_password);

                    Button back = popupView.findViewById(R.id.back);

                    back.setOnClickListener(v1 -> {
                        popupWindow.dismiss();
                    });
                });
                return view;
            }
        };
    }

    public void addItems(View v, EditText labelName, EditText key, EditText value) {
        String label = labelName.getText().toString();
        String username = key.getText().toString();
        String password = value.getText().toString();

        EncryptedSharedPref.save(getApplicationContext(), label + "username", username);
        EncryptedSharedPref.save(getApplicationContext(), label + "password", password);

        listItems.add(label);
        assert adapter != null;
        adapter.notifyDataSetChanged();
    }

    private void setLabel(View popupView, String labelName, int id) {
        TextView label = popupView.findViewById(id);
        label.setText(labelName);
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