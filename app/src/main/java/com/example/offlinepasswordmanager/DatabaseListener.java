package com.example.offlinepasswordmanager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseListener {
    CredentialAdapter adapter;
    private final AppDatabase db;
    private Context context;
    private List<Credential> defaultCredentials;
    private final CredentialDAO credentialDao;
    private List<Credential> credentials;
    private static DatabaseListener databaseListenerInstance = null;

    private DatabaseListener(Context context) {
        this.db = AppDatabase.getInstance(context);
        this.context = context;
        this.credentialDao = db.getCredentialDAO();
        this.defaultCredentials = new ArrayList<>();
    }

    public static DatabaseListener getInstance(Context context) {
        if (databaseListenerInstance == null) {
            databaseListenerInstance = new DatabaseListener(context);
        }
        return databaseListenerInstance;
    }

    public void getCredentials() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            credentials = credentialDao.getAll();
            defaultCredentials.addAll(credentials);
        });
    }

    public void insertToDb(Credential credential) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            credentialDao.insertAll(credential);
        });
    }

    public void deleteFromDb(Credential credential) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            credentialDao.delete(credential);
        });
    }

    public void attachAdapter(ListView listView, View credentialPopupView, View deleteConfirmationPopupView) {
        adapter = new CredentialAdapter(context, defaultCredentials, credentialPopupView, deleteConfirmationPopupView);
        listView.setAdapter(adapter);
    }

    public boolean isLabelAlreadyPresentInDB(String label) {
        return credentials.stream().anyMatch(e -> label.equals(e.label));
    }

    public void addCredentialToAdapter(Credential credential) {
        adapter.add(credential);
        assert adapter != null;
        adapter.notifyDataSetChanged();
    }
}
