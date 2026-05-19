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

public class Quiz3 extends AppCompatActivity {
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
        setContentView(R.layout.activity_quiz3);

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
                Toast.makeText(this, "Please select an answer!", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedRb = findViewById(rgOptions.getCheckedRadioButtonId());
                if (selectedRb.getText().toString().equals(RepCorrect)) {
                    score++;
                }
                Intent nextIntent = new Intent(Quiz3.this, Quiz4.class);
                nextIntent.putExtra("score", score);
                nextIntent.putExtra("continent", detectedContinent);
                startActivity(nextIntent);
                finish();
            }
        });
    }

    private void setupQuizContent() {
        ivQuestion.setImageResource(R.drawable.quiz_banner);
        if (detectedContinent.equals("Africa")) {
            tvQuestion.setText("In which year did Morocco win their first AFCON title?");
            rbA.setText("1976"); rbB.setText("2004"); 
            rbC.setText("1988"); rbD.setText("1994");
            RepCorrect = "1976";
        } else {
            tvQuestion.setText("Which player scored 91 goals in a single year (2012)?");
            rbA.setText("Lionel Messi"); rbB.setText("Cristiano Ronaldo"); 
            rbC.setText("Robert Lewandowski"); rbD.setText("Zlatan Ibrahimovic");
            RepCorrect = "Lionel Messi";
        }
    }
}
