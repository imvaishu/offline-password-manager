package com.example.offlinepasswordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

class CredentialAdapter extends ArrayAdapter<Credential> {

    @NonNull
    private final Context context;
    private final View credentialPopupView;
    private final View deletePopupView;

    public CredentialAdapter(@NonNull Context context, @NonNull List<Credential> items, View credentialPopupView, View deletePopupView) {
        super(context, R.layout.activity_listview, R.id.label, items);
        this.context = context;
        this.credentialPopupView = credentialPopupView;
        this.deletePopupView = deletePopupView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Credential credential = getItem(position);
        setView(view, formatLabel(position), R.id.label);

        View textView = view.findViewById(R.id.label);
        textView.setOnClickListener(getClickedCredential(view, credential));

        View deleteBtn = view.findViewById(R.id.delete);
        deleteBtn.setOnClickListener(deleteClickedCredential(view, credential));

        return view;
    }

    private View.OnClickListener deleteClickedCredential(View view, Credential credential) {
        return v -> {
            TextView deleteConfirmationMsg = deletePopupView.findViewById(R.id.delete_confirmation_msg);
            deleteConfirmationMsg.setText("Are you sure, You want to delete " + credential.label + "'s credential ?");

            final PopupWindow popupWindow = getPopupWindow(view, deletePopupView);
            deletePopupView.findViewById(R.id.cancel).setOnClickListener(v1 -> popupWindow.dismiss());
            deletePopupView.findViewById(R.id.delete).setOnClickListener(deleteCredentialOnConfirm(credential, popupWindow));
        };
    }

    private View.OnClickListener deleteCredentialOnConfirm(Credential credential, PopupWindow popupWindow) {
        return v2 -> {
            DatabaseListener databaseListener = DatabaseListener.getInstance(context);
            databaseListener.deleteFromDb(credential);

            EncryptedSharedPref.delete(context, credential.label + "username");
            EncryptedSharedPref.delete(context, credential.label + "password");

            remove(credential);
            notifyDataSetChanged();
            Toast.makeText(context, credential.label.toUpperCase() + " GOT DELETED!", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        };
    }

    private View.OnClickListener getClickedCredential(View view, Credential credential) {
        return v -> {

            final PopupWindow popupWindow = getPopupWindow(view, credentialPopupView);

            String username = EncryptedSharedPref.get(context, credential.label + "username");
            String password = EncryptedSharedPref.get(context, credential.label + "password");

            setView(credentialPopupView, "Label : " + credential.label, R.id.credential_label);
            setView(credentialPopupView, "Username : " + username, R.id.credential_username);
            String hiddenPassword = password.replaceAll(".", "*");;
            setView(credentialPopupView, "Password : " + hiddenPassword, R.id.credential_password);

            credentialPopupView.findViewById(R.id.reveal).setOnClickListener(revealPassword(password, hiddenPassword));

            FloatingActionButton back = credentialPopupView.findViewById(R.id.back);
            back.setOnClickListener(copyPassword(popupWindow, password));
        };
    }

    private View.OnClickListener copyPassword(PopupWindow popupWindow, String password) {
        return v1 -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("password", password);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "PASSWORD HAS BEEN COPIED!", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        };
    }

    private View.OnClickListener revealPassword(String password, String hiddenPassword) {
        return v12 -> {
            TextView textView1  = credentialPopupView.findViewById(R.id.credential_password);
            boolean isRevealed = textView1.getText().toString().equals("Password : " + hiddenPassword);
            if(isRevealed){
                 setView(credentialPopupView, "Password : " + password, R.id.credential_password);
                return;
            }
            String hiddenPassword1 = password.replaceAll(".", "*");;
            setView(credentialPopupView, "Password : " + hiddenPassword1, R.id.credential_password);
        };
    }

    private String formatLabel(int position) {
        return new StringBuilder().append(position + 1).append(". ").append(getItem(position).label).toString();
    }

    private void setView(View popupView, String labelName, int id) {
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
