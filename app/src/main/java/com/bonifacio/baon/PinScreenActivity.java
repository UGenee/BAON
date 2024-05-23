package com.bonifacio.baon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PinScreenActivity extends AppCompatActivity {

    private static final int PIN_LENGTH = 4;
    private EditText pinEditText;
    private Button[] numberButtons;
    private ImageView enterButton;
    private ImageView eraseButton;
    private String savedPin;

    private View[] pinCircles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinscreen);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        savedPin = sharedPreferences.getString("pin", "");

        pinEditText = findViewById(R.id.n_pin_pass);
        numberButtons = new Button[]{
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

        setupNumberButtonListeners();
        setupEnterButtonListener();
        setupEraseButtonListener();

        pinCircles = new View[]{
                findViewById(R.id.circle1),
                findViewById(R.id.circle2),
                findViewById(R.id.circle3),
                findViewById(R.id.circle4)
        };

        pinEditText.addTextChangedListener(new TextWatcher() { // For the input & erase
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePinCircles(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void updatePinCircles(int length) { // For the circle UI
        for (int i = 0; i < pinCircles.length; i++) {
            if (i < length) {
                pinCircles[i].setBackgroundResource(R.drawable.circle_filled);
            } else {
                pinCircles[i].setBackgroundResource(R.drawable.circle_empty);
            }
        }
    }

    private void setupNumberButtonListeners() {
        for (Button button : numberButtons) {
            button.setOnClickListener(v -> {
                Button btn = (Button) v;
                String text = btn.getText().toString();
                if (pinEditText.length() < PIN_LENGTH) {
                    pinEditText.append(text);
                }
            });
        }
    }

    private void setupEnterButtonListener() {
        enterButton.setOnClickListener(v -> {
            String pin = pinEditText.getText().toString();
            if (isValidPin(pin)) {
                if (pin.equals(savedPin)) {
                    setPinSetFlag(true);
                    Intent intent = new Intent(PinScreenActivity.this, dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    pinEditText.setText("");
                    Toast.makeText(PinScreenActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PinScreenActivity.this, "PIN must be " + PIN_LENGTH + " digits", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPinSetFlag(boolean isSet) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isPinSet", isSet).apply();
    }

    private void setupEraseButtonListener() {
        eraseButton.setOnClickListener(v -> pinEditText.setText(""));
    }

    private boolean isValidPin(String pin) {
        return pin.length() == PIN_LENGTH;
    }
}
