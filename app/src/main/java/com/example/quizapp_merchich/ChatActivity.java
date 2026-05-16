package com.example.quizapp_merchich;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

    private static final String TAG = "ChatActivity";
    private static final String BASE_URL = "http://192.168.11.164:11434";

    private RecyclerView rvChat;
    private ChatAdapter adapter;
    private List<ChatMessage> messages = new ArrayList<>();
    private EditText etMessage;
    private FloatingActionButton btnSend;
    private ProgressBar progressBar;
    private OllamaApi ollamaApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        progressBar = findViewById(R.id.progressBar);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new ChatAdapter(messages);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        initRetrofit();

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
            }
        });
    }

    private void initRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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

        OllamaRequest request = new OllamaRequest("phi3", text, false);
        ollamaApi.generate(request).enqueue(new Callback<OllamaResponse>() {
            @Override
            public void onResponse(Call<OllamaResponse> call, Response<OllamaResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnSend.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    String aiResponse = response.body().getResponse();
                    messages.add(new ChatMessage(aiResponse, ChatMessage.TYPE_AI));
                    adapter.notifyItemInserted(messages.size() - 1);
                    rvChat.scrollToPosition(messages.size() - 1);
                } else {
                    handleError();
                }
            }

            @Override
            public void onFailure(Call<OllamaResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSend.setEnabled(true);
                Log.e(TAG, "onFailure: ", t);
                handleError();
            }
        });
    }

    private void handleError() {
        messages.add(new ChatMessage("AI assistant unavailable", ChatMessage.TYPE_AI));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
        Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
    }
}
