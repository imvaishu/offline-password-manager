package com.example.offlinepasswordmanager;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public static String getPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DEMO", 0);
         return sharedPreferences.getString("PASSWORD", "");
    }

    public static void savePassword(Context context,String value){
        SharedPreferences sharedPreferences =  context.getSharedPreferences("DEMO", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PASSWORD", value);
        editor.apply();
    }
}
