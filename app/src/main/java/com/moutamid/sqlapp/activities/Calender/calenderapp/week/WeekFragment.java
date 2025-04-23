package com.moutamid.sqlapp.activities.Calender.calenderapp.week;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.moutamid.sqlapp.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeekFragment extends Fragment {

    private static final String ARG_WEEK_START_DATE = "week_start_date";

    private Calendar weekStartDate;

    public static WeekFragment newInstance(Calendar weekStartDate) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEEK_START_DATE, weekStartDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weekStartDate = (Calendar) getArguments().getSerializable(ARG_WEEK_START_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        TextView textView = view.findViewById(R.id.textView);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault());
        textView.setText(sdf.format(weekStartDate.getTime()));

        return view;
    }
}
