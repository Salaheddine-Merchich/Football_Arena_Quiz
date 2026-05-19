package com.example.quizapp_merchich;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Score extends AppCompatActivity {
    private static final String TAG = "SCORE_BACKEND";
    private Button bLogout, bTry, bHome;
    private TextView tvScore, tvContinentResult, tvMessage;
    private int score, total;
    private String continent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // UI Initialization
        tvScore = findViewById(R.id.tvScore);
        tvContinentResult = findViewById(R.id.tvContinentResult);
        tvMessage = findViewById(R.id.tvMessage);
        bLogout = findViewById(R.id.bLogout);
        bTry = findViewById(R.id.bTry);
        bHome = findViewById(R.id.bHome);

        // Retrieve dynamic data
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        total = intent.getIntExtra("total", 0);
        continent = intent.getStringExtra("continent");
        if (continent == null) continent = "Africa"; // Ensure non-null on retrieval

        // Display results
        tvContinentResult.setText(String.format("Region: %s", continent));
        tvScore.setText(String.format(Locale.getDefault(), "%d / %d", score, total));
        
        // Set dynamic message based on score
        setResultMessage(score, total);

        // Submit score to backend
        submitScoreToBackend();

        // REMATCH Button
        bTry.setOnClickListener(v -> {
            Intent retryIntent = new Intent(Score.this, Quiz1.class);
            retryIntent.putExtra("continent", continent);
            startActivity(retryIntent);
            finish();
        });

        // BACK TO ARENA Button
        bHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(Score.this, HomeActivity.class);
            homeIntent.putExtra("continent", continent);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
        });

        // LOGOUT Button
        bLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intentLogin = new Intent(Score.this, MainActivity.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentLogin);
        });
    }

    private void submitScoreToBackend() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        
        // 1. Validation before sending
        if (user == null || user.getUid() == null || user.getUid().isEmpty()) {
            ApiLogger.logError("POST /api/scores", "Validation failed: User ID is null or empty", null);
            return;
        }

        if (total <= 0) {
            ApiLogger.logError("POST /api/scores", "Validation failed: totalQuestions must be > 0", null);
            return;
        }

        if (score < 0) {
            ApiLogger.logError("POST /api/scores", "Validation failed: score must be >= 0", null);
            return;
        }

        // 2. Continent Fix: Backend rejects null. Force a valid value.
        String validatedContinent = continent;
        List<String> allowedContinents = Arrays.asList("Africa", "Europe", "Asia", "South America");
        if (validatedContinent == null || !allowedContinents.contains(validatedContinent)) {
            Log.w(TAG, "Invalid continent detected: " + validatedContinent + ". Falling back to Africa.");
            validatedContinent = "Africa"; // Strict backend validation requires a known continent
        }

        // 3. Prepare Request - Use userId instead of firebaseUid
        QuizSubmitRequest request = new QuizSubmitRequest(
                user.getUid(),
                score,
                total,
                validatedContinent
        );

        String endpoint = "POST /api/scores";
        
        // 4. Detailed logging before sending
        Log.i(TAG, "PRE-SEND VALIDATION SUCCESS");
        Log.i(TAG, "Payload details:");
        Log.i(TAG, "-> userId: " + request.getUserId());
        Log.i(TAG, "-> score: " + request.getScore());
        Log.i(TAG, "-> totalQuestions: " + request.getTotalQuestions());
        Log.i(TAG, "-> continent: " + request.getContinent());
        
        ApiLogger.logRequest(endpoint, "userId=" + request.getUserId() + ", score=" + request.getScore() + ", continent=" + request.getContinent());

        // 5. Execute API Call
        RetrofitClient.getApiService().submitScore(request).enqueue(new Callback<ApiResponse<ScoreResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ScoreResponse>> call, Response<ApiResponse<ScoreResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ScoreResponse> apiResponse = response.body();
                    ApiLogger.logResponse(endpoint, response.code(), apiResponse.getMessage());
                    if (apiResponse.isSuccess()) {
                        Log.d(TAG, "✅ Score submitted successfully: ID " + apiResponse.getData().getId());
                    } else {
                        ApiLogger.logError(endpoint, "Backend error message: " + apiResponse.getMessage(), null);
                    }
                } else {
                    // Log the error body if available for easier debugging of 400 errors
                    String errorMsg = "HTTP Error " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += " - " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    ApiLogger.logError(endpoint, errorMsg, null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ScoreResponse>> call, Throwable t) {
                ApiLogger.logError(endpoint, "Network failure: " + t.getMessage(), t);
                Toast.makeText(Score.this, "Offline: Score not saved to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setResultMessage(int score, int total) {
        if (total == 0) return;
        double percentage = (double) score / total;
        
        if (percentage >= 0.9) {
            tvMessage.setText("🏆 GOAT Status! Absolute Legend.");
        } else if (percentage >= 0.7) {
            tvMessage.setText("⚽ World Class Performance!");
        } else if (percentage >= 0.5) {
            tvMessage.setText("📈 Good Game! Keep Training.");
        } else {
            tvMessage.setText("🧤 Better Luck Next Match!");
        }
    }
}
