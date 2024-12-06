package com.example.meduction;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meduction.community.ui.CommunityActivity;
import com.example.meduction.consultation.ui.ConsultationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class EdukasiDetailActivity extends AppCompatActivity {

    private RecyclerView materiRecyclerView;
    private EdukasiAdapter materiAdapter;
    private List<Item> materiEdukasiList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edukasi_detail);

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Tetapkan item aktif pada BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Panggil metode initListener untuk mengaktifkan listener navigasi
        initListener();

        // Initialize RecyclerView
        materiRecyclerView = findViewById(R.id.materiEdukasiRecyclerView);
        materiRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Populate materiEdukasiList with data
        materiEdukasiList.add(new Item("Gizi Seimbang", "Panduan mengatur pola makan sehat untuk menjaga kesehatan tubuh.", "5", "https://example.com/gizi_seimbang.jpg"));
        materiEdukasiList.add(new Item("Manajemen Stres", "Tips mengelola stres agar tetap sehat dan produktif.", "4", "https://example.com/manajemen_stres.jpg"));
        materiEdukasiList.add(new Item("Pencegahan Diabetes", "Langkah-langkah mencegah diabetes melalui pola hidup sehat.", "3", "https://example.com/pencegahan_diabetes.jpg"));

        // Set adapter and listener for item clicks
        materiAdapter = new EdukasiAdapter(materiEdukasiList, new EdukasiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                // Handle item click event
                Intent intent = new Intent(EdukasiDetailActivity.this, EdukasiDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("imageUrl", item.getImageUrl());
                startActivity(intent);
            }
        });

        materiRecyclerView.setAdapter(materiAdapter);
    }

    private void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.nav_home) {
                if (!(EdukasiDetailActivity.this instanceof EdukasiDetailActivity)) {
                    startActivity(new Intent(EdukasiDetailActivity.this, HomeActivity.class));
                }
                return true;
            } else if (id == R.id.nav_consultation) {
                startActivity(new Intent(EdukasiDetailActivity.this, ConsultationActivity.class));
                return true;
            } else if (id == R.id.nav_community) {
                startActivity(new Intent(EdukasiDetailActivity.this, CommunityActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(EdukasiDetailActivity.this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }
}
