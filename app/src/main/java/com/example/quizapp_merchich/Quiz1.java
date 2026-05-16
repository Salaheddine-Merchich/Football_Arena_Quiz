package com.example.quizapp_merchich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Quiz1 extends AppCompatActivity {
    private static final String TAG = "QUIZ_DEBUG";
    private static final int LOCATION_PERMISSION_CODE = 1001;
    private static final int MIC_PERMISSION_CODE = 1002;
    private static final String PREFS_NAME = "quiz_prefs";
    private static final String KEY_LOCATION_REQUESTED = "location_requested";

    // UI Elements
    private RadioGroup rgOptions;
    private RadioButton rbA, rbB, rbC, rbD;
    private Button bNext;
    private FloatingActionButton fabMic;
    private TextView tvQuestion, tvContinent, tvProgress, tvTimer, tvCurrentScore;
    private ImageView ivQuestion;
    private LinearProgressIndicator quizProgressBar;

    // Quiz State
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String detectedContinent = "Global";
    private boolean isAnswered = false;
    
    // Timer
    private CountDownTimer countDownTimer;
    private static final long TIMER_DURATION = 15000;

    // Voice Interaction
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private boolean isListening = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

        // Bind UI
        tvContinent = findViewById(R.id.tvContinent);
        tvProgress = findViewById(R.id.tvProgress);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvTimer = findViewById(R.id.tvTimer);
        tvCurrentScore = findViewById(R.id.tvCurrentScore);
        ivQuestion = findViewById(R.id.ivQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        rbA = findViewById(R.id.rbOptionA);
        rbB = findViewById(R.id.rbOptionB);
        rbC = findViewById(R.id.rbOptionC);
        rbD = findViewById(R.id.rbOptionD);
        bNext = findViewById(R.id.bNext);
        fabMic = findViewById(R.id.fabMic);
        quizProgressBar = findViewById(R.id.quizProgressBar);

        // Initial UI state
        bNext.setEnabled(false);
        bNext.setText(R.string.preparing_pitch);
        
        initializeSpeechRecognizer();
        handleLocationPermissionFlow();

        bNext.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if (!isAnswered) checkAnswer();
            else goToNextQuestion();
        });

        fabMic.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            toggleMic();
        });
        
        setupOptionInteractions();
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Toast.makeText(Quiz1.this, R.string.mic_listening, Toast.LENGTH_SHORT).show();
                    isListening = true;
                    Animation pulse = AnimationUtils.loadAnimation(Quiz1.this, R.anim.pulse);
                    fabMic.startAnimation(pulse);
                }
                @Override
                public void onEndOfSpeech() {
                    isListening = false;
                    fabMic.clearAnimation();
                }
                @Override
                public void onError(int error) {
                    isListening = false;
                    fabMic.clearAnimation();
                    if (error == SpeechRecognizer.ERROR_NO_MATCH) {
                        Toast.makeText(Quiz1.this, R.string.mic_error_not_detected, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) processVoiceInput(matches.get(0));
                }
                @Override public void onBeginningOfSpeech() {}
                @Override public void onRmsChanged(float rmsdB) {}
                @Override public void onBufferReceived(byte[] buffer) {}
                @Override public void onPartialResults(Bundle partialResults) {}
                @Override public void onEvent(int eventType, Bundle params) {}
            });
        }
    }

    private void toggleMic() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MIC_PERMISSION_CODE);
            return;
        }
        if (isAnswered) return;
        if (!isListening) speechRecognizer.startListening(speechRecognizerIntent);
        else speechRecognizer.stopListening();
    }

    private void processVoiceInput(String voiceText) {
        String input = voiceText.toLowerCase().trim();
        RadioButton[] options = {rbA, rbB, rbC, rbD};
        for (RadioButton rb : options) {
            String text = rb.getText().toString().toLowerCase().trim();
            if (input.contains(text) || text.contains(input)) {
                rb.setChecked(true);
                checkAnswer();
                return;
            }
        }
        Toast.makeText(this, "Heard: \"" + voiceText + "\". Try again!", Toast.LENGTH_SHORT).show();
    }

    private void handleLocationPermissionFlow() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationDetection();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationaleDialog();
            } else {
                requestLocationPermission();
            }
        }
    }

    private void showRationaleDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.perm_explanation_title)
                .setMessage(R.string.perm_explanation_desc)
                .setPositiveButton("Allow", (dialog, which) -> requestLocationPermission())
                .setNegativeButton("Maybe Later", (dialog, which) -> activateGlobalMode(getString(R.string.loc_disabled_global)))
                .setCancelable(false)
                .show();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) startLocationDetection();
            else activateGlobalMode(getString(R.string.loc_disabled_global));
        } else if (requestCode == MIC_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) toggleMic();
            else Toast.makeText(this, R.string.mic_error_permission, Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocationDetection() {
        tvContinent.setText(R.string.detecting_stadium);
        LocationHelper.detectLocation(this, (locationInfo, continent) -> {
            runOnUiThread(() -> {
                detectedContinent = (continent != null) ? continent : "Global";
                tvContinent.setText(String.format("📍 %s", locationInfo != null ? locationInfo : "Global Mode"));
                loadMatchContent();
            });
        });
    }

    private void activateGlobalMode(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        detectedContinent = "Global";
        tvContinent.setText(R.string.global_mode);
        loadMatchContent();
    }

    private void loadMatchContent() {
        questionList = QuestionBank.getQuestionsByContinent(detectedContinent);
        Collections.shuffle(questionList);
        bNext.setEnabled(true);
        bNext.setText(R.string.start_match);
        displayQuestion();
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            isAnswered = false;
            Question q = questionList.get(currentQuestionIndex);
            
            quizProgressBar.setProgress((int) (((float) (currentQuestionIndex + 1) / questionList.size()) * 100), true);
            tvProgress.setText(String.format(Locale.getDefault(), getString(R.string.match_progress), currentQuestionIndex + 1, questionList.size()));
            tvCurrentScore.setText(String.format(Locale.getDefault(), "Score: %d", score));

            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(400);
            tvQuestion.startAnimation(fadeIn);
            ivQuestion.startAnimation(fadeIn);
            
            tvQuestion.setText(q.getQuestionText());
            rbA.setText(q.getOptionA());
            rbB.setText(q.getOptionB());
            rbC.setText(q.getOptionC());
            rbD.setText(q.getOptionD());

            resetOptionStyles();
            ivQuestion.setImageResource(q.getImageResource());
            rgOptions.clearCheck();
            bNext.setText(R.string.submit_action);
            fabMic.show();
            startTimer();
        }
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millis) {
                tvTimer.setText(String.format(Locale.getDefault(), "%ds", millis / 1000));
                tvTimer.setTextColor(ContextCompat.getColor(Quiz1.this, millis < 5000 ? R.color.error_red : R.color.football_gold));
            }
            @Override
            public void onFinish() { checkAnswer(); }
        }.start();
    }

    private void setupOptionInteractions() {
        View.OnClickListener radioClickListener = view -> view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK);
        rbA.setOnClickListener(radioClickListener);
        rbB.setOnClickListener(radioClickListener);
        rbC.setOnClickListener(radioClickListener);
        rbD.setOnClickListener(radioClickListener);
    }

    private void resetOptionStyles() {
        rbA.setBackgroundResource(R.drawable.bg_choice_selector);
        rbB.setBackgroundResource(R.drawable.bg_choice_selector);
        rbC.setBackgroundResource(R.drawable.bg_choice_selector);
        rbD.setBackgroundResource(R.drawable.bg_choice_selector);
        rbA.setEnabled(true); rbB.setEnabled(true); rbC.setEnabled(true); rbD.setEnabled(true);
    }

    private void checkAnswer() {
        if (countDownTimer != null) countDownTimer.cancel();
        int checkedId = rgOptions.getCheckedRadioButtonId();
        if (checkedId == -1 && !isAnswered) {
            Toast.makeText(this, R.string.select_strategy, Toast.LENGTH_SHORT).show();
            return;
        }

        isAnswered = true;
        fabMic.hide();
        if (isListening) speechRecognizer.stopListening();

        String selected = "";
        RadioButton selectedRb = null;
        if (checkedId != -1) {
            selectedRb = findViewById(checkedId);
            selected = selectedRb.getText().toString();
        }
        
        String correct = questionList.get(currentQuestionIndex).getCorrectAnswer();

        if (selected.equals(correct)) {
            score++;
            if (selectedRb != null) selectedRb.setBackgroundResource(R.drawable.bg_choice_correct);
        } else {
            if (selectedRb != null) {
                selectedRb.setBackgroundResource(R.drawable.bg_choice_wrong);
                selectedRb.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            }
            highlightCorrectAnswer(correct);
        }

        rbA.setEnabled(false); rbB.setEnabled(false); rbC.setEnabled(false); rbD.setEnabled(false);
        bNext.setText(currentQuestionIndex == questionList.size() - 1 ? R.string.finish_match : R.string.next_play);
    }

    private void highlightCorrectAnswer(String correct) {
        if (rbA.getText().toString().equals(correct)) rbA.setBackgroundResource(R.drawable.bg_choice_correct);
        else if (rbB.getText().toString().equals(correct)) rbB.setBackgroundResource(R.drawable.bg_choice_correct);
        else if (rbC.getText().toString().equals(correct)) rbC.setBackgroundResource(R.drawable.bg_choice_correct);
        else if (rbD.getText().toString().equals(correct)) rbD.setBackgroundResource(R.drawable.bg_choice_correct);
    }

    private void goToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) displayQuestion();
        else {
            Intent intent = new Intent(this, Score.class);
            intent.putExtra("score", score);
            intent.putExtra("total", questionList.size());
            intent.putExtra("continent", detectedContinent);
            startActivity(intent);
            finish();
        }
    }

    private void showSettingsDialog(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("📍 Permission Required")
                .setMessage(message)
                .setPositiveButton("Open Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
        if (speechRecognizer != null) speechRecognizer.destroy();
    }
}
