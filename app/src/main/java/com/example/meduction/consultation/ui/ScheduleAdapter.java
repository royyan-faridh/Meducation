package com.example.meduction.consultation.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meduction.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<String> dateList;

    // Constructor
    public ScheduleAdapter(List<String> dateList) {
        this.dateList = dateList;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date, parent, false);
        return new ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        String fullDate = dateList.get(position); // Contoh format: "2024-10-14"

        // Format tanggal untuk mendapatkan hari dan tanggal
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault()); // Format untuk nama hari (Sen, Sel, dsb.)
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault()); // Format untuk tanggal (14, 15, dsb.)

            Date date = inputFormat.parse(fullDate); // Mengonversi string ke Date
            if (date != null) {
                String day = dayFormat.format(date); // Mendapatkan nama hari
                String dateNumber = dateFormat.format(date); // Mendapatkan tanggal

                holder.dayText.setText(day);
                holder.dateText.setText(dateNumber);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return dateList.size();
    }

    // ViewHolder untuk item_date.xml
    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView dayText, dateText;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }
}


