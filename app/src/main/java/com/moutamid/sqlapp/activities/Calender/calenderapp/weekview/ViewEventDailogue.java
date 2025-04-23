package com.moutamid.sqlapp.activities.Calender.calenderapp.weekview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Calender.calenderapp.MainActivity;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.EventDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewEventDailogue extends Dialog {

    public Context c;
    private TextView addEventEditText;
    private TextView date_edit;
    private ImageView closeIcon, delete_icon,  edit_icon;
    private TextView saveButton;
    String date_str;
    String eventTime;

    public long id;
    public String title_value;
    public String date_value;
    public String time_value;
    public String description_value;
    public String exact_time_value;
    public boolean checked_value;
    public ViewEventDailogue(Context a, long id, String title, String time, String description, boolean checked, String date, String exact_time_value) {
        super(a);
        this.id = id;
        this.c = a;
        this.title_value = title;
        this.time_value = time;
        this.description_value = description;
        this.checked_value = checked;
        this.date_value = date;
        this.exact_time_value = exact_time_value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.view_add_event);
        // Find views
        addEventEditText = findViewById(R.id.add_event);
        closeIcon = findViewById(R.id.close_icon);
        delete_icon = findViewById(R.id.delete_icon);
        edit_icon = findViewById(R.id.edit_icon);
        date_edit = findViewById(R.id.date);
        saveButton = findViewById(R.id.save_button);
        addEventEditText.setText(title_value);
        String dateStr = date_value;
        String timeStr = time_value;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM, dd, yyyy", Locale.getDefault());
        String formattedDate = "";
        try {
            Date date = inputDateFormat.parse(dateStr);
            formattedDate = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH-mm", Locale.getDefault());
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = "";
        try {
            Date time = inputTimeFormat.parse(timeStr);
            formattedTime = outputTimeFormat.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date_edit.setText(formattedDate+" "+exact_time_value);

        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventDbHelper dataSource = new EventDbHelper(getContext());
                dataSource.deleteEvent(id);
                c.startActivity(new Intent(c, MainActivity.class));
                dismiss();

            }
        });
        String finalFormattedDate = formattedDate;
        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                EditEventDailogue adEventDailogue = new EditEventDailogue(c, id, finalFormattedDate, title_value, exact_time_value, time_value, description_value, checked_value, date_value);
                adEventDailogue.show();

            }
        });
       closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Dismiss the dialog
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addEventEditText.getText().toString();
                String date =date_value ; // Get the date value from your EditText
                String time = timeStr; // Get the time value from your EditText
                EventDbHelper dataSource = new EventDbHelper(getContext());
                dataSource.completeEvent(id, title, date, exact_time_value, time, description_value, checked_value);
                c.startActivity(new Intent(c, MainActivity.class));
                dismiss();
            }
        });

    }
}
