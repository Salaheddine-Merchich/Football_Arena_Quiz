package com.example.quizapp_merchich;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "QUIZ_DEBUG";
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
        
        btnResetPermission.setOnClickListener(v -> openAppSettings());

        Log.d(TAG, "MainActivity initialized. Checking for existing session...");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "User already logged in: " + currentUser.getEmail());
            syncUserAndStart(currentUser);
        }
    }

    private void checkAndRequestLocationPermission() {
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
            runOnUiThread(() -> {
                Log.d(TAG, "Detected Continent: " + continent);
                Toast.makeText(this, "Arena: " + continent, Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
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

        Log.d(TAG, "Attempting Firebase Login for: " + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "Firebase Login Successful");
                        syncUserAndStart(user);
                    } else {
                        Log.e(TAG, "Firebase Login Failed", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void syncUserAndStart(FirebaseUser user) {
        if (user == null) return;

        UserSyncRequest request = new UserSyncRequest(user.getUid(), user.getEmail());
        String endpoint = "POST /api/users/sync";
        Log.d("QUIZ_API", "FORCING USER SYNC: " + user.getEmail());
        ApiLogger.logRequest(endpoint, "uid=" + user.getUid());

        RetrofitClient.getApiService().syncUser(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiLogger.logResponse(endpoint, response.code(), response.body().getMessage());
                } else {
                    ApiLogger.logError(endpoint, "Sync Response Error: " + response.code(), null);
                }
                startHome();
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                ApiLogger.logError(endpoint, "Sync failed: " + t.getMessage(), t);
                startHome(); // Fallback
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
