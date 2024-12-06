package com.example.meduction.community.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.meduction.R;

public class ThreadDetailActivity extends AppCompatActivity {

    private RecyclerView rvReplies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_detail);

        rvReplies = findViewById(R.id.rvReplies);
        rvReplies.setLayoutManager(new LinearLayoutManager(this));

        // Set Adapter for replies
    }
}
