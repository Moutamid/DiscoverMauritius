package com.moutamid.sqlapp.activities.ExploreNew;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.databinding.ActivityExpListBinding;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.model.BrainDataModel;

import java.util.ArrayList;

public class ExpListActivity extends AppCompatActivity {

    ActivityExpListBinding b;
    ArrayList<BrainDataModel> dataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityExpListBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.icon.setOnClickListener(v -> {
            finish();
        });

        if (getIntent().hasExtra(Constants.PARAMS)) {
            b.title.setText(getIntent().getStringExtra(Constants.PARAMS) + "");
        }

    }

    private RecyclerView listRv;
    private RecyclerViewAdapterMessages listAdapter;

    private void initRecyclerView() {

        listRv = b.listView;
        listRv.addItemDecoration(new DividerItemDecoration(listRv.getContext(), DividerItemDecoration.VERTICAL));
        listAdapter = new RecyclerViewAdapterMessages();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
        listRv.setLayoutManager(linearLayoutManager);

        listRv.setHasFixedSize(false);

        listRv.setNestedScrollingEnabled(false);

        listRv.setAdapter(listAdapter);
//        permissionRecyclerView.scrollToPosition(permissionListAdapter.getItemCount() - 1);

    }

    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public RecyclerViewAdapterMessages.ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_layout, parent, false);
            return new RecyclerViewAdapterMessages.ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewAdapterMessages.ViewHolderRightMessage holder, int p) {
            int position = holder.getAdapterPosition();

            BrainDataModel brainDataModel = dataList.get(position);

            holder.itemTextView.setText(brainDataModel.name);

            holder.image.setImageResource(brainDataModel.headerImg);

        }

        @Override
        public int getItemCount() {
            if (dataList == null)
                return 0;
            return dataList.size();
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

            TextView itemTextView;
            ImageView image;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                itemTextView = v.findViewById(R.id.title);
                image = v.findViewById(R.id.image);

            }
        }

    }

}
