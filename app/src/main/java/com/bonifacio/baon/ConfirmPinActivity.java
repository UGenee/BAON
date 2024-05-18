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

    private static final int PIN_LENGTH = 4;
    private EditText pinInput;
    private String savedPin;
    private ImageView confirmButton, eraseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_pin_activity);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        savedPin = sharedPreferences.getString("pin", "");

        pinInput = findViewById(R.id.n_pin_pass);
        pinInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(PIN_LENGTH)});

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

        View[] numberButtons = new View[]{btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9};
        setupNumberButtonListeners(numberButtons);

        confirmButton = findViewById(R.id.btn_enter);
        eraseButton = findViewById(R.id.btn_erase);

        confirmButton.setOnClickListener(v -> checkPinValidity());
        eraseButton.setOnClickListener(v -> pinInput.setText(""));
    }

    private void setupNumberButtonListeners(View[] numberButtons) {
        for (int i = 0; i < numberButtons.length; i++) {
            final int digit = i;
            numberButtons[i].setOnClickListener(v -> appendDigitToPin(digit));
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
            navigateToPinScreenActivity();
        } else {
            Toast.makeText(this, "Pin is incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyPin(String inputPin) {
        return inputPin.equals(savedPin);
    }

    private void navigateToPinScreenActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isPinSet = sharedPreferences.getBoolean("isPinSet", false); // Get the flag
        if (isPinSet) {
            Intent intent = new Intent(ConfirmPinActivity.this, PinScreenActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "PIN is not yet set.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ConfirmPinActivity.this, EnterPinActivity.class));
        }
    }
}