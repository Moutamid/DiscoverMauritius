package com.moutamid.sqlapp.activities.ContactUs;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.fxn.stash.Stash;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.sqlapp.R;

public class ContactUsActivity extends AppCompatActivity {
    EditText first_name, last_name, email, message;
    TextView name_txt, last_name_txt, email_txt;


    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        first_name = findViewById(R.id.first_name);
        name_txt = findViewById(R.id.name_txt);
        last_name = findViewById(R.id.last_name);
        last_name_txt = findViewById(R.id.last_name_txt);
        email = findViewById(R.id.email);
        email_txt = findViewById(R.id.email_txt);
        message = findViewById(R.id.message);

        applyStylesToTextInputLayoutHint(first_name, name_txt, "First Name*");
        applyStylesToTextInputLayoutHint(last_name, last_name_txt, "Last Name*");
        applyStylesToTextInputLayoutHint(email, email_txt, "E-mail*");
        applyStylesToTextInputLayoutHint1(message, "Message");

        TextView saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Finger is touching the button, set transparency to 50%
                        saveBtn.setAlpha(0.5f);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Finger is lifted or moved away, set transparency back to 0%
                        saveBtn.setAlpha(1.0f);
                        break;
                }
                return false; // Return false to allow the click event to be handled by the onClick attribute
            }
        });

    }

    public void BackPress(View view) {
        onBackPressed();
    }

    private void applyStylesToTextInputLayoutHint(EditText editText, TextView textView, String hint) {
        TextInputLayout textInputLayout = (TextInputLayout) editText.getParent().getParent();
        SpannableString spannableString = new SpannableString(hint);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, hint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textInputLayout.setHint(spannableString);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (editText.getText().toString().length() != 0) {
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    textView.setVisibility(View.VISIBLE);

                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    if (!editText.isFocused()) {
                        textView.setVisibility(View.INVISIBLE);
                    } else {
                        textView.setVisibility(View.VISIBLE);
                    }
                } else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // The EditText gained focus
                    if (editText.getText().toString().length() != 0) {
                        textView.setVisibility(View.INVISIBLE);
                    } else {
                        textView.setVisibility(View.VISIBLE);

                    }
                } else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void applyStylesToTextInputLayoutHint1(EditText editText, String hint) {
        TextInputLayout textInputLayout = (TextInputLayout) editText.getParent().getParent();
        SpannableString spannableString = new SpannableString(hint);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, hint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textInputLayout.setHint(spannableString);


    }

    public void send(View view) {
        if (first_name.getText().toString().length() == 0 || last_name.getText().toString().length() == 0 || email.getText().toString().length() == 0) {
            ErrorDialog cdd = new ErrorDialog(ContactUsActivity.this);
            cdd.show();
        } else {
            Stash.put("email_first_name", first_name.getText().toString());
            Stash.put("email_last_name", last_name.getText().toString());
            Stash.put("email_address", email.getText().toString());
            if (message.getText().toString().length() == 0) {

                Stash.put("email_message", "no message yet");

            } else {
                Stash.put("email_message", message.getText().toString());
            }

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"daveberri@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from MyTrips");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + first_name.getText().toString() + " " + last_name.getText().toString() + "\n\n" +
                    "Email: " + email.getText().toString() + "\n\n" +
                    "Message: " + message.getText().toString());

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean isFirstTime = true;

    @Override
    protected void onResume() {
        super.onResume();

        if (!isFirstTime) {
            first_name.setText("");
            last_name.setText("");
            email.setText("");
            message.setText("");

            new AlertDialog.Builder(this)
                    .setMessage("Message successfully sent!")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        }

        isFirstTime = false;

    }
}