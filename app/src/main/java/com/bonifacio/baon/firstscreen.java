package com.bonifacio.baon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class firstscreen extends AppCompatActivity {
    private MotionLayout motionLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firstscreen);

        motionLayout = findViewById(R.id.motionLayout);

        // Start the animation automatically with a delay
        motionLayout.post(new Runnable() {
            @Override
            public void run() {
                motionLayout.setTransition(R.id.start, R.id.end);
                motionLayout.transitionToEnd();
            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // open dashboard
                startActivity(new Intent(getApplicationContext(), EnterPinActivity.class));
            }

        }, 3000); // 3000 is the wait time, in milliseconds. 3000ms=3sec
    }

}