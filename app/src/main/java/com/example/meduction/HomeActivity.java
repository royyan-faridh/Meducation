package com.example.meduction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.meduction.community.ui.CommunityActivity;
import com.example.meduction.consultation.ui.ConsultationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Perbaikan lengkap HomeActivity
public class HomeActivity extends AppCompatActivity {

    private RecyclerView beritaRecyclerView, edukasiRecyclerView;
    private BeritaAdapter beritaAdapter;
    private EdukasiAdapter edukasiAdapter;
    private List<Item> beritaList = new ArrayList<>();
    private List<Item> edukasiList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView tvGreeting;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Tetapkan item aktif pada BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Panggil metode initListener untuk mengaktifkan listener navigasi
        initListener();

        // Inisialisasi Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // TextView untuk menampilkan greeting
        tvGreeting = findViewById(R.id.tvGreeting);

        // Ambil username dari Firestore
        fetchUsername();

        // Setup RecyclerView berita
        beritaRecyclerView = findViewById(R.id.rvBerita);
        beritaRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        beritaAdapter = new BeritaAdapter(beritaList);
        beritaRecyclerView.setAdapter(beritaAdapter);

        // Fetch data berita
        fetchBeritaData();

        // Setup RecyclerView edukasi
        edukasiRecyclerView = findViewById(R.id.rvEdukasi);
        edukasiList.add(new Item("Gizi Seimbang", "Panduan mengatur pola makan sehat untuk menjaga kesehatan tubuh.", "5", "https://example.com/gizi_seimbang.jpg"));
        edukasiList.add(new Item("Manajemen Stres", "Tips mengelola stres agar tetap sehat dan produktif.", "4", "https://example.com/manajemen_stres.jpg"));
        edukasiList.add(new Item("Pencegahan Diabetes", "Langkah-langkah mencegah diabetes melalui pola hidup sehat.", "3", "https://example.com/pencegahan_diabetes.jpg"));
        edukasiList.add(new Item("Olahraga untuk Kesehatan", "Jenis olahraga yang mendukung kebugaran tubuh.", "6", "https://example.com/olahraga_kesehatan.jpg"));
        edukasiList.add(new Item("Higiene Pribadi", "Cara menjaga kebersihan tubuh untuk mencegah penyakit.", "2", "https://example.com/higiene_pribadi.jpg"));

        // Set adapter untuk RecyclerView edukasi
        edukasiAdapter = new EdukasiAdapter(edukasiList, item -> {
            // Ketika item diklik, buka EdukasiDetailActivity
            Intent intent = new Intent(HomeActivity.this, EdukasiDetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("imageUrl", item.getImageUrl());
            startActivity(intent);
        });
        edukasiRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        edukasiRecyclerView.setAdapter(edukasiAdapter);
    }

    private void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.nav_home) {
                if (!(HomeActivity.this instanceof HomeActivity)) {
                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                }
                return true;
            } else if (id == R.id.nav_consultation) {
                startActivity(new Intent(HomeActivity.this, ConsultationActivity.class));
                return true;
            } else if (id == R.id.nav_community) {
                startActivity(new Intent(HomeActivity.this, CommunityActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            }

            return false;
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

    private void fetchBeritaData() {
        String url = "https://jakpost.vercel.app/api/category/life/health";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Mengambil array "posts" dari response JSON
                        JSONArray posts = response.getJSONArray("posts");

                        // Clear beritaList agar tidak ada data duplikat
                        beritaList.clear();

                        for (int i = 0; i < posts.length(); i++) {
                            JSONObject post = posts.getJSONObject(i);

                            // Mengambil data dari setiap post
                            String title = post.getString("title");
                            String description = post.optString("headline", ""); // Mengambil headline sebagai deskripsi
                            String materialCount = post.optString("material_count", "0"); // Jumlah materi
                            String imageUrl = post.optString("image", ""); // Gambar artikel

                            // Menambahkan item berita ke daftar
                            beritaList.add(new Item(title, description, materialCount, imageUrl));
                        }

                        // Memberitahukan adapter untuk memperbarui tampilan
                        beritaAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(HomeActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(HomeActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    Log.e("VolleyError", "Error: " + error.getMessage());
                });

        // Menambahkan request ke antrean Volley
        Volley.newRequestQueue(this).add(request);
    }

}
