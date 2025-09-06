package com.example.assignment1c030RajKothari;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import java.util.ArrayList;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class MainActivity extends AppCompatActivity {

    private EditText promptEditText;
    private ProgressBar progressBar;
    private MessageAdapter adapter;
    private RecyclerView recyclerView;

    private ImageButton micButton;
    private ImageButton sendButton;

    private ImageView lightModeButton;
    private ImageView darkModeButton;

    private ImageView clearChatButton;

    private SharedPreferences prefs;
    private boolean darkMode;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> sttLauncher;

    private ChatRepository chatRepo; // Room repository

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dark mode
        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        darkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        // Delete Chat
        clearChatButton = findViewById(R.id.clear);

        clearChatButton.setOnClickListener(v -> {
            chatRepo.clearAll();          // wipe database
            adapter.clearMessages();      // wipe RecyclerView
            Toast.makeText(this, "Chat cleared", Toast.LENGTH_SHORT).show();
        });

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MessageAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // UI elements
        promptEditText = findViewById(R.id.promptEditText);
        sendButton = findViewById(R.id.sendButton);
        micButton = findViewById(R.id.micButton);
        progressBar = findViewById(R.id.progressBar);

        // Theme toggle buttons
        lightModeButton = findViewById(R.id.lightMode);
        darkModeButton = findViewById(R.id.darkMode);
        updateThemeButtons();

        // Light mode
        lightModeButton.setOnClickListener(v -> {
            prefs.edit().putBoolean("dark_mode", false).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            updateThemeButtons();
        });

        // Dark mode
        darkModeButton.setOnClickListener(v -> {
            prefs.edit().putBoolean("dark_mode", true).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            updateThemeButtons();
        });

        // Room repository
        chatRepo = new ChatRepository(this);

        // Load past chats
        progressBar.setVisibility(VISIBLE);
        chatRepo.getAllMessages(history -> {
            for (ChatEntity e : history) {
                Message m = new Message(e.id, e.isUser, e.text, e.timestamp);
                adapter.addMessage(m);
            }
            recyclerView.scrollToPosition(Math.max(0, adapter.getItemCount() - 1));
            progressBar.setVisibility(GONE);
        });

        // Gemini Model
        GenerativeModel generativeModel = new GenerativeModel(
                "gemini-2.0-flash",
                BuildConfig.API_KEY
        );

        // Register permission request launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        startSpeechRecognizer();
                    } else {
                        Toast.makeText(this, "Microphone permission required for voice input", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Register speech-to-text launcher
        sttLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && !matches.isEmpty()) {
                            String spoken = matches.get(0);
                            promptEditText.setText(spoken);
                            // OPTIONAL: auto-send after recognition
                            // sendButton.performClick();
                        }
                    }
                }
        );

        // Mic button
        micButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                startSpeechRecognizer();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
        });

        sendButton.setOnClickListener(v -> {
            String text = promptEditText.getText().toString().trim();
            if (text.isEmpty()) return;

            // Show user message
            Message userMsg = new Message(System.currentTimeMillis(), true, text, System.currentTimeMillis());
            adapter.addMessage(userMsg);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            chatRepo.saveMessage(new ChatEntity(true, text, System.currentTimeMillis())); // persist
            promptEditText.setText("");
            progressBar.setVisibility(VISIBLE);

            // Gemini API call
            generativeModel.generateContent(text, new Continuation<>() {
                @NonNull
                @Override
                public CoroutineContext getContext() {
                    return EmptyCoroutineContext.INSTANCE;
                }

                @Override
                public void resumeWith(@NonNull Object o) {
                    GenerateContentResponse response = (GenerateContentResponse) o;
                    String responseString = response.getText();
                    if (responseString == null) responseString = "No response";

                    Log.d("Response", responseString);

                    String finalResponse = responseString;
                    runOnUiThread(() -> {
                        progressBar.setVisibility(GONE);

                        // Show bot reply
                        Message botMsg = new Message(System.currentTimeMillis(), false,
                                TextFormatter.getBoldSpannableText(finalResponse).toString(),
                                System.currentTimeMillis());
                        adapter.addMessage(botMsg);
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        chatRepo.saveMessage(new ChatEntity(false, finalResponse, System.currentTimeMillis())); // persist
                    });
                }
            });
        });
    }

    private void startSpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        try {
            sttLauncher.launch(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Speech recognition not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateThemeButtons() {
        darkMode = prefs.getBoolean("dark_mode", false);
        if (darkMode) {
            darkModeButton.setVisibility(GONE);
            lightModeButton.setVisibility(VISIBLE);
        } else {
            lightModeButton.setVisibility(GONE);
            darkModeButton.setVisibility(VISIBLE);
        }
    }
}
