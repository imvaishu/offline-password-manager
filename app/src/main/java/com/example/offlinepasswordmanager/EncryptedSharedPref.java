package com.example.offlinepasswordmanager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EncryptedSharedPref {
    private static SharedPreferences encryptedSharedPreferences;

    public static String getPassword(Context context, String key) {
        SharedPreferences encryptedSharedPreference = getEncryptedSharedPreferences(context);
        return encryptedSharedPreference.getString(key, "");
    }

    public static void savePassword(Context context, String key, String value) {
        SharedPreferences encryptedSharedPreference = getEncryptedSharedPreferences(context);
        SharedPreferences.Editor editor = encryptedSharedPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static SharedPreferences getEncryptedSharedPreferences(Context context) {
        try {
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            encryptedSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encryptedSharedPreferences;
    }
}
