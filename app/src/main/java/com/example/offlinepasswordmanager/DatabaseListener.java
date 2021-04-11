package com.example.offlinepasswordmanager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseListener {
    private final AppDatabase db;
    private Context context;
    private final CredentialDAO credentialDao;
    private static DatabaseListener databaseListenerInstance = null;

    private DatabaseListener(Context context) {
        this.db = AppDatabase.getInstance(context);
        this.context = context;
        this.credentialDao = db.getCredentialDAO();
    }

    public static DatabaseListener getInstance(Context context) {
        if (databaseListenerInstance == null) {
            databaseListenerInstance = new DatabaseListener(context);
        }
        return databaseListenerInstance;
    }

    public List<Credential> getCredentials() {
        return credentialDao.getAll();
    }

    public void insertToDb(Credential credential) {
        credentialDao.insertAll(credential);
    }

    public void deleteFromDb(Credential credential) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            credentialDao.delete(credential);
        });
    }


}
