package com.bonifacio.baon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmPinActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmPinActivity";
    private static final int PIN_LENGTH = 4;

    private EditText pinInput;
    private String savedPin;

    private ImageView confirmButton, eraseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_pin_activity);

        // Load saved PIN from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        savedPin = sharedPreferences.getString("pin", "");

        pinInput = findViewById(R.id.n_pin_pass);
        pinInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(PIN_LENGTH)}); // Limit input to 4 characters

        // Initialize number buttons
        View btn0 = findViewById(R.id.btn_0);
        View btn1 = findViewById(R.id.btn_1);
        View btn2 = findViewById(R.id.btn_2);
        View btn3 = findViewById(R.id.btn_3);
        View btn4 = findViewById(R.id.btn_4);
        View btn5 = findViewById(R.id.btn_5);
        View btn6 = findViewById(R.id.btn_6);
        View btn7 = findViewById(R.id.btn_7);
        View btn8 = findViewById(R.id.btn_8);
        View btn9 = findViewById(R.id.btn_9);

        // Set up number button click listeners
        View[] numberButtons = new View[] {btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9};
        setupNumberButtonListeners(numberButtons);

        confirmButton = findViewById(R.id.btn_enter);
        eraseButton = findViewById(R.id.btn_erase);

        // Set up button click listeners
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPinValidity();
            }
        });

        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinInput.setText("");
            }
        });
    }

    private void setupNumberButtonListeners(View[] numberButtons) {
        for (int i = 0; i < numberButtons.length; i++) {
            final int digit = i;
            numberButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appendDigitToPin(digit);
                }
            });
        }
    }

    private void appendDigitToPin(int digit) {
        String currentPin = pinInput.getText().toString();
        if (currentPin.length() < PIN_LENGTH) {
            pinInput.append(String.valueOf(digit));
        }
    }

    private void checkPinValidity() {
        String inputPin = pinInput.getText().toString();
        if (verifyPin(inputPin)) {
            // Pin is correct, perform necessary actions
            Intent intent = new Intent(ConfirmPinActivity.this, PinScreenActivity.class);
            intent.putExtra("verified_pin", inputPin);
            startActivity(intent);
        } else {
            // Pin is incorrect, show error message or perform necessary actions
            Toast.makeText(this, "Pin is incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyPin(String inputPin) {

        return inputPin.equals(savedPin);
    }
}