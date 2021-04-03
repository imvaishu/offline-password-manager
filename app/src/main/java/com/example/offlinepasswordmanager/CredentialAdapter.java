package com.example.offlinepasswordmanager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class CredentialAdapter extends ArrayAdapter<String> {

    @NonNull
    private final Context context;
    private final ArrayList<String> items;
    private final View popupView;

    public CredentialAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> items, View popupView) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.popupView = popupView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setOnClickListener(v -> {

            final PopupWindow popupWindow = getPopupWindow(view, popupView);
            String labelName = this.items.get(position);
            String username = EncryptedSharedPref.get(context, labelName + "username");
            String password = EncryptedSharedPref.get(context, labelName + "password");

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

}
