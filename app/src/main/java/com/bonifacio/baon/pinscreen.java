package com.bonifacio.baon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class pinscreen extends AppCompatActivity {
    private EditText pinEditText;
    private String inputPin = "";
    private int attemptCount = 0;
    private final int MAX_ATTEMPTS = 3;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pinscreen);

            pinEditText = findViewById(R.id.n_pin_pass);

            // Set a TextWatcher to limit the input to 4 digits
            pinEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 4) {
                        inputPin = s.subSequence(0, 4).toString();
                        pinEditText.setText(inputPin);
                        pinEditText.setSelection(inputPin.length());
                    } else {
                        inputPin = s.toString();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            // Set OnClickListener for number buttons
            setNumberButtonClickListener((Button) findViewById(R.id.btn_1), "1");
            setNumberButtonClickListener((Button) findViewById(R.id.btn_2), "2");
            setNumberButtonClickListener((Button) findViewById(R.id.btn_3), "3");
            setNumberButtonClickListener((Button) findViewById(R.id.btn_4), "4");
            setNumberButtonClickListener((Button) findViewById(R.id.btn_5), "5");
            setNumberButtonClickListener((Button) findViewById(R.id.btn_6), "6");
            setNumberButtonClickListener((Button) findViewById(R.id.btn_7), "7");
            setNumberButtonClickListener((Button) findViewById(R.id.btn_8), "8");
            setNumberButtonClickListener((Button) findViewById(R.id.btn_9), "9");
            setNumberButtonClickListener((Button) findViewById(R.id.btn_0), "0");

            // Set OnClickListener for backspace button
            findViewById(R.id.btn_erase).setOnClickListener(view -> {
                if (inputPin.length() > 0) {
                    inputPin = inputPin.substring(0, inputPin.length() - 1);
                    pinEditText.setText(inputPin);
                    pinEditText.setSelection(inputPin.length());
                }
            });
            // Set OnClickListener for enter button
            ImageView enterButton = findViewById(R.id.btn_enter);
            enterButton.setOnClickListener(view -> {
                if (inputPin.length() == 4) {
                    attemptCount++;
                    if (attemptCount <= MAX_ATTEMPTS) {
                        // Perform your validation here
                        if (inputPin.equals("1234")) { // Replace with your desired pin
                            Toast.makeText(this, "Correct Pin!", Toast.LENGTH_SHORT).show();
                            // Reset the input and attempt count
                            inputPin = "";
                            pinEditText.setText("");
                            attemptCount = 0;
                        } else {
                            Toast.makeText(this, "Incorrect Pin! Attempts left: " + (MAX_ATTEMPTS - attemptCount), Toast.LENGTH_SHORT).show();
                            if (attemptCount == MAX_ATTEMPTS) {
                                enterButton.setEnabled(false);
                            }
                        }
                    } else {
                        Toast.makeText(this, "You have exceeded the maximum number of attempts. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter a 4-digit pin!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void setNumberButtonClickListener(Button button, String number) {
            button.setOnClickListener(view -> {
                if (inputPin.length() < 4) {
                    inputPin += number;
                    pinEditText.setText(inputPin);
                    pinEditText.setSelection(inputPin.length());
                }
            });
        }
}