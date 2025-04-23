package com.moutamid.sqlapp.activities.Calender.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.moutamid.sqlapp.R;

import java.util.Calendar;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<String> timeSlots;
    private int currentTimePosition = -1; // Variable to hold the position of the current time slot

    public TimeSlotAdapter(List<String> timeSlots) {
        this.timeSlots = timeSlots;
        updateCurrentTimePosition();
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String timeSlot = timeSlots.get(position);
        holder.bind(timeSlot);

        // Set line's visibility based on current time position
        if (position == currentTimePosition) {
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public void updateTimeSlots(List<String> updatedTimeSlots) {
        timeSlots.clear();
        timeSlots.addAll(updatedTimeSlots);
        updateCurrentTimePosition();
        notifyDataSetChanged();
    }

    private void updateCurrentTimePosition() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Calculate the current time position in minutes
        int currentTimePosition = currentHour * 60 + currentMinute;

        // Find the nearest time slot
        int nearestTimePosition = -1;
        int minDifference = Integer.MAX_VALUE;

        for (int i = 0; i < timeSlots.size(); i++) {
            String[] parts = timeSlots.get(i).split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1].substring(0, 2)); // Remove am/pm and convert to integer
            int timeSlotPosition = hour * 60 + minute;

            // Calculate the difference between the current time and the time slot
            int difference = Math.abs(currentTimePosition - timeSlotPosition);

            // Update the nearest time slot if this time slot has a smaller difference
            if (difference < minDifference) {
                minDifference = difference;
                nearestTimePosition = timeSlotPosition;
            }
        }

        this.currentTimePosition = nearestTimePosition;

     Log.d("data", currentHour+ "  "+ currentMinute+"   "+ currentTimePosition);

    }

    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        View line;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            line = itemView.findViewById(R.id.line);
        }

        public void bind(String timeSlot) {
            timeTextView.setText(timeSlot);
        }
    }
}
