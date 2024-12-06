package com.example.meduction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.meduction.community.ui.CommunityActivity;
import com.example.meduction.consultation.ui.ConsultationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private LinearLayout btnEditProfile;
    private TextView tvName, tvEmail, tvPhone, tvBirthday, tvAbout;
    private LinearLayout btnNotification, btnSettings, btnHelp, btnLogout;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Tetapkan item aktif pada BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // Panggil metode initListener untuk mengaktifkan listener navigasi
        initListener();

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Inisialisasi Views
        imgProfile = findViewById(R.id.imgProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvAbout = findViewById(R.id.tvAbout);
        btnNotification = findViewById(R.id.btnNotification);
        btnSettings = findViewById(R.id.btnSettings);
        btnHelp = findViewById(R.id.btnHelp);
        btnLogout = findViewById(R.id.btnLogout);

        // Ambil data pengguna
        loadUserProfile();

        // Set listener untuk tombol Logout
        btnLogout.setOnClickListener(v -> {
            firebaseAuth.signOut(); // Melakukan logout dari Firebase Auth
            Toast.makeText(ProfileActivity.this, "Logout berhasil", Toast.LENGTH_SHORT).show();

            // Pindah ke LoginActivity
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Menghapus aktivitas sebelumnya
            startActivity(intent);
            finish(); // Menutup aktivitas ProfileActivity
        });

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditprofileActivity.class);
            startActivity(intent);
        });


    }

    private void loadUserProfile() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // Ambil data dari Firebase Authentication
            String userId = user.getUid();

            // Set nama dan email langsung dari Firebase Authentication
            tvName.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());

            // Set foto profil jika ada
            if (user.getPhotoUrl() != null) {
                // Jika foto profil ada, ambil URL foto profil
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.sample_profile) // Gambar placeholder jika foto belum ada
                        .into(imgProfile);
            } else {
                // Jika tidak ada foto, tampilkan foto default
                imgProfile.setImageResource(R.drawable.sample_profile);
            }

            // Ambil data tambahan dari Firestore
            firestore.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Jika data "about" ada di Firestore
                            String about = documentSnapshot.getString("about");
                            tvAbout.setText(about != null && !about.isEmpty() ? about : "Tidak ada");

                            // Ambil tanggal lahir jika tersedia
                            String birthday = documentSnapshot.getString("birthday");
                            tvBirthday.setText(birthday != null && !birthday.isEmpty() ? birthday : "Belum diatur");
                        } else {
                            // Jika data Firestore tidak ditemukan, tambahkan data default
                            saveDefaultUserData(user);
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Gagal memuat data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }


    private void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                return true;
            } else if (id == R.id.nav_consultation) {
                startActivity(new Intent(ProfileActivity.this, ConsultationActivity.class));
                return true;
            } else if (id == R.id.nav_community) {
                startActivity(new Intent(ProfileActivity.this, CommunityActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                if (!(ProfileActivity.this instanceof ProfileActivity)) {
                    startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                }
                return true;
            }

            return false;
        });
    }

    private void saveDefaultUserData(FirebaseUser user) {
        String userId = user.getUid();

        // Data default yang akan disimpan
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", user.getDisplayName());
        userData.put("email", user.getEmail());
        userData.put("phone", user.getPhoneNumber());
        userData.put("birthday", "Belum diatur");
        userData.put("about", "Tidak ada");

        firestore.collection("users").document(userId).set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data pengguna berhasil disimpan", Toast.LENGTH_SHORT).show();
                    loadUserProfile(); // Muat ulang data setelah disimpan
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
