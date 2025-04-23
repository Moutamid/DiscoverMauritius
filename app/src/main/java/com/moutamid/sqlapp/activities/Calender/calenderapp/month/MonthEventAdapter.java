package com.moutamid.sqlapp.activities.Calender.calenderapp.month;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Calender.calenderapp.EventInfo;

import java.util.List;

public  class MonthEventAdapter extends RecyclerView.Adapter<MonthEventAdapter.EventViewHolder> {
   private Context context;
   private List<EventInfo> eventList;

   public MonthEventAdapter(Context context, List<EventInfo> eventList) {
       this.context = context;
       this.eventList = eventList;
   }

   @NonNull
   @Override
   public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_event_month, parent, false);
       return new EventViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
       EventInfo event = eventList.get(position);
       Log.d("dataaaa", event.nextnode + "   " + event.endtime + event.eventtitles + event.starttime + event.accountname + "  " + event.timezone + "  " + event.id + " ");
       holder.bind(event);
   }

   @Override
   public int getItemCount() {
       return eventList.size();
   }

   public static class EventViewHolder extends RecyclerView.ViewHolder {
       TextView textViewTitle;

       public EventViewHolder(@NonNull View itemView) {
           super(itemView);
           textViewTitle = itemView.findViewById(R.id.event_name);
       }

       public void bind(EventInfo event) {
           textViewTitle.setText(event.title);
       }
   }
}
  