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
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;

public class records extends AppCompatActivity implements View.OnClickListener {

    private android.database.sqlite.SQLiteDatabase SQLiteDatabase;
    private android.widget.Button Button;
    private android.widget.EditText EditText;
    private android.widget.Spinner Spinner;
    EditText budget, notes = (EditText);
    Button btnCancel, btnSave, btnExpense, btnIncome, delete = (Button);
    DatabaseHandler db;
    Spinner spinner = (Spinner);

    private boolean mIsViewingOrUpdating;
    private String EntryName;
    private tbl_Entry EntryNote;
    private long DateTime;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records);

        //Spinner
        spinner = findViewById(R.id.spin_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.income_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        budget = findViewById(R.id.budget_edit);
        notes = findViewById(R.id.note_edit);

        btnCancel = findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(this);
        btnSave = findViewById(R.id.btn_save);
            btnSave.setOnClickListener(this);
        btnExpense = findViewById(R.id.expense);
            btnExpense.setOnClickListener(this);
        btnIncome = findViewById(R.id.income);
            btnIncome.setOnClickListener(this);
        delete = findViewById(R.id.delete);
            delete.setOnClickListener(this);

        EntryName = getIntent().getStringExtra("Id");
        if (EntryName != null && !EntryName.isEmpty()) {
            int id = Integer.parseInt(EntryName);
            db = new DatabaseHandler(getApplicationContext());
            EntryNote = db.getEntry(id);
            Log.d("STATUS", (EntryNote == null) + "");
            if (EntryNote != null) {
                // update the widgets from the loaded note
                delete.setVisibility(View.VISIBLE);
                budget.setText(EntryNote.getEntryTitle());
                notes.setText(EntryNote.getContent());
                DateTime = EntryNote.getDate();
                mIsViewingOrUpdating = true;
            }
        } else {
            // user wants to create a new note
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
        }
    }

    private void actionCancel() {
        if (!checkNoteAltered()) {
            // if note is not altered by user
            // (user only viewed the note/or did not write anything)
            finish(); // just exit the activity and go back to DashboardActivity
        } else {
            // we want to remind user to decide about
            // saving the changes or not, by showing a dialog
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("Discard changes...")
                    .setMessage("Are you sure you do not want to save " +
                            "changes to this note?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); // just go back to main activity
                        }
                    })
                    .setNegativeButton("NO", null); // null = stay in the activity!
            dialogCancel.show();
        }
    }
    private void actionDelete() {
        // ask user if he really wants to delete the note!
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
                .setNegativeButton("NO", null); // do nothing on clicking NO button :P

        dialogDelete.show();
    }

    /**
     * Handle cancel action
     */
    private boolean checkNoteAltered() {
        if (mIsViewingOrUpdating) { // if in view/update mode
            return EntryNote != null
                    && (!budget.getText().toString().equalsIgnoreCase(
                    EntryNote.getEntryTitle()) ||
                    !notes.getText().toString().equalsIgnoreCase(
                            EntryNote.getContent()));
        } else { // if in new note mode
            return !budget.getText().toString().isEmpty()
                    || !notes.getText().toString().isEmpty();
        }
    }

    /**
     * Validate the title and content and save the note
     * and finally exit the activity and go back to DashboardActivity
     */
    private void validateAndSaveNote() {
        String title = this.budget.getText().toString();
        String content = this.notes.getText().toString();

        // see if user has entered anything :D lol
        if (title.isEmpty()) { // title
            Toast.makeText(records.this, "Please enter a title."
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) { // content
            Toast.makeText(records.this, "Please enter a content. "
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        // set the creation time, if new note, now,
        // otherwise the loaded note's creation time
        if (EntryNote != null) {
            DateTime = EntryNote.getDate();
        } else {
            DateTime = System.currentTimeMillis();
        }

        // finally save the note!
        db = new DatabaseHandler(getApplicationContext());

        if (EntryNote != null) {
            EntryNote.setEntryTitle(this.budget.getText().toString());
            EntryNote.setContent(this.notes.getText().toString());
            EntryNote.setDate(DateTime);
            db.updateEntry(EntryNote);
            mIsViewingOrUpdating = true;
        } else {
            EntryNote = new tbl_Entry();
            EntryNote.setEntryTitle(this.budget.getText().toString());
            EntryNote.setContent(this.notes.getText().toString());
            EntryNote.setDate(DateTime);
            db.addEntry(EntryNote);
        }

        finish(); // exit the activity, should return us to DashboardActivity
    }


}

    /*
    @Override
        public void onClick(View view) {
            if (view == btnCancel) {
                finish();
            } else if (view == btnSave) {
                String budgetInput = budget.getText().toString();
                String noteInput = notes.getText().toString();

                if (budgetInput.isEmpty()) {
                    Toast.makeText(this, "Please put a budget.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(records.this, dashboard.class));
                }
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
    */





