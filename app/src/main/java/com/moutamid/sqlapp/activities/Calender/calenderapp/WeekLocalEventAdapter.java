package com.moutamid.sqlapp.activities.Calender.calenderapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.Event;
import com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.ViewEventDailogue;

import java.util.List;

public class WeekLocalEventAdapter extends RecyclerView.Adapter<WeekLocalEventAdapter.EventViewHolder> {
    private List<Event> events;
Context context;

    public WeekLocalEventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_local_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.textViewTitle.setText(event.title);
        holder.time.setText(event.exact_time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewEventDailogue adEventDailogue = new ViewEventDailogue(context, event.id, event.title, event.time, event.description, event.checked, event.date, event.exact_time);
                adEventDailogue.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, time;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.event_name);
            time = itemView.findViewById(R.id.time);
        }
    }
}
