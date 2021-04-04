package com.example.offlinepasswordmanager;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Credential {

    public Credential(@NonNull String label) {
        this.label = label;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "label")
    public String label;
}
