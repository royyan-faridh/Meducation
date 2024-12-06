package com.example.meduction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class EdukasiAdapter extends RecyclerView.Adapter<EdukasiAdapter.ViewHolder> {

    private List<Item> itemList;
    private OnItemClickListener onItemClickListener;

    // Constructor untuk menerima OnItemClickListener
    public EdukasiAdapter(List<Item> itemList, OnItemClickListener onItemClickListener) {
        this.itemList = itemList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gunakan item_edukasi.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_edukasi_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        // Menggunakan Glide untuk memuat gambar dari URL
        Glide.with(holder.image.getContext())
                .load(item.getImageUrl())  // URL gambar
                .into(holder.image);       // ImageView tempat gambar akan dimuat

        // Set listener untuk item klik
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            description = itemView.findViewById(R.id.descriptionTextView);
            image = itemView.findViewById(R.id.imageView);
        }
    }

    // Interface untuk menangani klik item
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
}