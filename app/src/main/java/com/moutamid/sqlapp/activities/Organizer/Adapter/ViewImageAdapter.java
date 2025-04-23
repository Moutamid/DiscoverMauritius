package com.moutamid.sqlapp.activities.Organizer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Organizer.ImageViewrActivity;
import com.moutamid.sqlapp.activities.Organizer.Model.ImageData;

import java.util.List;

public class ViewImageAdapter extends RecyclerView.Adapter<ViewImageAdapter.ImageViewHolder> {
    Context context;
    private List<ImageData> imageList;

    public ViewImageAdapter(Context context, List<ImageData> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_view, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageData imageData = imageList.get(position);
        holder.imageNameTextView.setText(imageData.getImageName());
        Log.d("uri", imageData.getImageUri() +"    uri");
        holder.imageSizeTextView.setText(Formatter.formatFileSize(holder.imageView.getContext(), imageData.getImageSize()));
        Glide.with(holder.itemView.getContext())
                .load(imageData.getImageUri())
                .into(holder.imageView);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, imageList.size());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageData imageData = imageList.get(position);
                Intent intent = new Intent(context, ImageViewrActivity.class);
                intent.putExtra("imageUri", imageData.getImageUri().toString()); // Put the Uri as a string
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, delete;
        TextView imageNameTextView, imageSizeTextView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            imageView = itemView.findViewById(R.id.imageView);
            imageNameTextView = itemView.findViewById(R.id.imageNameTextView);
            imageSizeTextView = itemView.findViewById(R.id.imageSizeTextView);
        }
    }

}
