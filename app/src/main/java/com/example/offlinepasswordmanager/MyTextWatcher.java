package com.example.offlinepasswordmanager;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MyTextWatcher {
    private EditText label;
    private DatabaseListener databaseListener;

    public MyTextWatcher(EditText label,DatabaseListener databaseListener) {
        this.label = label;
        this.databaseListener = databaseListener;
    }

    public TextWatcher invoke() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String labelAsString = label.getText().toString();
                boolean isAlreadyPresent = databaseListener.isLabelAlreadyPresentInDB(labelAsString);
                if (isAlreadyPresent) {
                    label.requestFocus();
                    label.setError("LABEL SHOULD BE UNIQUE");
                }
            }
        };
    }
}
