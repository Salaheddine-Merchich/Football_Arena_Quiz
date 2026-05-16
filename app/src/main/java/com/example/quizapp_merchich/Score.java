package com.example.quizapp_merchich;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Locale;

public class Score extends AppCompatActivity {
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
        if (continent == null) continent = "Global";

        // Display results
        tvContinentResult.setText(String.format("Region: %s", continent));
        tvScore.setText(String.format(Locale.getDefault(), "%d / %d", score, total));
        
        // Set dynamic message based on score
        setResultMessage(score, total);

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
