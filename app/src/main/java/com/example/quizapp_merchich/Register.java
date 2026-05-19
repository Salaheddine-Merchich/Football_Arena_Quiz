package com.example.quizapp_merchich;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private static final String TAG = "QUIZ_DEBUG";
    EditText etMail, etPassword, etPassword1;
    Button bRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etPassword1 = findViewById(R.id.etPassword1);
        bRegister = findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = etMail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPassword1.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etMail.setError("Email valide requis");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("6 caractères minimum");
            return;
        }
        if (!password.equals(passwordConfirm)) {
            etPassword1.setError("Les mots de passe ne correspondent pas");
            return;
        }

        Log.d(TAG, "Tentative Firebase pour : " + email);
        Toast.makeText(this, "Vérification avec Firebase...", Toast.LENGTH_SHORT).show();
        
        bRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                syncUserWithBackend(user.getUid(), user.getEmail());
                            } else {
                                bRegister.setEnabled(true);
                                proceedToMain();
                            }
                        } else {
                            bRegister.setEnabled(true);
                            String error = task.getException() != null ? task.getException().getMessage() : "Erreur inconnue";
                            Log.e(TAG, "Erreur Firebase : " + error);
                            Toast.makeText(Register.this, "Erreur : " + error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void syncUserWithBackend(String uid, String email) {
        UserSyncRequest request = new UserSyncRequest(uid, email);
        String endpoint = "POST /api/users/sync";
        ApiLogger.logRequest(endpoint, "uid=" + uid + ", email=" + email);

        RetrofitClient.getApiService().syncUser(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                bRegister.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserResponse> apiResponse = response.body();
                    ApiLogger.logResponse(endpoint, response.code(), apiResponse.getMessage());
                    if (apiResponse.isSuccess()) {
                        Log.d(TAG, "User synced with backend successfully");
                    } else {
                        ApiLogger.logError(endpoint, "Backend sync error: " + apiResponse.getMessage(), null);
                    }
                } else {
                    ApiLogger.logError(endpoint, "HTTP Error: " + response.code(), null);
                }
                proceedToMain();
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                bRegister.setEnabled(true);
                ApiLogger.logError(endpoint, "Network failure: " + t.getMessage(), t);
                proceedToMain();
            }
        });
    }

    private void proceedToMain() {
        Toast.makeText(Register.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Register.this, MainActivity.class));
        finish();
    }
}
