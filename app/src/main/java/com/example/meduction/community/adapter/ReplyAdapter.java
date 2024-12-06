package com.example.meduction.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.meduction.R;

import com.example.meduction.community.models.Reply;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private Context context;
    private List<Reply> replyList;

    public ReplyAdapter(Context context, List<Reply> replyList) {
        this.context = context;
        this.replyList = replyList;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_reply_adapter, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Reply reply = replyList.get(position);
        holder.tvReplyAuthor.setText(reply.getAuthor());
        holder.tvReplyDate.setText(reply.getDate());
        holder.tvReplyContent.setText(reply.getContent());
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView tvReplyAuthor, tvReplyDate, tvReplyContent;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReplyAuthor = itemView.findViewById(R.id.tvReplyAuthor);
            tvReplyDate = itemView.findViewById(R.id.tvReplyDate);
            tvReplyContent = itemView.findViewById(R.id.tvReplyContent);
        }
    }
}
