package com.bonifacio.baon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EnterPinActivity extends AppCompatActivity {

        private static final int PIN_LENGTH = 4;
        private EditText pinInput;
        private View btnEnter, btnErase;
        private View[] numberButtons;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_setpinscreen);

                pinInput = findViewById(R.id.n_pin_pass);
                btnEnter = findViewById(R.id.btn_enter);
                btnErase = findViewById(R.id.btn_erase);

                numberButtons = new View[]{
                        findViewById(R.id.btn_0),
                        findViewById(R.id.btn_1),
                        findViewById(R.id.btn_2),
                        findViewById(R.id.btn_3),
                        findViewById(R.id.btn_4),
                        findViewById(R.id.btn_5),
                        findViewById(R.id.btn_6),
                        findViewById(R.id.btn_7),
                        findViewById(R.id.btn_8),
                        findViewById(R.id.btn_9)
                };

                setupButtonListeners();

                btnEnter.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_check));
                btnErase.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_erase));
        }

        private void setupButtonListeners() {
                for (int i = 0; i < numberButtons.length; i++) {
                        int digit = i;
                        numberButtons[i].setOnClickListener(v -> appendDigitToPin(digit));
                }

                btnEnter.setOnClickListener(v -> {
                        String pin = pinInput.getText().toString();
                        if (pin.length() == PIN_LENGTH) {
                                savePin(pin);
                                setPinSetFlag(true);
                                navigateToConfirmPinActivity();
                        } else {
                                Toast.makeText(EnterPinActivity.this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                        }
                });

                btnErase.setOnClickListener(v -> pinInput.setText(""));
        }

        private void appendDigitToPin(int digit) {
                String currentPin = pinInput.getText().toString();
                if (currentPin.length() < PIN_LENGTH) {
                        currentPin += digit;
                        pinInput.setText(currentPin);
                }
        }

        private void savePin(String pin) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("pin", pin).apply();
        }

        private void setPinSetFlag(boolean isSet) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("isPinSet", isSet).apply();
        }

        private void navigateToConfirmPinActivity() {
                setPinSetFlag(true);
                Intent intent = new Intent(EnterPinActivity.this, ConfirmPinActivity.class);
                startActivity(intent);
                finish();
        }
}
