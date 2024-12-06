package com.example.meduction.community.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meduction.R;
import com.example.meduction.community.models.Thread;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddThreadActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Button btnSubmit, btnCancel;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thread);

        // Inisialisasi View
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancelSubmit);

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();

        // Listener untuk tombol Submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitThread();
            }
        });

        // Listener untuk tombol Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submitThread() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String author = "Anonymous"; // Bisa diganti dengan nama user yang login
        String date = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date());

        // Validasi input
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title dan Content tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat objek Thread
        Thread thread = new Thread(title, author, date, content);

        // Simpan ke Firestore
        db.collection("threads")
                .add(thread)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddThreadActivity.this, "Thread berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                    finish(); // Kembali ke activity sebelumnya
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddThreadActivity.this, "Gagal menambahkan thread: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
