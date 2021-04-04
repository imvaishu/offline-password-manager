package com.example.offlinepasswordmanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CredentialDAO {
    @Query("SELECT * FROM credential")
    List<Credential> getAll();

    @Insert
    void insertAll(Credential... credentials);

    @Delete
    void delete(Credential credential);
}
