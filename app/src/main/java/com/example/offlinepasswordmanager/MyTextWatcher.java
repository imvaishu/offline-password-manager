package com.example.offlinepasswordmanager;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.List;

public class MyTextWatcher {
    private EditText label;
    private List<Credential> credentials;

    public MyTextWatcher(EditText label, List<Credential> credentials) {
        this.label = label;
        this.credentials = credentials;
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
                boolean isAlreadyPresent = isLabelAlreadyPresentInDB(labelAsString);
                if (isAlreadyPresent) {
                    label.requestFocus();
                    label.setError("LABEL SHOULD BE UNIQUE");
                }
            }
        };
    }
    private boolean isLabelAlreadyPresentInDB(String label) {
        return credentials.stream().anyMatch(e -> label.equals(e.label));
    }
}
