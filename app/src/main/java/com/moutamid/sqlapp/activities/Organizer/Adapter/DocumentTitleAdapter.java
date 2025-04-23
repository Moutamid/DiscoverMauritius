package com.moutamid.sqlapp.activities.Organizer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Organizer.DocDetailsActivity;

import java.util.List;

public class DocumentTitleAdapter extends RecyclerView.Adapter<DocumentTitleAdapter.DocumentTitleViewHolder> {
    private List<String> documentTitles;
    Context context;
    public DocumentTitleAdapter(Context context, List<String> documentTitles) {
        this.context = context;
        this.documentTitles = documentTitles;
    }
    public void filterList(List<String> filteredList) {
        documentTitles = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DocumentTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document_title, parent, false);
        return new DocumentTitleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentTitleViewHolder holder, int position) {
        String documentTitle = documentTitles.get(position);
        int i = position + 1;
        holder.bind(documentTitle, i);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent= new Intent(context, DocDetailsActivity.class);
            intent.putExtra("position", position);
            context.startActivity(intent);
        }
    });}

    @Override
    public int getItemCount() {
        return documentTitles.size();
    }

    static class DocumentTitleViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle, number;

        DocumentTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }

        void bind(String documentTitle, int i) {
            textViewTitle.setText(documentTitle);
            number.setText(i+"");
        }
    }
}
