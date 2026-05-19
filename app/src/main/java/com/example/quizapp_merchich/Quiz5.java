package com.example.quizapp_merchich;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz5 extends AppCompatActivity {
    private RadioGroup rgOptions;
    private RadioButton rbA, rbB, rbC, rbD;
    private Button bNext;
    private TextView tvQuestion, tvContinent, tvCurrentScore;
    private ImageView ivQuestion;

    private int score;
    private String detectedContinent;
    private String RepCorrect = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz5);

        // UI Initialization with modern IDs
        rgOptions = findViewById(R.id.rgOptions);
        rbA = findViewById(R.id.rbOptionA);
        rbB = findViewById(R.id.rbOptionB);
        rbC = findViewById(R.id.rbOptionC);
        rbD = findViewById(R.id.rbOptionD);
        bNext = findViewById(R.id.bNext);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvContinent = findViewById(R.id.tvContinent);
        tvCurrentScore = findViewById(R.id.tvCurrentScore);
        ivQuestion = findViewById(R.id.ivQuestion);

        // Retrieve data
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        detectedContinent = intent.getStringExtra("continent");
        if (detectedContinent == null) detectedContinent = "Global";

        tvContinent.setText("📍 " + detectedContinent);
        tvCurrentScore.setText("Score: " + score);
        
        setupQuizContent();

        bNext.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if (rgOptions.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Final move! Select an answer!", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedRb = findViewById(rgOptions.getCheckedRadioButtonId());
                if (selectedRb.getText().toString().equals(RepCorrect)) {
                    score++;
                }
                
                // Navigate to final Score activity
                Intent nextIntent = new Intent(Quiz5.this, Score.class);
                nextIntent.putExtra("score", score);
                nextIntent.putExtra("total", 5); 
                nextIntent.putExtra("continent", detectedContinent);
                startActivity(nextIntent);
                finish();
            }
        });
    }

    private void setupQuizContent() {
        ivQuestion.setImageResource(R.drawable.quiz_banner);
        
        switch (detectedContinent) {
            case "Africa":
                tvQuestion.setText("Which Moroccan club is nicknamed 'The Green Eagles'?");
                rbA.setText("Raja CA"); rbB.setText("Wydad AC"); 
                rbC.setText("AS FAR"); rbD.setText("Maghreb Fès");
                RepCorrect = "Raja CA";
                break;
            case "Europe":
                tvQuestion.setText("Which club plays at the iconic Camp Nou stadium?");
                rbA.setText("FC Barcelona"); rbB.setText("Real Madrid"); 
                rbC.setText("Atletico Madrid"); rbD.setText("Espanyol");
                RepCorrect = "FC Barcelona";
                break;
            case "South America":
                tvQuestion.setText("Who captained Argentina to the 2022 World Cup title?");
                rbA.setText("Lionel Messi"); rbB.setText("Angel Di Maria"); 
                rbC.setText("Rodrigo De Paul"); rbD.setText("Julian Alvarez");
                RepCorrect = "Lionel Messi";
                break;
            default:
                tvQuestion.setText("What is the official duration of a standard football match?");
                rbA.setText("90 minutes"); rbB.setText("80 minutes"); 
                rbC.setText("100 minutes"); rbD.setText("70 minutes");
                RepCorrect = "90 minutes";
                break;
        }
    }
}
