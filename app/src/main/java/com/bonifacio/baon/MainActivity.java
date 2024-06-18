package com.bonifacio.baon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class MainActivity extends AppCompatActivity {

    private MotionLayout motionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen); // Use the splash screen layout

        motionLayout = findViewById(R.id.motionLayout);

        motionLayout.post(new Runnable() {
            @Override
            public void run() {
                motionLayout.setTransition(R.id.start, R.id.end);
                motionLayout.transitionToEnd();
            }
        });

        // Delay for 3 seconds and then check PIN and proceed to appropriate activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPinAndStartActivity();
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }

    private void checkPinAndStartActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isPinSet = sharedPreferences.getBoolean("isPinSet", false);

        Intent intent;
        if (isPinSet) {
            intent = new Intent(MainActivity.this, PinScreenActivity.class);
        } else {
            intent = new Intent(MainActivity.this, EnterPinActivity.class);
        }
        startActivity(intent);
        finish(); // Finish the splash screen activity so it cannot be returned to
    }
}
