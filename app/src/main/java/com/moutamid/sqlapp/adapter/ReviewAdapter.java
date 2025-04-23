package com.moutamid.sqlapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.model.Review;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;
    String title;

    public ReviewAdapter(List<Review> reviews, String title) {
        this.reviews = reviews;
        this.title = title;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.reviewText.setText(review.getReview());
        holder.timeText.setText(getTimeAgo(review.getTimestamp()));
        loadRating(title, holder);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewText, timeText;
        TextView  count;
        me.zhanghai.android.materialratingbar.MaterialRatingBar ratingBar;

        ReviewViewHolder(View itemView) {
            super(itemView);
            reviewText = itemView.findViewById(R.id.reviewText);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            count = itemView.findViewById(R.id.count);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }

    private String getTimeAgo(long timestamp) {
        long now = System.currentTimeMillis() / 1000;
        long diff = now - timestamp;

        if (diff < 60) return "Just now";
        else if (diff < 3600) return (diff / 60) + " minutes ago";
        else if (diff < 86400) return (diff / 3600) + " hours ago";
        else return (diff / 86400) + " days ago";
    }
    private void loadRating(String placeName, ReviewViewHolder holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings").child(placeName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float total = 0;
                int count = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Float rating = child.child("rating").getValue(Float.class);
                    if (rating != null) {
                        total += rating;
                        count++;
                    }
                }

                if (count > 0) {
                    float average = total / count;
                   holder.ratingBar.setRating(average);
                    // Optional: show total reviewers
                    holder.count.setText(average + "  (" + count + ")");
                    Log.d("Rating", "Average: " + average + ", Count: " + count);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Rating", "Error: " + error.getMessage());
            }
        });
    }
}
