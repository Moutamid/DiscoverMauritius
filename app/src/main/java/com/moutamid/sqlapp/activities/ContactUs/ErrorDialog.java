package com.moutamid.sqlapp.activities.ContactUs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.moutamid.sqlapp.R;

public class ErrorDialog extends Dialog {

    public Activity c;
    public Dialog d;
    private TextView title_ok;
    public ErrorDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.error_dailogue);
        title_ok = findViewById(R.id.title_ok);
        title_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });

    }



}