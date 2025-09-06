package com.example.assignment1c030RajKothari;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ChatEntity.class}, version = 1)
public abstract class ChatDatabase extends RoomDatabase {

    public abstract ChatDao chatDao();

    private static volatile ChatDatabase INSTANCE;

    public static ChatDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ChatDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ChatDatabase.class,
                            "chat_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
