package com.bonifacio.baon;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class dashboard extends AppCompatActivity {

    private android.widget.ImageView ImageView;
    private android.widget.TextView TextView;
    TextView balanceTextView = (TextView);

    ImageView btnAdd = (ImageView);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        //Add_Button


        balanceTextView = findViewById(R.id.balance);

        Intent intent = getIntent();
        String budgetInput = intent.getStringExtra("BUDGET_INPUT");

        balanceTextView.setText(budgetInput);


        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this, records.class));
            }
        });
    }


}