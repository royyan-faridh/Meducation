package com.example.meduction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class MateriDetailActivity extends AppCompatActivity {

    private TextView title, description, materialCount;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi_detail);

        // Inisialisasi komponen UI
        title = findViewById(R.id.materiTitle);
        description = findViewById(R.id.materiDescription);
        materialCount = findViewById(R.id.materialCount);
        imageView = findViewById(R.id.materiImageView);

        // Mengambil data dari Intent
        Intent intent = getIntent();
        String materiTitle = intent.getStringExtra("title");
        String materiDescription = intent.getStringExtra("description");
        String materiCount = intent.getStringExtra("materialCount");
        String imageUrl = intent.getStringExtra("imageUrl");

        // Menampilkan data ke UI
        title.setText(materiTitle);
        description.setText(materiDescription);
        materialCount.setText("Jumlah Materi: " + materiCount);

        // Menampilkan gambar menggunakan Glide
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
    }
}

