package com.bonifacio.baon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class records extends AppCompatActivity implements View.OnClickListener {

    private Button btnCancel, btnSave, btnExpense, btnIncome, delete;
    private EditText budget, notes;
    private Spinner spinner;
    private DatabaseHandler db;
    private boolean mIsViewingOrUpdating;
    private String EntryName;
    private tbl_Entry EntryNote;
    private long DateTime;
    private boolean isIncome = true;  // Default to Income
    private TextView categoryTextView;  // Reference to the category TextView

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
        budget = findViewById(R.id.budget_edit);
        notes = findViewById(R.id.note_edit);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
        btnExpense = findViewById(R.id.expense);
        btnIncome = findViewById(R.id.income);
        delete = findViewById(R.id.delete);
        categoryTextView = findViewById(R.id.category);  // Reference to the category TextView

        // Set click listeners
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnExpense.setOnClickListener(this);
        btnIncome.setOnClickListener(this);
        delete.setOnClickListener(this);

        // Add TextWatcher to budget EditText
        budget.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;
            private int prevLength;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!isFormatting) {
                    prevLength = s.length();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isFormatting && !s.toString().startsWith("₱")) {
                    isFormatting = true;
                    String newText = "₱" + s.toString().replace("₱", "");
                    budget.setText(newText);
                    budget.setSelection(newText.length());
                    isFormatting = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isFormatting) {
                    isFormatting = true;
                    if (s.length() > 0 && !s.toString().startsWith("₱")) {
                        s.insert(0, "₱");
                    }
                    isFormatting = false;
                }
            }
        });

        EntryName = getIntent().getStringExtra("Id");
        if (EntryName != null && !EntryName.isEmpty()) {
            int id = Integer.parseInt(EntryName);
            db = new DatabaseHandler(getApplicationContext());
            EntryNote = db.getEntry(id);
            if (EntryNote != null) {
                delete.setVisibility(View.VISIBLE);
                budget.setText("₱" + EntryNote.getEntryTitle());
                notes.setText(EntryNote.getContent());
                DateTime = EntryNote.getDate();
                mIsViewingOrUpdating = true;

                int categoryPosition = adapter.getPosition(EntryNote.getCategory());
                spinner.setSelection(categoryPosition);
                categoryTextView.setText(EntryNote.getCategory());  // Set the category TextView
            }
        } else {
            delete.setVisibility(View.GONE);
            DateTime = System.currentTimeMillis();
            mIsViewingOrUpdating = false;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Diary");
        }

        // Set initial underline state
        btnIncome.setBackgroundResource(R.drawable.underline);
        btnExpense.setBackgroundResource(R.drawable.no_underline);

        // Set initial text color to green for Income
        budget.setTextColor(Color.GREEN);
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
        } else if (view == btnIncome) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.income_categories, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            budget.setTextColor(Color.GREEN);  // Set text color to green for income
            btnIncome.setBackgroundResource(R.drawable.underline);
            btnExpense.setBackgroundResource(R.drawable.no_underline);
            isIncome = true;
            updateCategoryTextView();  // Update the category TextView
        } else if (view == btnExpense) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expense_categories, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            budget.setTextColor(Color.RED);  // Set text color to red for expense
            btnIncome.setBackgroundResource(R.drawable.no_underline);
            btnExpense.setBackgroundResource(R.drawable.underline);
            isIncome = false;
            updateCategoryTextView();  // Update the category TextView
        }
    }

    private void updateCategoryTextView() {
        String selectedCategory = spinner.getSelectedItem().toString();
        categoryTextView.setText(selectedCategory);  // Set the category TextView to the selected category
    }

    private void actionCancel() {
        if (!checkNoteAltered()) {
            finish();
        } else {
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("Discard changes...")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("NO", null);
            dialogCancel.show();
        }
    }

    private void actionDelete() {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this)
                .setTitle("Delete note")
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
            return EntryNote != null && (!budget.getText().toString().equalsIgnoreCase("₱" + EntryNote.getEntryTitle()) || !notes.getText().toString().equalsIgnoreCase(EntryNote.getContent()));
        } else {
            return !budget.getText().toString().isEmpty() || !notes.getText().toString().isEmpty();
        }
    }

    private void validateAndSaveNote() {
        String title = budget.getText().toString().replace("₱", "").trim();
        String content = notes.getText().toString();
        String category = spinner.getSelectedItem().toString();  // Get the selected category from the Spinner

        if (title.isEmpty()) {
            Toast.makeText(records.this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EntryNote != null) {
            DateTime = EntryNote.getDate();
        } else {
            DateTime = System.currentTimeMillis();
        }

        db = new DatabaseHandler(getApplicationContext());

        if (EntryNote != null) {
            EntryNote.setEntryTitle(title);
            EntryNote.setContent(content);
            EntryNote.setCategory(category);
            EntryNote.setDate(DateTime);
            db.updateEntry(EntryNote);
        } else {
            tbl_Entry entry = new tbl_Entry();
            entry.setEntryTitle(title);
            entry.setContent(content);
            entry.setCategory(category);
            entry.setDate(DateTime);
            db.addEntry(entry);
        }

        Intent intent = new Intent(records.this, dashboard.class);
        startActivity(intent);
        finish();
    }
}
