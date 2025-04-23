package com.moutamid.sqlapp.activities.Organizer.helper;

// CustomDialogFragment.java
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Organizer.SendActivity;

public class CustomDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the custom layout
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_layout, null);

        // Set the custom layout to the dialog builder
        builder.setView(view);

        // Set click listener for dialog dismissal
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dismiss the dialog
                dialogInterface.dismiss();
                Intent intent = new Intent(getContext(), SendActivity.class);
                intent.putExtra("position", Stash.getInt("position"));
                startActivity(intent);
            }
        });

        // Create and return the dialog
        return builder.create();
    }
}
