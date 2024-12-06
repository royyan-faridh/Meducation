package com.example.meduction.consultation.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meduction.R;
import com.example.meduction.consultation.models.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private Context context;
    private List<Doctor> doctorList;
    private List<List<String>> scheduleData;

    public DoctorAdapter(Context context, List<Doctor> doctorList, List<List<String>> scheduleData) {
        this.context = context;
        this.doctorList = doctorList;
        this.scheduleData = scheduleData;

        // Debugging: Log data dokter dan jadwal
        for (int i = 0; i < doctorList.size(); i++) {
            Doctor doctor = doctorList.get(i);
            List<String> schedules = scheduleData.get(i);
            android.util.Log.d("DoctorAdapter", "Doctor: " + doctor.getName() + ", Schedules: " + schedules.toString());
        }
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);

        // Debugging: Log dokter yang sedang di-bind
        android.util.Log.d("DoctorAdapter", "Binding doctor: " + doctor.getName());

        // Bind doctor data
        holder.doctorName.setText(doctor.getName());
        holder.specialty.setText(doctor.getSpecialty());
        holder.sessionPrice.setText(doctor.getPrice());
        holder.ratingText.setText(String.valueOf(doctor.getRating()));
        holder.doctorBackground.setImageResource(doctor.getImage());

        // Debugging: Log jadwal untuk dokter ini
        List<String> schedules = scheduleData.get(position);
        android.util.Log.d("DoctorAdapter", "Schedules for " + doctor.getName() + ": " + schedules.toString());

        // Bind schedule data
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedules);
        holder.rvSchedule.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvSchedule.setAdapter(scheduleAdapter);

        // Debugging: Log detail jadwal per dokter
        for (String schedule : schedules) {
            android.util.Log.d("DoctorAdapter", "Schedule: " + schedule);
        }

        // Update schedule items (dayText and dateText)
        for (String schedule : schedules) {
            try {
                String[] splitSchedule = schedule.split(" "); // Memisahkan hari dan tanggal
                String day = splitSchedule[0];  // Hari (misalnya "Sen")
                String date = splitSchedule[1];  // Tanggal (misalnya "4")

                // Debugging: Log parsing jadwal
                android.util.Log.d("DoctorAdapter", "Parsed schedule - Day: " + day + ", Date: " + date);

                // Set dayText dan dateText
                holder.dayText.setText(day);
                holder.dateText.setText(date);
            } catch (Exception e) {
                android.util.Log.e("DoctorAdapter", "Error parsing schedule: " + schedule, e);
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = doctorList.size();
        android.util.Log.d("DoctorAdapter", "Total doctors: " + count);
        return count;
    }


    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, specialty, sessionPrice, ratingText;
        ImageView doctorBackground;
        ImageButton favoriteButton;
        RecyclerView rvSchedule;
        TextView dayText, dateText;

        public DoctorViewHolder(View itemView) {
            super(itemView);
            try {
                doctorName = itemView.findViewById(R.id.doctorName);
                specialty = itemView.findViewById(R.id.specialty);
                sessionPrice = itemView.findViewById(R.id.sessionPrice);
                ratingText = itemView.findViewById(R.id.ratingText);
                doctorBackground = itemView.findViewById(R.id.doctorBackground);
                favoriteButton = itemView.findViewById(R.id.favoriteButton);
                rvSchedule = itemView.findViewById(R.id.rvSchedule);

                // Inisialisasi dayText dan dateText untuk referensi
                dayText = itemView.findViewById(R.id.dayText);
                dateText = itemView.findViewById(R.id.dateText);

                // Debugging: Log keberhasilan binding view
                android.util.Log.d("DoctorViewHolder", "ViewHolder initialized successfully");
            } catch (Exception e) {
                android.util.Log.e("DoctorViewHolder", "Error initializing ViewHolder", e);
            }
        }

    }
}
