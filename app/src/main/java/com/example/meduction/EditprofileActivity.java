package com.example.meduction;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditprofileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private TextView btnChangeImage;
    private EditText etName, etEmail, etPhone, etBirthday, etDescription;
    private Button btnCancel, btnSave;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // Inisialisasi View
        imgProfile = findViewById(R.id.imgProfile);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etBirthday = findViewById(R.id.etBirthday);
        etDescription = findViewById(R.id.etDescription);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        // Cek apakah pengguna login menggunakan akun pihak ketiga (misalnya Google)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getProviderId().equals("google.com")) {
                // Jika login menggunakan Google, nonaktifkan atau sembunyikan elemen untuk mengedit email dan nama
                etName.setEnabled(false);
                etEmail.setEnabled(false);
                etName.setText(user.getDisplayName());  // Menampilkan nama pengguna yang terhubung dengan Google
                etEmail.setText(user.getEmail());      // Menampilkan email pengguna yang terhubung dengan Google

                // Menampilkan foto profil dari Google (dari FirebaseUser)
                Uri photoUrl = user.getPhotoUrl();
                if (photoUrl != null) {
                    Log.d("Profile", "Photo URL: " + photoUrl.toString());
                    Glide.with(EditprofileActivity.this)
                            .load(photoUrl)
                            .placeholder(R.drawable.sample_profile)  // Placeholder
                            .into(imgProfile);
                } else {
                    Log.d("Profile", "No photo URL found.");
                    imgProfile.setImageResource(R.drawable.sample_profile);
                }
            }
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Ambil data profil
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("phone");
                            String birthday = documentSnapshot.getString("birthday");
                            String about = documentSnapshot.getString("about");
                            String profileImageUrl = documentSnapshot.getString("profileImageUrl"); // URL gambar profil

                            // Set hint untuk EditText
                            etName.setHint(name != null ? name : "Masukkan nama");
                            etEmail.setHint(email != null ? email : "Masukkan email");
                            etPhone.setHint(phone != null ? phone : "Masukkan nomor telepon");
                            etBirthday.setHint(birthday != null ? birthday : "Pilih tanggal lahir");
                            etDescription.setHint(about != null ? about : "Masukkan deskripsi");

                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                Glide.with(EditprofileActivity.this)
                                        .load(profileImageUrl) // URL gambar profil
                                        .placeholder(R.drawable.sample_profile) // Gambar placeholder jika belum dimuat
                                        .into(imgProfile); // Menampilkan gambar ke ImageView
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(EditprofileActivity.this, "Gagal mengambil data profil", Toast.LENGTH_SHORT).show());
        }

        // Ganti Gambar Profil
        btnChangeImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar Profil"), PICK_IMAGE_REQUEST);
        });

        // Pilih Tanggal Lahir
        etBirthday.setOnClickListener(v -> showDatePicker());

        // Tombol Batal
        btnCancel.setOnClickListener(v -> finish()); // Menutup aktivitas

        // Tombol Simpan
        btnSave.setOnClickListener(v -> saveProfile());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Menampilkan gambar yang dipilih di ImageView
            ImageView imageView = findViewById(R.id.imgProfile); // Ganti dengan ID ImageView Anda
            imageView.setImageURI(selectedImageUri);

            Toast.makeText(this, "Gambar profil berhasil diubah!", Toast.LENGTH_SHORT).show();
        }
    }


    private void showDatePicker() {
        // Membuka dialog pemilih tanggal
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Set tanggal yang dipilih ke EditText
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etBirthday.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveProfile() {
        // Validasi input
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String birthday = etBirthday.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || birthday.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data yang wajib", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi nomor telepon (hanya angka, minimal 10 digit)
        if (!phone.matches("\\d{10,}")) {
            Toast.makeText(this, "Nomor telepon tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Menampilkan indikator proses
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan profil...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Mengambil UID pengguna yang sedang login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Mencegah pengeditan nama/email untuk pengguna pihak ketiga
            for (UserInfo userInfo : user.getProviderData()) {
                if (userInfo.getProviderId().equals("google.com")) {
                    name = user.getDisplayName(); // Mengunci nama asli
                    email = user.getEmail();      // Mengunci email asli
                    break;
                }
            }

            // Data yang akan disimpan
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("email", email); // Email jika ingin menyimpan di Firestore juga
            userData.put("phone", phone);
            userData.put("birthday", birthday);
            userData.put("about", description); // Deskripsi

            // Menyimpan data ke Firestore
            FirebaseFirestore.getInstance().collection("users").document(userId).update(userData)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditprofileActivity.this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        finish(); // Kembali ke activity sebelumnya setelah sukses
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditprofileActivity.this, "Gagal memperbarui profil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Pengguna tidak ditemukan, harap login kembali", Toast.LENGTH_SHORT).show();
        }
    }

}
