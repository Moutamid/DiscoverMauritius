package com.moutamid.sqlapp.activities.Calender.calenderapp.weekview;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Calender.calenderapp.MainActivity;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.EventDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdEventDailogue extends Dialog {
    String formattedDate;
    public Activity c;
    private EditText addEventEditText;
    private EditText date;
    private ImageView closeIcon, timerIcon, menuIcon;
    private CheckBox allDayCheckbox;
    private EditText descriptionText;
    private TextView saveButton;
    String date_str;
    String eventTime;
    private Calendar calendar;
    String date_string;
    String timeRange;

    public AdEventDailogue(Activity a, String date, String eventTime) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.date_str = date;
        this.eventTime = eventTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.add_event);

        // Find views
        addEventEditText = findViewById(R.id.add_event);
        closeIcon = findViewById(R.id.close_icon);
        timerIcon = findViewById(R.id.timer_icon);
        date = findViewById(R.id.date);
        allDayCheckbox = findViewById(R.id.all_day_checkbox);
        descriptionText = findViewById(R.id.description_text);
        saveButton = findViewById(R.id.save_button);
        date.setText(date_str);
        calendar = Calendar.getInstance();
        String[] parts = date_str.split("\\s+");
        timeRange = parts[3] + " " + parts[4]; // Join the time parts with a space

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        addEventEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (input.length() > 15) {
                    addEventEditText.setError("Character limit is 15");
                    saveButton.setEnabled(false);
                } else {
                    addEventEditText.setError(null); // Remove the error
                    saveButton.setEnabled(true);
                }
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
                String inputDate = date_str;
                SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy   hh:mm a");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = inputFormat.parse(inputDate);
                    formattedDate = outputFormat.format(date);
                } catch (Exception e) {

                }
                String title = addEventEditText.getText().toString();
                String date_ = formattedDate; // Get the date value from your EditText
                String time = eventTime; // Get the time value from your EditText
                String description = descriptionText.getText().toString();
                boolean checked = allDayCheckbox.isChecked();
                EventDbHelper dataSource = new EventDbHelper(getContext());
                Log.d("Parameters", "Title: " + title);
                Log.d("Parameters", "Date: " + date_);
                Log.d("Parameters", "Time: " + time);
                Log.d("Parameters", "Time: " + timeRange);
                Log.d("Parameters", "Description: " + description);
                Log.d("Parameters", "Checked: " + checked);
                dataSource.insertEvent(title, date_, time, timeRange, description, checked, 0);
                c.startActivity(new Intent(c, MainActivity.class));
                dismiss();
                c.finish();
            }
        });
    }

    private void showTimePickerDialog() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(c,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int startHour = hourOfDay;
                        int startMinute = minute;

                        // Update the calendar to the selected start time
                        calendar.set(Calendar.HOUR_OF_DAY, startHour);
                        calendar.set(Calendar.MINUTE, startMinute);
                        eventTime = startHour + "-" + startMinute;
                        // Show end time picker
                        showEndTimePickerDialog();
                    }
                }, hour, minute, false);

        timePickerDialog.setTitle("Start Time");
        timePickerDialog.show();
    }

    private void showEndTimePickerDialog() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(c,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int endHour = hourOfDay;
                        int endMinute = minute;

                        DateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        String startTime = timeFormat.format(calendar.getTime());
                        calendar.set(Calendar.HOUR_OF_DAY, endHour);
                        calendar.set(Calendar.MINUTE, endMinute);
                        String endTime = timeFormat.format(calendar.getTime());
                        timeRange = startTime + " - " + endTime;

                        String dateTime = date_str;
                        String[] parts = dateTime.split("\\s+");
                        date_string = parts[0] + " " + parts[1] + " " + parts[2];

                        Log.d("datat", parts + "      " + date_string);

                        date.setText(date_string + " " + timeRange);
                    }
                }, hour, minute, false);

        timePickerDialog.setTitle("End Time");
        timePickerDialog.show();
    }
}
