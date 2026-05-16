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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz4 extends AppCompatActivity {
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
        setContentView(R.layout.activity_quiz4);

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
        setupQuizContent(detectedContinent);

        bNext.setOnClickListener(v -> {
            if (rg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please select an answer!", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedRb = findViewById(rg.getCheckedRadioButtonId());
                if (selectedRb.getText().toString().equals(RepCorrect)) {
                    score += 1;
                }
                Intent nextIntent = new Intent(Quiz4.this, Quiz5.class);
                nextIntent.putExtra("score", score);
                nextIntent.putExtra("continent", detectedContinent);
                startActivity(nextIntent);
                finish();
            }
        });
    }

    private void setupQuizContent(String continent) {
        // Intégration de la bannière du quiz
        ivQuestion.setImageResource(R.drawable.quiz_banner);

        String qText = "";
        String opt1 = "";
        String opt2 = "";

        switch (continent) {
            case "Africa":
                qText = "Which Moroccan player is a key right-back for Paris Saint-Germain?";
                opt1 = "Achraf Hakimi";
                opt2 = "Noussair Mazraoui";
                RepCorrect = "Achraf Hakimi";
                break;
            case "Europe":
                qText = "Which club won the Premier League title for four consecutive seasons (2021-2024)?";
                opt1 = "Manchester City";
                opt2 = "Liverpool";
                RepCorrect = "Manchester City";
                break;
            case "South America":
                qText = "Which South American nation hosted and won the first-ever FIFA World Cup in 1930?";
                opt1 = "Uruguay";
                opt2 = "Argentina";
                RepCorrect = "Uruguay";
                break;
            case "Asia":
                qText = "Which Asian player won the Premier League Golden Boot for the 2021-22 season?";
                opt1 = "Son Heung-min";
                opt2 = "Mitoma";
                RepCorrect = "Son Heung-min";
                break;
            default:
                qText = "Who is the all-time top scorer in FIFA World Cup history with 16 goals?";
                opt1 = "Miroslav Klose";
                opt2 = "Ronaldo";
                RepCorrect = "Miroslav Klose";
                break;
        }

        tvQuestion.setText(qText);

        // FIX RANDOMNESS: Shuffle answer choices dynamically
        List<String> options = new ArrayList<>();
        options.add(opt1);
        options.add(opt2);
        Collections.shuffle(options);

        rb1.setText(options.get(0));
        rb2.setText(options.get(1));
    }
}
