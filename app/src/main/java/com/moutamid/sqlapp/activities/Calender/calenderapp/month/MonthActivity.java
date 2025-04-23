package com.moutamid.sqlapp.activities.Calender.calenderapp.month;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthActivity extends AppCompatActivity {

    private RecyclerView recyclerView_month;
    private ImageView previousButton_month, nextButton_month;
    private List<String> dates_month = new ArrayList<>();
    private List<String> search_dates_month = new ArrayList<>();
    private Calendar currentDate_month = Calendar.getInstance();
    private TextView dateRangeTextView_month, range_month;
    String search_date_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);


        range_month = findViewById(R.id.range_month);
        dateRangeTextView_month = findViewById(R.id.dateRangeTextView_month);
        recyclerView_month = findViewById(R.id.recyclerView_month);
        previousButton_month = findViewById(R.id.previousButton_month);
        nextButton_month = findViewById(R.id.nextButton_month);

        updateDates_month(currentDate_month);

//        MonthAdapter adapter_month = new MonthAdapter(this, dates_month, getDate_month(currentDate_month), search_dates_month);
//        recyclerView_month.setAdapter(adapter_month);
        recyclerView_month.setLayoutManager(new GridLayoutManager(this, 7));

        previousButton_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousMonth();
//                adapter_month.notifyDataSetChanged(); // Notify adapter after updating dates
            }
        });

        nextButton_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextMonth();
//                adapter_month.notifyDataSetChanged(); // Notify adapter after updating dates
            }
        });
    }


    private String getDate_month(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private String getSearch_date_month(Calendar calendar) {
        SimpleDateFormat search_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return search_date.format(calendar.getTime());
    }

    private void updateDates_month(Calendar currentDate) {
        dates_month.clear();
        search_dates_month.clear();
        SimpleDateFormat search_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM ", Locale.getDefault());
        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE", Locale.getDefault());
        Calendar startDate = (Calendar) currentDate.clone();

        startDate.set(Calendar.DAY_OF_MONTH, 1);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        int maxDayOfMonth = startDate.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Get the day of the week for the first day of the month
        int firstDayOfWeek = startDate.get(Calendar.DAY_OF_WEEK);

        // Add empty spaces to the beginning of the list based on the first day of the week
        for (int i = 1; i < firstDayOfWeek; i++) {
            dates_month.add("");
            search_dates_month.add("");
        }

        for (int i = 1; i <= maxDayOfMonth; i++) {
            dates_month.add(sdf.format(startDate.getTime()));
            search_dates_month.add(search_date.format(startDate.getTime()));
            startDate.add(Calendar.DATE, 1);
        }

        // Use a copy of the current date for display purposes
        Calendar displayDate = (Calendar) currentDate.clone();

        String startDateString = sdf1.format(displayDate.getTime());
        String year = sdf2.format(displayDate.getTime());
        displayDate.set(Calendar.DAY_OF_MONTH, 1); // Reset to the first day of the month
        displayDate.add(Calendar.MONTH, 1); // Move to the next month
        displayDate.add(Calendar.DATE, -1); // Move to the last day of the current month
        String endDateString = sdf1.format(displayDate.getTime());

        range_month.setText(startDateString);
        dateRangeTextView_month.setText(year);
    }

    private void goToNextMonth() {
        currentDate_month.add(Calendar.MONTH, 1); // Move to the next month
        updateDates_month(currentDate_month); // Update the dates
    }

    private void goToPreviousMonth() {
        currentDate_month.add(Calendar.MONTH, -1); // Move to the previous month
        updateDates_month(currentDate_month); // Update the dates
    }


    private String getMonthName_month(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }
}
