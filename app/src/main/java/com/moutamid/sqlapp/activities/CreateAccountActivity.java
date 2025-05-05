package com.moutamid.sqlapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.helper.Constants;

public class  CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nameEditText, emailEditText, passwordEditText, reEnterPasswordEditText;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        reEnterPasswordEditText = findViewById(R.id.re_enter_password);
        TextView saveButton = findViewById(R.id.save_btn);
        saveButton.setOnClickListener(this::registerUser);
        ImageView togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);

        togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    // Show Password
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    togglePasswordVisibility.setImageResource(R.drawable.ic_eye_open);
                } else {
                    // Hide Password
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    togglePasswordVisibility.setImageResource(R.drawable.ic_eye_closed);
                }
                // Move the cursor to the end of the text
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        ImageView reenter_toggle_password_visibility = findViewById(R.id.reenter_toggle_password_visibility);

        reenter_toggle_password_visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reEnterPasswordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    // Show Password
                    reEnterPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    reenter_toggle_password_visibility.setImageResource(R.drawable.ic_eye_open);
                } else {
                    // Hide Password
                    reEnterPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    reenter_toggle_password_visibility.setImageResource(R.drawable.ic_eye_closed);
                }
                // Move the cursor to the end of the text
                reEnterPasswordEditText.setSelection(reEnterPasswordEditText.getText().length());
            }
        });
    }

    public void registerUser(View view) {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String reEnterPassword = reEnterPasswordEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(reEnterPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount(name, email, password);
    }

    private void createAccount(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            storeUserData(user.getUid(), name, email);
                        }
                        updateUI(user);
//                        Stash.put(Constants.IS_PREMIUM, true);
                        Stash.put(Constants.IS_LOGGED_IN, true);
                    } else {
                        Toast.makeText(CreateAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void storeUserData(String uid, String name, String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("DiscoverMauritius").child("Users");
        User user = new User(name, email, false);
        usersRef.child(uid).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Stash.put(Constants.IS_PREMIUM, false);
                        Toast.makeText(CreateAccountActivity.this, "User data stored successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateAccountActivity.this, "Failed to store user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            finish();
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DashboardActivity.class));
            Stash.put(Constants.IS_LOGGED_IN, true);
        } else {
            Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void BackPress(View view) {
        onBackPressed();
    }

    public void menu(View view) {
        onBackPressed();
    }
}
