package com.bonifacio.baon;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class pinscreen extends AppCompatActivity implements View.OnClickListener{
    private android.widget.ImageView ImageView;
    android.widget.ImageView btn_etr = (ImageView);
    private EditText pinEditText;
    private String inputPin = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinscreen);

        pinEditText = findViewById(R.id.n_pin_pass);
        btn_etr = findViewById(R.id.btn_enter);
        btn_etr.setOnClickListener(this);

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

            findViewById(R.id.btn_erase).setOnClickListener(view -> {
                if (inputPin.length() > 0) {
                    inputPin = inputPin.substring(0, inputPin.length() - 1);
                    pinEditText.setText(inputPin);
                    pinEditText.setSelection(inputPin.length());
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
    @Override
    public void onClick(View v) {
            if (inputPin.equals("1234")) {
            startActivity(new Intent(pinscreen.this, dashboard.class));
        } else {
            Toast.makeText(this, "Incorrect Pin!", Toast.LENGTH_SHORT).show();
            pinEditText.setText("");
        }

    }

}