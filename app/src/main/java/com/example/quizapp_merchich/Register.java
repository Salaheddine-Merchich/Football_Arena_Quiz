package com.example.quizapp_merchich;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

        // Sécurité : Réactiver le bouton après 10 secondes si pas de réponse
        new Handler().postDelayed(() -> {
            if (!bRegister.isEnabled()) {
                bRegister.setEnabled(true);
                Log.w(TAG, "Timeout : Firebase ne répond pas. Vérifiez SHA-1 et Internet.");
                Toast.makeText(Register.this, "Le serveur ne répond pas. Vérifiez votre connexion ou votre config SHA-1.", Toast.LENGTH_LONG).show();
            }
        }, 10000);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        bRegister.setEnabled(true);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Utilisateur créé avec succès !");
                            Toast.makeText(Register.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Erreur inconnue";
                            Log.e(TAG, "Erreur Firebase : " + error);
                            Toast.makeText(Register.this, "Erreur : " + error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
