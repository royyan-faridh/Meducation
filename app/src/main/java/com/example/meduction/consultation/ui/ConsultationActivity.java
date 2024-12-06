package com.example.meduction.consultation.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meduction.HomeActivity;
import com.example.meduction.ProfileActivity;
import com.example.meduction.R;
import com.example.meduction.community.ui.CommunityActivity;
import com.example.meduction.consultation.models.Doctor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConsultationActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView rvSchedule, rvDoctors;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<String> scheduleList;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList;
    private List<List<String>> scheduleData; // Menambahkan data jadwal per dokter
    private FirebaseAuth auth;
    private TextView tvGreeting;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);
        EdgeToEdge.enable(this);

        // Debugging: Memastikan layout activity berhasil dipasang
        Log.d("ConsultationActivity", "Activity Consultation berhasil dimuat");

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView == null) {
            Log.e("ConsultationActivity", "BottomNavigationView tidak ditemukan");
        }

        // Tetapkan item aktif pada BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.nav_consultation);

        // Debugging: Memastikan item aktif dipilih dengan benar
        Log.d("ConsultationActivity", "Item BottomNavigationView terpilih: " + R.id.nav_consultation);

        // Panggil metode initListener untuk mengaktifkan listener navigasi
        initListener();

        // Inisialisasi data dokter dan jadwal
        doctorList = new ArrayList<>();

        scheduleData = new ArrayList<>();
        scheduleData.add(Arrays.asList("2024-10-14", "2024-10-15", "2024-10-16"));
        scheduleData.add(Arrays.asList("2024-10-17", "2024-10-18", "2024-10-19"));

        // Menambahkan data dokter
        doctorList.add(new Doctor("Dr. William Smith", "Cardiology", "Rp100.000", 4.5, R.drawable.doctor_image));
        doctorList.add(new Doctor("Dr. Albert Pramudita", "Neurology", "Rp60.000", 4.7, R.drawable.doctor_image_2));

        // Debugging: Mengecek data dokter yang berhasil ditambahkan
        Log.d("ConsultationActivity", "Data Dokter: " + doctorList.size() + " dokter ditambahkan");


        // Inisialisasi RecyclerView
        rvDoctors = findViewById(R.id.rvDoctors);
        if (rvDoctors == null) {
            Log.e("ConsultationActivity", "RecyclerView rvDoctors tidak ditemukan");
        }
        rvDoctors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Pasang adapter ke RecyclerView
        doctorAdapter = new DoctorAdapter(this, doctorList, scheduleData);
        rvDoctors.setAdapter(doctorAdapter);
        Log.d("ConsultationActivity", "Adapter Doctor berhasil dipasang");

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // TextView untuk menampilkan greeting
        tvGreeting = findViewById(R.id.tvGreeting);

        // Ambil username dari Firestore
        fetchUsername();

    }

    private void fetchUsername() {
        String uid = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (uid != null) {
            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    tvGreeting.setText("Hai, " + (username != null ? username : "Someone"));
                } else {
                    tvGreeting.setText("Hai, Someone");
                }
            }).addOnFailureListener(e -> {
                tvGreeting.setText("Hai, Someone");
                Log.e("Firestore", "Gagal mengambil username", e);
            });
        } else {
            tvGreeting.setText("Hai, Someone");
        }
    }

    private void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            Log.d("ConsultationActivity", "Menu item dipilih: " + id);

            if (id == R.id.nav_home) {
                startActivity(new Intent(ConsultationActivity.this, HomeActivity.class));
                Log.d("ConsultationActivity", "Navigasi ke HomeActivity");
                return true;
            } else if (id == R.id.nav_consultation) {
                if (!(ConsultationActivity.this instanceof ConsultationActivity)) {
                    startActivity(new Intent(ConsultationActivity.this, ConsultationActivity.class));
                    Log.d("ConsultationActivity", "Navigasi ke ConsultationActivity");
                }
                return true;
            } else if (id == R.id.nav_community) {
                startActivity(new Intent(ConsultationActivity.this, CommunityActivity.class));
                Log.d("ConsultationActivity", "Navigasi ke CommunityActivity");
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(ConsultationActivity.this, ProfileActivity.class));
                Log.d("ConsultationActivity", "Navigasi ke ProfileActivity");
                return true;
            }

            return false;
        });
    }

}
