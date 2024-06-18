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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
=======
<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> e209816b0977861b78676d0cf713618bda80803b
>>>>>>> 03dff656d6cf0ae73c00930fa3257cf8f820cdbc
public class records extends AppCompatActivity implements View.OnClickListener {

    private Button btnCancel, btnSave, btnExpense, btnIncome, delete;
    private EditText budget, notes;
    private Spinner spinner;
    private DatabaseHandler db;
    private boolean mIsViewingOrUpdating;
    private tbl_Entry EntryNote;
    private long DateTime;
    private boolean isIncome = true;  // Default to Income
<<<<<<< HEAD
=======
    private TextView categoryTextView;  // Reference to the category TextView
>>>>>>> e209816b0977861b78676d0cf713618bda80803b

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
<<<<<<< HEAD
=======
        categoryTextView = findViewById(R.id.category);  // Reference to the category TextView
>>>>>>> e209816b0977861b78676d0cf713618bda80803b

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
                if (!isFormatting &&!s.toString().startsWith("₱")) {
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
                    if (s.length() > 0 &&!s.toString().startsWith("₱")) {
                        s.insert(0, "₱");
                    }
                    isFormatting = false;
                }
            }
        });

        // Retrieve entry ID from intent
        String entryId = getIntent().getStringExtra("Id");
        if (entryId != null && !entryId.isEmpty()) {
            int id = Integer.parseInt(entryId);
            db = new DatabaseHandler(getApplicationContext());
            EntryNote = db.getEntry(id);
            if (EntryNote != null) {
                delete.setVisibility(View.VISIBLE);
                budget.setText("₱" + EntryNote.getEntryTitle());
                notes.setText(EntryNote.getContent());
                DateTime = EntryNote.getDate();
                mIsViewingOrUpdating = true;

                // Set the spinner selection to the existing category
                String category = EntryNote.getCategory();
                ArrayAdapter<CharSequence> adapterspinner = (ArrayAdapter<CharSequence>) spinner.getAdapter();
                int categoryPosition = adapterspinner.getPosition(category);
                spinner.setSelection(categoryPosition);
<<<<<<< HEAD

                // Set the text color and underline based on the existing category
                if (category.equalsIgnoreCase("Income")) {
                    budget.setTextColor(Color.GREEN);
                    btnIncome.setBackgroundResource(R.drawable.underline);
                    btnExpense.setBackgroundResource(R.drawable.no_underline);
                    isIncome = true;
                } else {
                    budget.setTextColor(Color.RED);
                    btnIncome.setBackgroundResource(R.drawable.no_underline);
                    btnExpense.setBackgroundResource(R.drawable.underline);
                    isIncome = false;
                }
=======
<<<<<<< HEAD
=======
                categoryTextView.setText(EntryNote.getCategory());  // Set the category TextView
>>>>>>> e209816b0977861b78676d0cf713618bda80803b
>>>>>>> 222e0dca437266ab5fb5cb9c2c4f0457161c758c
            }
        } else {
            delete.setVisibility(View.GONE);
            DateTime = System.currentTimeMillis();
            mIsViewingOrUpdating = false;
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
<<<<<<< HEAD
=======
            updateCategoryTextView();  // Update the category TextView
>>>>>>> e209816b0977861b78676d0cf713618bda80803b
        } else if (view == btnExpense) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expense_categories, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            budget.setTextColor(Color.RED);  // Set text color to red for expense
            btnIncome.setBackgroundResource(R.drawable.no_underline);
            btnExpense.setBackgroundResource(R.drawable.underline);
            isIncome = false;
<<<<<<< HEAD
        }
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
<<<<<<< HEAD
        String category = spinner.getSelectedItem().toString(); // Get the selected category from the spinner
=======
        String category = spinner.getSelectedItem().toString(); // Get selected category from spinner
=======
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
>>>>>>> e209816b0977861b78676d0cf713618bda80803b
>>>>>>> 03dff656d6cf0ae73c00930fa3257cf8f820cdbc

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
<<<<<<< HEAD
            EntryNote.setCategory(category); // Save the category
=======
            EntryNote.setCategory(category);
>>>>>>> e209816b0977861b78676d0cf713618bda80803b
            EntryNote.setDate(DateTime);
            db.updateEntry(EntryNote);
        } else {
            tbl_Entry entry = new tbl_Entry();
            entry.setEntryTitle(title);
            entry.setContent(content);
<<<<<<< HEAD
            entry.setCategory(category); // Save the category
=======
            entry.setCategory(category);
>>>>>>> e209816b0977861b78676d0cf713618bda80803b
            entry.setDate(DateTime);
            db.addEntry(entry);
        }

<<<<<<< HEAD
        Intent intent = new Intent();
        intent.putExtra("record_updated", true);
        // this will make it so that the spinner becomes the title for that listview
        intent.putExtra("category", category);
        setResult(RESULT_OK, intent);
=======
        Intent intent = new Intent(records.this, dashboard.class);
<<<<<<< HEAD
        intent.putExtra("record_updated", true);
        setResult(RESULT_OK, intent);
=======
        startActivity(intent);
>>>>>>> e209816b0977861b78676d0cf713618bda80803b
>>>>>>> 03dff656d6cf0ae73c00930fa3257cf8f820cdbc
        finish();
    }
}