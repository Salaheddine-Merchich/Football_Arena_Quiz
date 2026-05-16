package com.example.quizapp_merchich;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz2 extends AppCompatActivity {
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
        setContentView(R.layout.activity_quiz2);

        rg = findViewById(R.id.rg);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        bNext = findViewById(R.id.bNext);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvContinent = findViewById(R.id.tvContinent);
        ivQuestion = findViewById(R.id.ivQuestion);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        detectedContinent = intent.getStringExtra("continent");
        if (detectedContinent == null) detectedContinent = "Global";

        tvContinent.setText("📍 Detected Region: " + detectedContinent);
        setupQuizContent(detectedContinent);

        bNext.setOnClickListener(v -> {
            if (rg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please select an answer!", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedRb = findViewById(rg.getCheckedRadioButtonId());
                if (selectedRb.getText().toString().equals(RepCorrect)) {
                    score += 1;
                }
                // Continue to next question
                Intent nextIntent = new Intent(Quiz2.this, Quiz3.class);
                nextIntent.putExtra("score", score);
                nextIntent.putExtra("continent", detectedContinent);
                startActivity(nextIntent);
                finish();
            }
        });
    }

    private void setupQuizContent(String continent) {
        // Intégration de la bannière principale du quiz
        ivQuestion.setImageResource(R.drawable.quiz_banner);

        String qText = "";
        String opt1 = "";
        String opt2 = "";

        switch (continent) {
            case "Africa":
                qText = "Which African player won the Ballon d'Or in 1995?";
                opt1 = "George Weah";
                opt2 = "Samuel Eto'o";
                RepCorrect = "George Weah";
                break;
            case "Europe":
                qText = "Which city hosted the 2024 UEFA Champions League final?";
                opt1 = "London (Wembley)";
                opt2 = "Paris (Stade de France)";
                RepCorrect = "London (Wembley)";
                break;
            case "South America":
                qText = "Which club has won the most Copa Libertadores titles?";
                opt1 = "Independiente";
                opt2 = "Boca Juniors";
                RepCorrect = "Independiente";
                break;
            case "Asia":
                qText = "Which Asian team reached the semi-finals of the 2002 World Cup?";
                opt1 = "South Korea";
                opt2 = "Japan";
                RepCorrect = "South Korea";
                break;
            default:
                qText = "Who is the all-time top scorer in FIFA World Cup history?";
                opt1 = "Miroslav Klose";
                opt2 = "Pelé";
                RepCorrect = "Miroslav Klose";
                break;
        }

        tvQuestion.setText(qText);
        
        // Randomize options
        List<String> options = new ArrayList<>();
        options.add(opt1);
        options.add(opt2);
        Collections.shuffle(options);
        
        rb1.setText(options.get(0));
        rb2.setText(options.get(1));
    }
}
