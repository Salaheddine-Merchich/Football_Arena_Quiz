package com.example.quizapp_merchich;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "QUIZ_AI_DEBUG";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    
    // CONFIGURATION RÉSEAU
    private static final String EMULATOR_URL = "http://10.0.2.2:11434/";
    private static final String PHYSICAL_DEVICE_URL = "http://192.168.11.164:11434/";

    private RecyclerView rvChat;
    private ChatAdapter adapter;
    private List<ChatMessage> messages = new ArrayList<>();
    private EditText etMessage;
    private FloatingActionButton btnSend, btnVoice;
    private ProgressBar progressBar;
    private OllamaApi ollamaApi;
    private String activeBaseUrl = "";
    
    private VoiceInputHelper voiceInputHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnVoice = findViewById(R.id.btnVoice);
        progressBar = findViewById(R.id.progressBar);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new ChatAdapter(messages);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        initRetrofit();
        initVoiceInput();

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
            }
        });

        btnVoice.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            } else {
                toggleVoiceInput();
            }
        });
    }

    private void initVoiceInput() {
        voiceInputHelper = new VoiceInputHelper(this, new VoiceInputHelper.VoiceInputListener() {
            @Override
            public void onReadyForSpeech() {
                updateVoiceButtonUI(true);
                etMessage.setHint("Listening...");
            }

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
                updateVoiceButtonUI(false);
                etMessage.setHint("Ask about football...");
            }

            @Override
            public void onError(int error) {
                updateVoiceButtonUI(false);
                etMessage.setHint("Ask about football...");
                Log.e(TAG, "Speech Error: " + error);
            }

            @Override
            public void onResults(String result) {
                etMessage.setText(result);
                etMessage.setSelection(result.length());
            }

            @Override
            public void onPartialResults(String partialResult) {
                etMessage.setText(partialResult);
                etMessage.setSelection(partialResult.length());
            }
        });
    }

    private void toggleVoiceInput() {
        if (voiceInputHelper.isListening()) {
            voiceInputHelper.stopListening();
        } else {
            voiceInputHelper.startListening();
        }
    }

    private void updateVoiceButtonUI(boolean isListening) {
        if (isListening) {
            btnVoice.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_light)));
        } else {
            // Restore original gold color from layout or use specific color
            btnVoice.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.football_gold)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toggleVoiceInput();
            } else {
                Toast.makeText(this, "Microphone permission required for voice input", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initRetrofit() {
        activeBaseUrl = isEmulator() ? EMULATOR_URL : PHYSICAL_DEVICE_URL;
        Log.i(TAG, "Démarrage Retrofit. Cible : " + activeBaseUrl);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(msg -> Log.d(TAG, "OkHttp: " + msg));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(activeBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ollamaApi = retrofit.create(OllamaApi.class);
    }

    private void sendMessage(String text) {
        messages.add(new ChatMessage(text, ChatMessage.TYPE_USER));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
        etMessage.setText("");

        progressBar.setVisibility(View.VISIBLE);
        btnSend.setEnabled(false);

        Log.i(TAG, "Envoi au modèle phi3...");
        
        OllamaRequest request = new OllamaRequest("phi3", text, false);
        ollamaApi.generate(request).enqueue(new Callback<OllamaResponse>() {
            @Override
            public void onResponse(Call<OllamaResponse> call, Response<OllamaResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnSend.setEnabled(true);
                
                if (response.isSuccessful() && response.body() != null) {
                    String aiResponse = response.body().getResponse();
                    Log.i(TAG, "Réponse IA reçue : " + aiResponse);
                    messages.add(new ChatMessage(aiResponse, ChatMessage.TYPE_AI));
                    adapter.notifyItemInserted(messages.size() - 1);
                    rvChat.scrollToPosition(messages.size() - 1);
                } else {
                    Log.e(TAG, "Erreur Serveur : Code " + response.code());
                    handleError("Code " + response.code() + " (Modèle 'phi3' introuvable ?)");
                }
            }

            @Override
            public void onFailure(Call<OllamaResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSend.setEnabled(true);
                
                String errorType = t.getClass().getSimpleName();
                Log.e(TAG, "ÉCHEC CONNEXION : " + errorType + " - " + t.getMessage());

                String diagnostic = errorType;
                if (t instanceof java.net.ConnectException) diagnostic = "Connexion refusée (Vérifiez OLLAMA_HOST=0.0.0.0)";
                if (t instanceof java.net.SocketTimeoutException) diagnostic = "Délai dépassé (PC trop lent)";
                
                handleError(diagnostic);
            }
        });
    }

    private void handleError(String diagnostic) {
        String fullMsg = "AI assistant unavailable\nDiagnostic : " + diagnostic + "\nCible : " + activeBaseUrl;
        messages.add(new ChatMessage(fullMsg, ChatMessage.TYPE_AI));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
        Toast.makeText(this, "Vérifiez les logs (QUIZ_AI_DEBUG)", Toast.LENGTH_LONG).show();
    }

    private boolean isEmulator() {
        return Build.FINGERPRINT.contains("generic")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (voiceInputHelper != null) {
            voiceInputHelper.destroy();
        }
    }
}
