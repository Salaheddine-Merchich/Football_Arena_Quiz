package com.example.quizapp_merchich;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz5 extends AppCompatActivity {
    RadioGroup rg;
    RadioButton rb1, rb2;
    Button bNext;
    TextView tvQuestion, tvContinent;
    ImageView ivQuestion;

    int score;
    String detectedContinent;
    String RepCorrect = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz5);

        // UI Initialization
        rg = findViewById(R.id.rg);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        bNext = findViewById(R.id.bNext);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvContinent = findViewById(R.id.tvContinent);
        ivQuestion = findViewById(R.id.ivQuestion);

        // Retrieve data from previous activity
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        detectedContinent = intent.getStringExtra("continent");
        if (detectedContinent == null) detectedContinent = "Global";

        // UI Requirement: Display detected continent
        tvContinent.setText("📍 Region: " + detectedContinent);
        
        // Load content based on continent
        setupQuizContent(detectedContinent);

        bNext.setOnClickListener(v -> {
            if (rg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please select an answer!", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedRb = findViewById(rg.getCheckedRadioButtonId());
                if (selectedRb.getText().toString().equals(RepCorrect)) {
                    score += 1;
                }
                // Navigate to final Score activity
                Intent nextIntent = new Intent(Quiz5.this, Score.class);
                nextIntent.putExtra("score", score);
                nextIntent.putExtra("total", 5); // Total questions in this flow
                nextIntent.putExtra("continent", detectedContinent);
                startActivity(nextIntent);
                finish();
            }
        });
    }

    private void setupQuizContent(String continent) {
        // Intégration de la bannière principale du quiz
        ivQuestion.setImageResource(R.drawable.quiz_banner);

        switch (continent) {
            case "Africa":
                tvQuestion.setText("Which legendary Moroccan club is nicknamed 'The Green Eagles' and plays at Stade Mohammed V?");
                rb1.setText("Raja CA");
                rb2.setText("Wydad AC");
                RepCorrect = "Raja CA";
                break;
            case "Europe":
                tvQuestion.setText("Which club plays its home matches at the iconic Camp Nou stadium?");
                rb1.setText("FC Barcelona");
                rb2.setText("Real Madrid");
                RepCorrect = "FC Barcelona";
                break;
            case "South America":
                tvQuestion.setText("Which player captained Argentina to their 2022 FIFA World Cup victory?");
                rb1.setText("Lionel Messi");
                rb2.setText("Neymar Jr");
                RepCorrect = "Lionel Messi";
                break;
            case "Asia":
                tvQuestion.setText("Which nation has won the most AFC Asian Cup titles (4 titles)?");
                rb1.setText("Japan");
                rb2.setText("South Korea");
                RepCorrect = "Japan";
                break;
            default:
                tvQuestion.setText("What is the official duration of a standard professional football match?");
                rb1.setText("90 minutes");
                rb2.setText("80 minutes");
                RepCorrect = "90 minutes";
                break;
        }
    }
}
