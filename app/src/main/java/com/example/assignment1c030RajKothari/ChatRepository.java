package com.example.assignment1c030RajKothari;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executors;

public class ChatRepository {
    private final ChatDao dao;

    public interface Callback<T> {
        void onComplete(T result);
    }

    public ChatRepository(Context context) {
        dao = ChatDatabase.getInstance(context).chatDao();
    }

    public void saveMessage(ChatEntity entity) {
        Executors.newSingleThreadExecutor().execute(() -> dao.insert(entity));
    }

    public void getAllMessages(Callback<List<ChatEntity>> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ChatEntity> list = dao.getAll();
            new android.os.Handler(android.os.Looper.getMainLooper())
                    .post(() -> callback.onComplete(list));
        });
    }

    public void clearAll() {
        Executors.newSingleThreadExecutor().execute(() -> dao.clearAll());
    }
}
