package com.moutamid.sqlapp.activities.Organizer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Organizer.Model.FileData;
import com.moutamid.sqlapp.activities.Organizer.PdfViewerActivity;
import com.moutamid.sqlapp.activities.Organizer.helper.Constants;

import java.util.List;

public class ViewFileAdapter extends RecyclerView.Adapter<ViewFileAdapter.FileViewHolder> {
Context context;
    private List<FileData> fileList;

    public ViewFileAdapter(Context context, List<FileData> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_view, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileData fileData = fileList.get(position);
        holder.imageView.setImageBitmap(fileData.getBitmap());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, PdfViewerActivity.class);
                Stash.put(Constants.PATH, fileData.getFileUri());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        //        TextView fileNameTextView, fileSizeTextView;
//        ImageView delete;
        ImageView imageView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
//            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
//            fileSizeTextView = itemView.findViewById(R.id.fileSizeTextView);
//            delete = itemView.findViewById(R.id.delete);
        }
    }
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }


}
