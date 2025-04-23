package com.moutamid.sqlapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.moutamid.sqlapp.R;

public class RemeberPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_remeber_password);

        emailEditText = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.save_btn).setOnClickListener(this::sendPasswordResetEmail);
    }

    public void sendPasswordResetEmail(View view) {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RemeberPasswordActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RemeberPasswordActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void BackPress(View view) {
        // Handle back press
        onBackPressed();
    }

    public void menu(View view) {
onBackPressed();    }

}
