package com.example.meduction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meduction.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText edt_tanggal_lahir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        edt_tanggal_lahir = findViewById(R.id.edt_tanggal_lahir);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupAction();
    }

    private void setupAction() {
        // Navigate to LoginActivity when the login link is clicked
        binding.loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Register button action
        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString();
            String username = binding.edtUsername.getText().toString();
            String password = binding.edtPassword.getText().toString();
            String tanggalLahir = binding.edtTanggalLahir.getText().toString();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || tanggalLahir.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Lengkapi Data", Toast.LENGTH_SHORT).show();
            } else {
                registerWithEmail(email, username, password, tanggalLahir);
            }
        });

        edt_tanggal_lahir.setOnClickListener(v -> showDatePicker());

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
                    edt_tanggal_lahir.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    // Register user using Firebase Authentication
    private void registerWithEmail(String email, String username, String password, String tanggalLahir) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        saveUserToFirestore(user, username, tanggalLahir);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Gagal Membuat Akun: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    // Save user data to Firestore
    private void saveUserToFirestore(FirebaseUser user, String username, String tanggalLahir) {
        String uid = user.getUid();
        String email = user.getEmail();

        // Create user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("email", email != null ? email : "");
        userData.put("tanggal_lahir", tanggalLahir); // Tambahkan tanggal lahir

        // Save to Firestore
        db.collection("users").document(uid).set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Gagal menyimpan data pengguna", Toast.LENGTH_SHORT).show();
                });
    }

}
