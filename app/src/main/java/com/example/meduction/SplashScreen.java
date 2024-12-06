package com.example.meduction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meduction.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Delay for 2 seconds before transitioning to the next screen (e.g., LoginActivity or HomeActivity)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent to navigate to the next activity (e.g., LoginActivity)
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class); // Change LoginActivity to your next activity
                startActivity(intent);
                finish(); // Finish MainActivity so the user can't go back to the splash screen
            }
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}