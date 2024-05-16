package com.bonifacio.baon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class records extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase SQLiteDatabase;
    private Button btnCancel, btnSave, btnExpense, btnIncome, delete;
    private EditText budget, notes;
    private Spinner spinner;
    private DatabaseHandler db;
    private boolean mIsViewingOrUpdating;
    private String EntryName;
    private tbl_Entry EntryNote;
    private long DateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records);

        // Default spinner
        spinner = findViewById(R.id.spin_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.income_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Instantiation
        spinner = findViewById(R.id.spin_category);
        budget = findViewById(R.id.budget_edit);
        notes = findViewById(R.id.note_edit);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
        btnExpense = findViewById(R.id.expense);
        btnIncome = findViewById(R.id.income);
        delete = findViewById(R.id.delete);

        // Set click listeners
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnExpense.setOnClickListener(this);
        btnIncome.setOnClickListener(this);
        delete.setOnClickListener(this);

        EntryName = getIntent().getStringExtra("Id");
        if (EntryName != null && !EntryName.isEmpty()) {
            int id = Integer.parseInt(EntryName);
            db = new DatabaseHandler(getApplicationContext());
            EntryNote = db.getEntry(id);
            Log.d("STATUS", (EntryNote == null) + "");
            if (EntryNote != null) {
                delete.setVisibility(View.VISIBLE);
                budget.setText(EntryNote.getEntryTitle());
                notes.setText(EntryNote.getContent());
                DateTime = EntryNote.getDate();
                mIsViewingOrUpdating = true;
            }
        } else {
            delete.setVisibility(View.GONE);
            DateTime = System.currentTimeMillis();
            mIsViewingOrUpdating = false;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Diary");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_save) {
            validateAndSaveNote();
        } else if (id == R.id.btn_cancel) {
            actionCancel();
        } else if (id == R.id.delete) {
            actionDelete();
        } else if (view == btnCancel) {
            finish();
        } else if (view == btnIncome) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.income_categories, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else if (view == btnExpense) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expense_categories, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    private void actionCancel() {
        if (!checkNoteAltered()) {
            finish(); // No changes, directly finish the activity
        } else {
            // If changes are made, show the confirmation dialog
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("Discard changes...")
                    .setMessage("Are you sure you do not want to save changes to this note?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); // Finish the activity if user confirms
                        }
                    })
                    .setNegativeButton("NO", null);
            dialogCancel.show();
        }
    }


    private void actionDelete() {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this)
                .setTitle("Delete note")
                .setMessage("Really delete the note?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = new DatabaseHandler(getApplicationContext());
                        db.deleteEntry(EntryNote);
                        finish();
                    }
                })
                .setNegativeButton("NO", null);
        dialogDelete.show();
    }

    private boolean checkNoteAltered() {
        if (mIsViewingOrUpdating) {
            return EntryNote != null && (!budget.getText().toString().equalsIgnoreCase(EntryNote.getEntryTitle()) || !notes.getText().toString().equalsIgnoreCase(EntryNote.getContent()));
        } else {
            return !budget.getText().toString().isEmpty() || !notes.getText().toString().isEmpty();
        }
    }

    private void validateAndSaveNote() {
        String title = budget.getText().toString();
        String content = notes.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(records.this, "Please enter a title.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            Toast.makeText(records.this, "Please enter a content.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EntryNote != null) {
            DateTime = EntryNote.getDate();
        } else {
            DateTime = System.currentTimeMillis();
        }

        db = new DatabaseHandler(getApplicationContext());

        if (EntryNote != null) {
            EntryNote.setEntryTitle(budget.getText().toString());
            EntryNote.setContent(notes.getText().toString());
            EntryNote.setDate(DateTime);
            db.updateEntry(EntryNote);
            mIsViewingOrUpdating = true;
        } else {
            EntryNote = new tbl_Entry();
            EntryNote.setEntryTitle(budget.getText().toString());
            EntryNote.setContent(notes.getText().toString());
            EntryNote.setDate(DateTime);
            db.addEntry(EntryNote);
        }

        startActivity(new Intent(records.this, dashboard.class));
        finish();
    }
}
