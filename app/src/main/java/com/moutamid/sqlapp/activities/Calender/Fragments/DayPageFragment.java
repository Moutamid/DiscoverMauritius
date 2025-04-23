package com.moutamid.sqlapp.activities.Calender.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Calender.Adapter.TimeSlotAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DayPageFragment extends Fragment {

    private RecyclerView timeSlotRecyclerView;
    private TextView dateTextView;
    private TextView currentTimeMarker;

    private TimeSlotAdapter timeSlotAdapter;
    private Calendar currentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_page, container, false);

        timeSlotRecyclerView = view.findViewById(R.id.timeSlotRecyclerView);
        dateTextView = view.findViewById(R.id.dateTextView);
        currentTimeMarker = view.findViewById(R.id.currentTimeMarker);

        currentDate = Calendar.getInstance();
        setUpTimeSlotRecyclerView();
        updateDate(currentDate);
        updateCurrentTimeMarker();

        return view;
    }

    private void setUpTimeSlotRecyclerView() {
        timeSlotAdapter = new TimeSlotAdapter(generateTimeSlots());
        timeSlotRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        timeSlotRecyclerView.setAdapter(timeSlotAdapter);
    }

    private List<String> generateTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 360; i++) {
            calendar.set(Calendar.HOUR_OF_DAY, i);
            calendar.set(Calendar.MINUTE, 0);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String formattedTime = sdf.format(calendar.getTime());
            timeSlots.add(formattedTime);
        }

        return timeSlots;
    }

    private void updateDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = dateFormat.format(calendar.getTime());
        dateTextView.setText(dateString);
    }

    private void updateCurrentTimeMarker() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = timeFormat.format(Calendar.getInstance().getTime());
        currentTimeMarker.setText(currentTime);
        new android.os.Handler().postDelayed(this::updateCurrentTimeMarker, 60000);
    }
}
