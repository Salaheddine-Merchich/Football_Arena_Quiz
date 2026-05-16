package com.example.quizapp_merchich;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;

    private TextView tvWelcome, tvLocationIndicator;
    private Button btnStartQuiz, btnOpenMap, btnOpenChat;
    private ImageView ivLogout, ivLogo;
    private FloatingActionButton fabCamera;
    private String detectedContinent = "Global";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // UI Initialization
        tvWelcome = findViewById(R.id.tvWelcome);
        tvLocationIndicator = findViewById(R.id.tvLocationIndicator);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        btnOpenMap = findViewById(R.id.btnOpenMap);
        btnOpenChat = findViewById(R.id.btnOpenChat);
        ivLogout = findViewById(R.id.ivLogout);
        ivLogo = findViewById(R.id.ivLogo);
        fabCamera = findViewById(R.id.fabCamera);

        // --- PREMIUM ANIMATIONS ---
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_slow);
        ivLogo.startAnimation(rotate);

        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        btnStartQuiz.startAnimation(pulse);

        // Set User Welcome Message
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            if (name == null || name.isEmpty()) {
                name = user.getEmail().split("@")[0];
            }
            tvWelcome.setText("Welcome back, " + name + "!");
        }

        // Get detected continent from Intent
        detectedContinent = getIntent().getStringExtra("continent");
        if (detectedContinent != null && !detectedContinent.equals("Global")) {
            tvLocationIndicator.setText("📍 Playing in " + detectedContinent + " Region");
        } else {
            tvLocationIndicator.setText("🌍 Global Mode Enabled");
        }

        // Button Listeners
        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, Quiz1.class);
            intent.putExtra("continent", detectedContinent);
            startActivity(intent);
        });

        btnOpenMap.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, StadiumMapActivity.class);
            intent.putExtra("continent", detectedContinent);
            startActivity(intent);
        });

        btnOpenChat.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            startActivity(intent);
        });

        ivLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        });

        fabCamera.setOnClickListener(v -> checkCameraPermission());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(this, "No camera application found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Device has no camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ivLogo.clearAnimation(); // Stop rotation before showing static photo
            ivLogo.setImageBitmap(photo);
            Toast.makeText(this, "Avatar updated successfully!", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Capture cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
