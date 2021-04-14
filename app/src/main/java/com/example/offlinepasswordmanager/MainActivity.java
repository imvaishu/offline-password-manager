package com.example.offlinepasswordmanager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    DatabaseListener databaseListener;
    CredentialAdapter adapter;
    private List<Credential> defaultCredentials;
    private List<Credential> credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseListener = DatabaseListener.getInstance(getApplicationContext());
        this.defaultCredentials = new ArrayList<>();

        ListView listView = findViewById(R.id.mobile_list);
        adapter = new CredentialAdapter(getApplicationContext(), defaultCredentials, getPopupView(R.layout.credential_popup), getPopupView(R.layout.delete_confirmation_popup));
        listView.setAdapter(adapter);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            credentials = databaseListener.getCredentials();
            defaultCredentials.addAll(credentials);
        });

        initializeFabButtons();
    }

    private void initializeFabButtons() {
        FloatingActionButton fab = findViewById(R.id.fab);

        FloatingActionButton importFab = findViewById(R.id.import_fab);
        FloatingActionButton exportFab = findViewById(R.id.export_fab);
        FloatingActionButton saveFab = findViewById(R.id.save_fab);

        importFab.setVisibility(View.GONE);
        exportFab.setVisibility(View.GONE);
        saveFab.setVisibility(View.GONE);
        fab.setOnClickListener(toggleFabViewOnClickListener(fab, importFab, exportFab, saveFab));

        exportFab.setOnClickListener(exportOnClickListener());
        importFab.setOnClickListener(importOnClickListener());
        saveFab.setOnClickListener(saveCredentialPopup(saveFab));
    }

    private View.OnClickListener toggleFabViewOnClickListener(FloatingActionButton fab, FloatingActionButton importFab, FloatingActionButton export_fab, FloatingActionButton save_fab) {
        return view -> {
            if (!importFab.isOrWillBeShown()) {
                importFab.show();
                export_fab.show();
                save_fab.show();
                fab.setImageResource(R.drawable.hide_fabs_icon);
            } else {
                importFab.hide();
                export_fab.hide();
                save_fab.hide();
                fab.setImageResource(R.drawable.show_fabs_icon);
            }
        };
    }

    private View.OnClickListener importOnClickListener() {
        return view -> {
            Toast.makeText(MainActivity.this, "imported", Toast.LENGTH_SHORT).show();
        };
    }

    private View.OnClickListener exportOnClickListener() {
        return view -> {
            Toast.makeText(MainActivity.this, "exported", Toast.LENGTH_SHORT).show();
        };
    }

    private View.OnClickListener saveCredentialPopup(FloatingActionButton fab) {
        return view -> {
            View popupView = getPopupView(R.layout.popup_window);
            final PopupWindow popupWindow = getPopupWindow(fab, popupView);

            EditText label = popupView.findViewById(R.id.label);
            label.addTextChangedListener(new MyTextWatcher(label, credentials).invoke());

            EditText username = popupView.findViewById(R.id.username);
            EditText password = popupView.findViewById(R.id.subPassword);

            Button save = popupView.findViewById(R.id.save);

            save.setOnClickListener(saveOnClickListener(popupWindow, label, username, password));
        };
    }

    private View.OnClickListener saveOnClickListener(PopupWindow popupWindow, EditText label, EditText username, EditText password) {
        return v1 -> {
            String labelAsString = label.getText().toString();
            String usernameAsString = username.getText().toString();
            String passwordAsString = password.getText().toString();

            Credential credential = new Credential(labelAsString);

            if (labelAsString.equals("") || usernameAsString.equals("") || passwordAsString.equals("") || isLabelAlreadyPresentInDB(labelAsString)) {
                return;
            }

            saveCredentialsInEncryptedFormat(credential, usernameAsString, passwordAsString);
            saveCredentialToDbAndAdapter(credential);
            popupWindow.dismiss();
        };
    }

    private void saveCredentialToDbAndAdapter(Credential credential) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            databaseListener.insertToDb(credential);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.add(credential);
                    assert adapter != null;
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }

    private boolean isLabelAlreadyPresentInDB(String label) {
        return credentials.stream().anyMatch(e -> label.equals(e.label));
    }

    private void saveCredentialsInEncryptedFormat(Credential credential, String username, String password) {
        EncryptedSharedPref.save(getApplicationContext(), credential.label + "username", username);
        EncryptedSharedPref.save(getApplicationContext(), credential.label + "password", password);
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