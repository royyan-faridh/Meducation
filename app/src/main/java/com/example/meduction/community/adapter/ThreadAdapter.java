package com.example.meduction.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.meduction.R;
import com.example.meduction.community.models.Thread;


import com.example.meduction.community.ui.ThreadDetailActivity;

import java.util.List;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadViewHolder> {

    private Context context;
    private List<Thread> threadList;

    public ThreadAdapter(Context context, List<Thread> threadList) {
        this.context = context;
        this.threadList = threadList;
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_thread_adapter, parent, false);
        return new ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
        Thread thread = threadList.get(position);
        holder.tvThreadTitle.setText(thread.getTitle());
        holder.tvThreadAuthor.setText(thread.getAuthor());
        holder.tvThreadDate.setText(thread.getDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ThreadDetailActivity.class);
            intent.putExtra("thread", (Parcelable) thread); // Menggunakan Parcelable
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    // ViewHolder untuk item thread
    public class ThreadViewHolder extends RecyclerView.ViewHolder {
        public TextView tvThreadTitle;
        public TextView tvThreadAuthor;
        public TextView tvThreadDate;

        public ThreadViewHolder(View itemView) {
            super(itemView);
            tvThreadTitle = itemView.findViewById(R.id.tvThreadTitle);    // ID dari item_thread.xml
            tvThreadAuthor = itemView.findViewById(R.id.tvThreadAuthor);  // ID dari item_thread.xml
            tvThreadDate = itemView.findViewById(R.id.tvThreadDate);      // ID dari item_thread.xml
        }
    }
}
