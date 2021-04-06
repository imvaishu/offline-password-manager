package com.example.offlinepasswordmanager;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    DatabaseListener databaseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseListener = DatabaseListener.getInstance(getApplicationContext());
        databaseListener.getCredentials();
        ListView listView = findViewById(R.id.mobile_list);
        databaseListener.attachAdapter(listView, getPopupView(R.layout.credential_popup), getPopupView(R.layout.delete_confirmation_popup));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(getSaveCredentialPopup(fab));
    }

    private View.OnClickListener getSaveCredentialPopup(FloatingActionButton fab) {
        return view -> {
            View popupView = getPopupView(R.layout.popup_window);
            final PopupWindow popupWindow = getPopupWindow(fab, popupView);

            EditText label = popupView.findViewById(R.id.label);
            label.addTextChangedListener(new MyTextWatcher(label,databaseListener).invoke());

            EditText username = popupView.findViewById(R.id.username);
            EditText password = popupView.findViewById(R.id.subPassword);

            Button save = popupView.findViewById(R.id.save);

            save.setOnClickListener(getOnClickListenerOfSave(popupWindow, label, username, password));
        };
    }

    private View.OnClickListener getOnClickListenerOfSave(PopupWindow popupWindow, EditText label, EditText username, EditText password) {
        return v1 -> {
            String labelAsString = label.getText().toString();
            String usernameAsString = username.getText().toString();
            String passwordAsString = password.getText().toString();

            Credential credential = new Credential(labelAsString);

            if (labelAsString.equals("") || usernameAsString.equals("") || passwordAsString.equals("") || databaseListener.isLabelAlreadyPresentInDB(labelAsString)) {
                return;
            }

            saveCredentialsInEncryptedFormat(credential, usernameAsString, passwordAsString);
            databaseListener.insertToDb(credential);
            popupWindow.dismiss();
        };
    }

    public void saveCredentialsInEncryptedFormat(Credential credential, String username, String password) {
        EncryptedSharedPref.save(getApplicationContext(), credential.label + "username", username);
        EncryptedSharedPref.save(getApplicationContext(), credential.label + "password", password);

        databaseListener.addCredentialToAdapter(credential);
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