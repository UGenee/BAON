package com.bonifacio.baon;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class dashboard extends AppCompatActivity {


    private android.widget.TextView TextView;
    TextView balanceTextView = (TextView);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        balanceTextView = findViewById(R.id.balance);

        Intent intent = getIntent();
        String budgetInput = intent.getStringExtra("BUDGET_INPUT");

        balanceTextView.setText(budgetInput);
    }

}