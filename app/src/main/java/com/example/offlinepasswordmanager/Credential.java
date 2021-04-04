package com.example.offlinepasswordmanager;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Credential {

    public Credential(@NonNull String label, String username, String password) {
        this.label = label;
        this.username = username;
        this.password = password;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "label")
    public String label;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;
}
