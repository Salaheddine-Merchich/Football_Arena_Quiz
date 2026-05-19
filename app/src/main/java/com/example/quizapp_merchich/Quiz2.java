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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Quiz2 extends AppCompatActivity {
    private RadioGroup rgOptions;
    private RadioButton rbA, rbB, rbC, rbD;
    private Button bNext;
    private TextView tvQuestion, tvContinent, tvCurrentScore;
    private ImageView ivQuestion;
    private FloatingActionButton fabMic;

    private int score;
    private String detectedContinent;
    private String RepCorrect = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        // Bind UI with new IDs
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
        fabMic = findViewById(R.id.fabMic);

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
                Intent nextIntent = new Intent(Quiz2.this, Quiz3.class);
                nextIntent.putExtra("score", score);
                nextIntent.putExtra("continent", detectedContinent);
                startActivity(nextIntent);
                finish();
            }
        });
    }

    private void setupQuizContent() {
        ivQuestion.setImageResource(R.drawable.quiz_banner);
        // Contenu dynamique basé sur le continent
        if (detectedContinent.equals("Africa")) {
            tvQuestion.setText("Which African stadium is known as 'Soccer City'?");
            rbA.setText("FNB Stadium"); rbB.setText("Stade Mohammed V"); 
            rbC.setText("Cairo Stadium"); rbD.setText("Stade Léopold Sédar Senghor");
            RepCorrect = "FNB Stadium";
        } else {
            tvQuestion.setText("Who won the UEFA Euro 2024?");
            rbA.setText("Spain"); rbB.setText("England"); 
            rbC.setText("France"); rbD.setText("Germany");
            RepCorrect = "Spain";
        }
    }
}
