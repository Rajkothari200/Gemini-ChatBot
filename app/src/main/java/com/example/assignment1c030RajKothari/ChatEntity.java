package com.example.assignment1c030RajKothari;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class ChatEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public boolean isUser;
    public String text;
    public long timestamp;

    public ChatEntity(boolean isUser, String text, long timestamp) {
        this.isUser = isUser;
        this.text = text;
        this.timestamp = timestamp;
    }
}
