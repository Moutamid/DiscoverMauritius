package com.moutamid.sqlapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fxn.stash.Stash;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.InAppPurchase.SliderAdapterExample;
import com.moutamid.sqlapp.helper.Constants;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private EditText emailEditText, passwordEditText;
    private ImageView togglePasswordVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        File cacheDir = new File(getFilesDir(), "cached_images");
        String fullPath = cacheDir.getAbsolutePath();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize EditText fields
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("97166215280-4ja4kllhtq8fcvtt01bg4bfko7lqv1c3.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        findViewById(R.id.facebook_login).setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "Facebook onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "Facebook onError", error);
                }
            });
        });
        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility);

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


        findViewById(R.id.google_login).setOnClickListener(v -> signInWithGoogle());

        findViewById(R.id.save_btn).setOnClickListener(this::login);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show();
                Stash.put(Constants.IS_LOGGED_IN, true);

                Log.w(TAG, "Google sign in failed", e);

                finish();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    public void login(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in
            Toast.makeText(this, "Authentication successful!", Toast.LENGTH_SHORT).show();
            Stash.put(Constants.IS_LOGGED_IN, true);
            finish();
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            // You can add additional actions here, such as redirecting to another activity
        } else {
            // User is signed out
            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void BackPress(View view) {
        // Handle back press
        onBackPressed();
    }

    public void menu(View view) {
        // Handle menu click
        onBackPressed();
    }


    public void recover(View view) {
startActivity(new Intent(this, RemeberPasswordActivity.class));
    }

    public void signup(View view) {
        startActivity(new Intent(this, CreateAccountActivity.class));

    }
}
