package com.moutamid.sqlapp.activities.Organizer.Adapter;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Organizer.Model.FileData;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private List<FileData> fileList;

    public FileAdapter(List<FileData> fileList) {
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileData fileData = fileList.get(position);
        holder.fileNameTextView.setText(fileData.getFileName());
        holder.fileSizeTextView.setText(Formatter.formatFileSize(holder.itemView.getContext(), fileData.getFileSize()));
        holder.imageView.setImageBitmap(fileData.getBitmap());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, fileList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView, fileSizeTextView;
        ImageView delete, imageView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            fileSizeTextView = itemView.findViewById(R.id.fileSizeTextView);
            delete = itemView.findViewById(R.id.delete);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
