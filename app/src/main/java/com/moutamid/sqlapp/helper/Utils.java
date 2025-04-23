package com.moutamid.sqlapp.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.facebook.AccessToken;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Beaches.BeachDetails;
import com.moutamid.sqlapp.activities.LoginActivity;
import com.moutamid.sqlapp.activities.Stay.EatActivity;
import com.moutamid.sqlapp.activities.Stay.StayActivity;
import com.moutamid.sqlapp.activities.Tour.ToursActivity;

public class Utils {

    public static void loginBtnMenuListener(Activity activity) {
        TextView loginTextView = activity.findViewById(R.id.login_txt);
        if (loginTextView != null) {

            if (Stash.getBoolean(Constants.IS_LOGGED_IN, false)) {
                loginTextView.setText(Constants.LOGOUT);
            }

            RelativeLayout loginBtn = activity.findViewById(R.id.login_txt_layout);
            if (loginBtn != null) {
                loginBtn.setOnClickListener(v -> {
                    if (loginTextView.getText().toString().equals(Constants.LOGOUT)) {
                        // already logged in
                        new AlertDialog.Builder(activity)
                                .setTitle("Discover Mauritius")
                                .setMessage("Are you sure you want to log out?")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    Toast.makeText(activity, "Logged out!", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    Stash.put(Constants.IS_LOGGED_IN, false);
                                    activity.finish();
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    activity.startActivity(intent);
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .show();

                    } else {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    }
                });
            }

           /* loginTextView.setOnClickListener(v -> {

            });*/
        }

        RelativeLayout toursBtn = activity.findViewById(R.id.toursBtn);
        if (toursBtn != null) {
            toursBtn.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity, ToursActivity.class));
            });
        }

        RelativeLayout eatBtn = activity.findViewById(R.id.eatBtn);
        if (eatBtn != null) {
            eatBtn.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity, EatActivity.class));
            });
        }
        RelativeLayout stayBtn = activity.findViewById(R.id.stayBtn);
        if (stayBtn != null) {
            stayBtn.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity, StayActivity.class));
            });
        }
    }

}
