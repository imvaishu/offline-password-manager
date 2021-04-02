package com.example.offlinepasswordmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EncryptedSharedPref {

    public static String getPassword(Context context, String key) throws GeneralSecurityException, IOException {
        SharedPreferences encryptedSharedPreference = getEncryptedSharedPreferences(context);
        return encryptedSharedPreference.getString(key, "");
    }

    public static void savePassword(Context context, String key, String value) throws GeneralSecurityException, IOException {
        SharedPreferences encryptedSharedPreference = getEncryptedSharedPreferences(context);
        SharedPreferences.Editor editor = encryptedSharedPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static SharedPreferences getEncryptedSharedPreferences(Context context) throws GeneralSecurityException, IOException {
        String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        return EncryptedSharedPreferences.create(
                "secret_shared_prefs",
                masterKey,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
    }
}
