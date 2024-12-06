package com.example.meduction.community.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meduction.HomeActivity;
import com.example.meduction.ProfileActivity;
import com.example.meduction.R;
import com.example.meduction.community.adapter.ThreadAdapter;
import com.example.meduction.community.models.Thread;
import com.example.meduction.consultation.ui.ConsultationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private RecyclerView rvThreadList;
    private ImageButton btnAddThread;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore firestore;
    private ThreadAdapter adapter;
    private List<Thread> threadList;
    private FirebaseAuth auth;
    private TextView tvGreeting;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // Inisialisasi view
        rvThreadList = findViewById(R.id.rvThreadList);
        btnAddThread = findViewById(R.id.btnAddThread);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Inisialisasi daftar thread dan adapter
        threadList = new ArrayList<>();
        adapter = new ThreadAdapter(this, threadList);
        rvThreadList.setLayoutManager(new LinearLayoutManager(this));
        rvThreadList.setAdapter(adapter);

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();

        // Memuat thread dari Firestore
        loadThreads();

        // Listener untuk tombol tambah thread
        btnAddThread.setOnClickListener(v -> {
            Intent intent = new Intent(CommunityActivity.this, AddThreadActivity.class);
            startActivity(intent);
        });

        // Mengatur navigasi bawah
        bottomNavigationView.setSelectedItemId(R.id.nav_community);
        initListener();

        auth = FirebaseAuth.getInstance();

        // TextView untuk menampilkan greeting
        tvGreeting = findViewById(R.id.tvGreeting);

        // Ambil username dari Firestore
        fetchUsername();
    }

    private void loadThreads() {
        db.collection("threads")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        threadList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Thread thread = document.toObject(Thread.class);
                            threadList.add(thread);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("CommunityActivity", "Gagal memuat data threads", task.getException());
                    }
                });
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
                startActivity(new Intent(CommunityActivity.this, HomeActivity.class));
                Log.d("ConsultationActivity", "Navigasi ke HomeActivity");
                return true;
            } else if (id == R.id.nav_consultation) {
                startActivity(new Intent(CommunityActivity.this, ConsultationActivity.class));
                Log.d("ConsultationActivity", "Navigasi ke CommunityActivity");
                return true;
            } else if (id == R.id.nav_community) {
                if (!(CommunityActivity.this instanceof CommunityActivity)) {
                    startActivity(new Intent(CommunityActivity.this, ConsultationActivity.class));
                    Log.d("ConsultationActivity", "Navigasi ke ConsultationActivity");
                }
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(CommunityActivity.this, ProfileActivity.class));
                Log.d("ConsultationActivity", "Navigasi ke ProfileActivity");
                return true;
            }

            return false;
        });
    }
}
