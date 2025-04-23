package com.moutamid.sqlapp.activities.Calender.calenderapp.weekview;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditEventDailogue extends Dialog {

    public Context c;
    private EditText addEventEditText;
    private EditText date_edit;
    private ImageView closeIcon, delete_icon,  timerIcon, menuIcon;
    private CheckBox allDayCheckbox;
    private EditText descriptionText;
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
    private Calendar calendar;
    String timeRange;

    public EditEventDailogue(Context a, long id, String formatted_date, String title,String exact_time_value, String time, String description, boolean checked, String date) {
        super(a);
        this.id = id;
        this.c = a;
        this.title_value = title;
        this.time_value = time;
        this.description_value = description;
        this.checked_value = checked;
        this.date_value = date;
        this.exact_time_value = exact_time_value;
        this.date_str = formatted_date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.edit_add_event);
        // Find views
        addEventEditText = findViewById(R.id.add_event);
        closeIcon = findViewById(R.id.close_icon);
        delete_icon = findViewById(R.id.delete_icon);
        timerIcon = findViewById(R.id.timer_icon);
        date_edit = findViewById(R.id.date);
        allDayCheckbox = findViewById(R.id.all_day_checkbox);
        descriptionText = findViewById(R.id.description_text);
        saveButton = findViewById(R.id.save_button);
        addEventEditText.setText(title_value);
        descriptionText.setText(description_value);
        String dateStr = date_value;
        String timeStr = time_value;
        calendar = Calendar.getInstance();
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
        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventDbHelper dataSource = new EventDbHelper(getContext());
                dataSource.deleteEvent(id);
//                MainActivity.activity.startActivity(new Intent(MainActivity.activity, MainActivity.class));
                dismiss();

            }
        });
        date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        date_edit.setText(formattedDate+" "+exact_time_value);
        allDayCheckbox.setChecked(checked_value);
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
                String title = addEventEditText.getText().toString();
                String date =date_value ; // Get the date value from your EditText
                String time = timeStr; // Get the time value from your EditText
                String description = descriptionText.getText().toString();
                boolean checked = allDayCheckbox.isChecked();
                EventDbHelper dataSource = new EventDbHelper(getContext());
                dataSource.updateEvent(id, title, date, exact_time_value, time, description, checked);
                c.startActivity(new Intent(c, MainActivity.class));
                dismiss();
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


//
                        date_edit.setText(date_str+" "+timeRange);
                    }
                }, hour, minute, false);

        timePickerDialog.setTitle("End Time");
        timePickerDialog.show();
    }

}
