package com.example.quizapp_merchich;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    EditText etLogin, etPassword;
    Button bLogin, btnResetPermission;
    TextView tvRegister;
    
    private FirebaseAuth mAuth;
    private String detectedContinent = "Global";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // UI Binding
        etLogin = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        bLogin = findViewById(R.id.bLogin);
        tvRegister = findViewById(R.id.tvRegister);
        btnResetPermission = findViewById(R.id.btnResetPermission);

        // 1. Check & Request REAL Android Permission on Startup
        checkAndRequestLocationPermission();

        bLogin.setOnClickListener(v -> loginUser());
        tvRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Register.class)));
        
        // 2. Demo Mode Button: Redirect to settings so you can reset and show the popup again
        btnResetPermission.setOnClickListener(v -> openAppSettings());
    }

    private void checkAndRequestLocationPermission() {
        // Requesting both FINE and COARSE to trigger the modern Android selection UI (Precise vs Approximate)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            detectUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                detectUserLocation();
                Toast.makeText(this, "Location Access Granted", Toast.LENGTH_SHORT).show();
            } else {
                detectedContinent = "Global";
                Toast.makeText(this, "Running in Global Mode", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void detectUserLocation() {
        LocationHelper.detectLocation(this, (locationInfo, continent) -> {
            detectedContinent = continent;
            runOnUiThread(() -> Toast.makeText(this, "Arena: " + continent, Toast.LENGTH_SHORT).show());
        });
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        Toast.makeText(this, "Reset location permission in Settings to see the popup again", Toast.LENGTH_LONG).show();
    }

    private void loginUser() {
        String email = etLogin.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etLogin.setError("Valid email required");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Min 6 characters");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startHome();
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("continent", detectedContinent);
        startActivity(intent);
        finish();
    }
}
