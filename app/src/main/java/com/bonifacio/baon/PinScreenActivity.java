package com.bonifacio.baon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PinScreenActivity extends AppCompatActivity {

    private EditText pinEditText;
    private Button[] numberButtons;
    private ImageView enterButton;
    private ImageView eraseButton;

    private static final int PIN_LENGTH = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinscreen);

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
                    String text = button.getText().toString();
                    if (pinEditText.length() < PIN_LENGTH) {
                        pinEditText.append(text);
                    }
                }
            });
        }
    }

    private void setEnterButtonListener() {
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = pinEditText.getText().toString();
                if (isValidPin(pin)) {
                    // Navigate to next activity
                    Intent intent = new Intent(PinScreenActivity.this, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PinScreenActivity.this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        return pin.length() == PIN_LENGTH;
    }
}