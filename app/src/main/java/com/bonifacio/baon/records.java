package com.bonifacio.baon;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;

public class records extends AppCompatActivity implements View.OnClickListener {

    private android.database.sqlite.SQLiteDatabase SQLiteDatabase;
    private android.widget.Button Button;
    private android.widget.EditText EditText;
    private android.widget.Spinner Spinner;
    EditText budget, notes = (EditText);
    Button btnCancel, btnSave = (Button);
    SQLiteDatabase db = (SQLiteDatabase);
    Spinner spinner = (Spinner);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records);

        //Spinner
        spinner = findViewById(R.id.spin_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        budget = findViewById(R.id.budget_edit);
        notes = findViewById(R.id.note_edit);

        btnCancel = findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(this);
        btnSave = findViewById(R.id.btn_save);
            btnSave.setOnClickListener(this);


            db = openOrCreateDatabase ("recordDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS record (budget_input VARCHAR, note_input VARCHAR);");


    }
        @Override
        public void onClick(View view) {
            if (view == btnCancel) {
                finish();
                Toast.makeText(records.this, "Canceled.", Toast.LENGTH_SHORT).show();
            } else if (view == btnSave) {

            }

    }

}

