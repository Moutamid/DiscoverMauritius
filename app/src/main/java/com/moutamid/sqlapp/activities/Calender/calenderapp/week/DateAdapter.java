package com.moutamid.sqlapp.activities.Calender.calenderapp.week;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Calender.calenderapp.MainActivity;
import com.moutamid.sqlapp.activities.Calender.calenderapp.WeekLocalEventAdapter;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.Event;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.EventDbHelper;
import com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.MonthAdEventDailogue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private Activity context;
    private List<String> dates;
    private List<String> search_dates;
    private List<String> search_dates_local_event;
    public String currentDate;
    EventAdapter eventAdapter;

    public DateAdapter(Activity context, List<String> dates, String currentDate, List<String> search_dates, List<String> search_dates_local_event) {
        this.context = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.search_dates = search_dates;
        this.search_dates_local_event = search_dates_local_event;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_week, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        holder.bind(dates.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        eventAdapter = new EventAdapter(context, MainActivity.func_week(context, search_dates.get(position)));
        holder.recyclerView.setAdapter(eventAdapter);
        EventDbHelper eventDbHelper = new EventDbHelper(context);
        List<Event> events = eventDbHelper.getEventsAll(search_dates.get(position));
        List<String> checkedTitles = new ArrayList<>();
        List<Event> checkedEvents = new ArrayList<>();
        for (Event event : events) {
            holder.recyclerView_local_event.setVisibility(View.VISIBLE);
            checkedTitles.add(event.getTitle());
            checkedEvents.add(event);
        }
        holder.recyclerView_local_event.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        WeekLocalEventAdapter local_adapter = new WeekLocalEventAdapter(checkedEvents, context);
        holder.recyclerView_local_event.setAdapter(local_adapter);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                SimpleDateFormat stime = new SimpleDateFormat("hh-mm", Locale.getDefault());
                Date currentDate = new Date();
                String formattedDate = sdf.format(currentDate);
                String time = stime.format(currentDate);
                Log.d("gfgfgfghh", search_dates_local_event.get(position)+" "+formattedDate);
                MonthAdEventDailogue adEventDailogue = new MonthAdEventDailogue(context, search_dates_local_event.get(position)+"   "+formattedDate, time);
                adEventDailogue.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View dateView;
        RecyclerView recyclerView;
        RecyclerView recyclerView_local_event;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            dateView = itemView.findViewById(R.id.dateView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView_local_event = itemView.findViewById(R.id.recyclerView_local_event);
        }

        public void bind(String date) {
            String[] parts = date.split(" ");
            String dayOfWeek = parts[0];
            String dayOfMonth = parts[1];
            SpannableStringBuilder builder = new SpannableStringBuilder(dayOfWeek + "\n" + dayOfMonth);
            builder.setSpan(new AbsoluteSizeSpan(10, true), 0, dayOfWeek.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(13, true), dayOfWeek.length() + 1, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
            if (date.equals(currentDate))
            {
                dateView.setVisibility(View.VISIBLE);
                textView.setTextColor(Color.parseColor("#5C79FF"));
            } else {
                dateView.setVisibility(View.GONE);
                textView.setTextColor(Color.parseColor("#000000"));

            }


        }
    }


    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
