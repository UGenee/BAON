package com.bonifacio.baon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isPinSet = sharedPreferences.getBoolean("isPinSet", false);

        if (isPinSet) {
            Intent intent = new Intent(MainActivity.this, PinScreenActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, EnterPinActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
