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

public class Quiz3 extends AppCompatActivity {
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
        setContentView(R.layout.activity_quiz3);

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
                Intent nextIntent = new Intent(Quiz3.this, Quiz4.class);
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
                qText = "In which year did the Morocco national team win their first and only AFCON title?";
                opt1 = "1976";
                opt2 = "2004";
                RepCorrect = "1976";
                break;
            case "Europe":
                qText = "Which legendary French player scored twice in the 1998 World Cup Final?";
                opt1 = "Zinedine Zidane";
                opt2 = "Thierry Henry";
                RepCorrect = "Zinedine Zidane";
                break;
            case "South America":
                qText = "Which club is famous for its 'La Bombonera' stadium in Buenos Aires?";
                opt1 = "Boca Juniors";
                opt2 = "River Plate";
                RepCorrect = "Boca Juniors";
                break;
            case "Asia":
                qText = "Which Asian country co-hosted the 2002 FIFA World Cup along with South Korea?";
                opt1 = "Japan";
                opt2 = "China";
                RepCorrect = "Japan";
                break;
            default:
                qText = "Which nation hosted the first-ever FIFA World Cup in 1930?";
                opt1 = "Uruguay";
                opt2 = "Italy";
                RepCorrect = "Uruguay";
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
