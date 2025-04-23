package com.moutamid.sqlapp.offlinemap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> data;
Context context;
    public MyAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        File cacheDir = new File(context.getFilesDir(), "cached_images");
        String fullPath = cacheDir.getAbsolutePath();
        if (position == 0) {
            holder.logo.setImageResource(R.drawable.location_arrow_circle);
        } else {
            holder.logo.setImageResource(R.drawable.pin_circle);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView dragHandle;
        public ImageView logo;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textVView);
            dragHandle = itemView.findViewById(R.id.drag_handle);
            logo = itemView.findViewById(R.id.logo);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
}
