package com.bonifacio.baon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetupPinActivity extends AppCompatActivity {

        private EditText pinEditText;
        private Button[] numberButtons;
        private ImageView enterButton;
        private ImageView eraseButton;

        private static final int PIN_LENGTH = 4;
        private static final String KEY_INPUT_PIN = "input_pin";
        private static final String CORRECT_PIN = "1234";
        private String inputPin;

        private boolean isFirstInstall = true;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.setpin);

                pinEditText = findViewById(R.id.n_pin_pass);
                numberButtons = new Button[] {
                        findViewById(R.id.btn_1),
                        findViewById(R.id.btn_2),
                        findViewById(R.id.btn_3),
                        findViewById(R.id.btn_4),
                        findViewById(R.id.btn_5),
                        findViewById(R.id.btn_6),
                        findViewById(R.id.btn_7),
                        findViewById(R.id.btn_8),
                        findViewById(R.id.btn_9),
                        findViewById(R.id.btn_0)
                };
                enterButton = findViewById(R.id.btn_enter);
                eraseButton = findViewById(R.id.btn_erase);

                if (savedInstanceState != null) {
                        inputPin = savedInstanceState.getString(KEY_INPUT_PIN);
                        pinEditText.setText(inputPin);
                }

                // Check if it's the first install
                isFirstInstall = getSharedPreferences("app_data", MODE_PRIVATE).getBoolean("first_install", true);

                if (isFirstInstall) {
                        // Show a message to set up the PIN
                        Toast.makeText(this, "Please set up your PIN", Toast.LENGTH_SHORT).show();
                }

                setNumberButtonListeners();
                setEnterButtonListener();
                setEraseButtonListener();
        }

        private void setNumberButtonListeners() {
                for (Button button : numberButtons) {
                        button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        Button button = (Button) v;
                                        String currentText = pinEditText.getText().toString();
                                        if (currentText.length() < PIN_LENGTH) {
                                                pinEditText.append(button.getText());
                                        }
                                }
                        });
                }
        }

        private void setEnterButtonListener() {
                enterButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        inputPin = pinEditText.getText().toString();
        if (isValidPin(inputPin)) {
                if (isFirstInstall) {
                        savePinAndStartPinscreenActivity();
        } else {
                if (inputPin.equals(CORRECT_PIN)) {
                Toast.makeText(SetupPinActivity.this, "PIN entered: " + inputPin, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SetupPinActivity.this, dashboard.class);
                intent.putExtra(KEY_INPUT_PIN, inputPin);
                startActivity(intent);
        } else {
        Toast.makeText(SetupPinActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
        pinEditText.setText("");
                }
        }
        } else {
                Toast.makeText(SetupPinActivity.this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                pinEditText.setText("");
                }
        }
                });
        }

        private void savePinAndStartPinscreenActivity() {
                savePinToDatabase(inputPin);
                Intent intent = new Intent(SetupPinActivity.this, pinscreen.class);
                startActivity(intent);
                finish();
        }
        private void setEraseButtonListener() {
                eraseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                pinEditText.setText("");
                        }
                });
        }

        private boolean isValidPin(String pin) {
                return pin.length() == PIN_LENGTH && pin.matches("\\d{4}");
        }

        private void savePinToDatabase(String pin) {
                // Implement your database saving logic here
                // For example, using SharedPreferences
                getSharedPreferences("app_data", MODE_PRIVATE).edit().putString("pin", pin).apply();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                outState.putString(KEY_INPUT_PIN, inputPin);
        }
}