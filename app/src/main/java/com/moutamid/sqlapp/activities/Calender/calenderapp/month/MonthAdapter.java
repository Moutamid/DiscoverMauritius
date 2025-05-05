package com.moutamid.sqlapp.activities.Calender.calenderapp.month;


import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Calender.calenderapp.EventInfo;
import com.moutamid.sqlapp.activities.Calender.calenderapp.MainActivity;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.Event;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.EventDbHelper;
import com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.MonthAdEventDailogue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.DateViewHolder> {
    private Activity context;
    private List<String> dates;
    private List<String> search_dates;
    public String currentDate;
    MonthEventAdapter eventAdapter;
    private List<String> search_dates_local_event;

    public MonthAdapter(Activity context, List<String> dates, String currentDate, List<String> search_dates, List<String> search_dates_local_event) {
        this.context = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.search_dates = search_dates;
        this.search_dates_local_event = search_dates_local_event;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        holder.bind(dates.get(position));
        File cacheDir = new File(context.getFilesDir(), "cached_images");
        String fullPath = cacheDir.getAbsolutePath();
        List<EventInfo> eventInfos = MainActivity.func_week(context, search_dates.get(position));
        if (eventInfos.size() > 0) {
            holder.recyclerView.setVisibility(View.VISIBLE);

        }
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        eventAdapter = new MonthEventAdapter(context, MainActivity.func_week(context, search_dates.get(position)));
        holder.recyclerView.setAdapter(eventAdapter);
        if (MainActivity.func_week(context, search_dates.get(position)).size() > 0) {
            holder.main_layout.setBackgroundResource(R.drawable.bg_stroke_current);
        }
        EventDbHelper eventDbHelper = new EventDbHelper(context);

        List<Event> events = eventDbHelper.getEventsAll(search_dates.get(position));
        List<String> checkedTitles = new ArrayList<>();
        List<Event> checkedEvents = new ArrayList<>();
        for (Event event : events) {
            holder.recyclerView_local_event.setVisibility(View.VISIBLE);
            checkedTitles.add(event.getTitle());
            checkedEvents.add(event);

        }
        holder.recyclerView_local_event.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        MonthLocalEventAdapter local_adapter = new MonthLocalEventAdapter(checkedEvents, context);
        holder.recyclerView_local_event.setAdapter(local_adapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position >= 0 && position < search_dates_local_event.size()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    SimpleDateFormat stime = new SimpleDateFormat("hh-mm", Locale.getDefault());
                    Date currentDate = new Date();
                    String formattedDate = sdf.format(currentDate);
                    String time = stime.format(currentDate);
                    Log.d("gfgfgfghh", search_dates_local_event.get(position)+" "+formattedDate);
                    MonthAdEventDailogue adEventDailogue = new MonthAdEventDailogue(context, search_dates_local_event.get(position)+"   "+formattedDate, time);
                    adEventDailogue.show();
                } else {
                    // Handle the case when position is out of bounds
                    Log.e("onClick", "Position out of bounds: " + position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView, recyclerView_local_event;
        RelativeLayout main_layout;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            recyclerView_local_event = itemView.findViewById(R.id.recyclerView_local_event);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            main_layout = itemView.findViewById(R.id.main_layout);
        }

        public void bind(String date) {
//            if (date.isEmpty()) {
//                itemView.setBackgroundColor(Color.parseColor("#EFEFEF"));
//                textView.setText("");
//            } else {
                textView.setText(date);
                // Reset background color
                // Set text color based on whether it's the current date or not
                if (date.equals(currentDate)) {
                    main_layout.setBackgroundResource(R.drawable.bg_stroke_current);
                    textView.setTextColor(Color.parseColor("#ffffff"));
                    textView.setBackgroundColor(Color.parseColor("#5C79FF"));

                } else {
                    main_layout.setBackgroundResource(R.drawable.bg_stroke);

                    textView.setTextColor(Color.parseColor("#000000"));
//                }
            }
        }

    }


    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
