package com.example.assignment1c030RajKothari;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatDao {
    @Insert
    void insert(ChatEntity entity);

    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    List<ChatEntity> getAll();

    @Query("DELETE FROM messages")
    void clearAll();
}
