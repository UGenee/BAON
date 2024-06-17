package com.bonifacio.baon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class dashboard extends AppCompatActivity {

    private TextView balanceTextView, noRecordsTextView, dateTextView, totalTextView, expenseTextView;
    private ListView listView;
    private Calendar selectedDate;
    private DatabaseHandler db;

    private FloatingActionButton fab;
    private static final int RECORDS_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        balanceTextView = findViewById(R.id.balance);
        noRecordsTextView = findViewById(R.id.NoRecords);
        dateTextView = findViewById(R.id.date);
        totalTextView = findViewById(R.id.total);
        listView = findViewById(R.id.list_item);
        expenseTextView = findViewById(R.id.expense);

        // Set up FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), records.class), RECORDS_REQUEST_CODE); // Modify this line
            }
        });

        // Set the current date
        selectedDate = Calendar.getInstance();
        updateDateTextView();

        // Make the date TextView clickable
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Initialize database handler
        db = new DatabaseHandler(getApplicationContext());

        // Load entries for the current date
        loadEntries(selectedDate.get(Calendar.DAY_OF_MONTH), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.YEAR));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload entries on resume
        loadEntries(selectedDate.get(Calendar.DAY_OF_MONTH), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.YEAR));
    }

    private void updateDateTextView() {
        // Format the selected date as "MMMM dd, yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String dateString = dateFormat.format(selectedDate.getTime());
        dateTextView.setText(dateString);
    }

    private void showDatePickerDialog() {
        // Create and show DatePickerDialog with the selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update the selectedDate calendar with the selected date
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Update the date TextView with the selected date
                        updateDateTextView();

                        // Reload entries for the selected date
                        loadEntries(dayOfMonth, monthOfYear, year);
                    }
                },
                selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void loadEntries(Integer day, Integer month, Integer year) {
        List<tbl_Entry> entries = db.getAllEntries();

        // Filter entries by selected day, month, and year
        List<tbl_Entry> filteredEntries = new ArrayList<>();
        for (tbl_Entry entry : entries) {
            Calendar entryCalendar = Calendar.getInstance();
            entryCalendar.setTime(new Date(entry.getDate()));
            if (entryCalendar.get(Calendar.DAY_OF_MONTH) == day &&
                    entryCalendar.get(Calendar.MONTH) == month &&
                    entryCalendar.get(Calendar.YEAR) == year) {
                filteredEntries.add(entry);
            }
        }
        entries = filteredEntries;

        // Sort entries in reverse chronological order
        Collections.sort(entries, new Comparator<tbl_Entry>() {
            @Override
            public int compare(tbl_Entry lhs, tbl_Entry rhs) {
                return Long.compare(rhs.getDate(), lhs.getDate());
            }
        });

        // Update visibility of NoRecords TextView
        if (!entries.isEmpty()) {
            EntryAdapter adapter = new EntryAdapter(getApplicationContext(), R.layout.note_list_item, (ArrayList<tbl_Entry>) entries);
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            noRecordsTextView.setVisibility(View.GONE);

            // Handle click on ListView item
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int entryId = ((tbl_Entry) listView.getItemAtPosition(position)).getEntryId();
                    Intent viewNoteIntent = new Intent(getApplicationContext(), records.class);
                    viewNoteIntent.putExtra("Id", String.valueOf(entryId));
                    startActivityForResult(viewNoteIntent, RECORDS_REQUEST_CODE);
                }
            });
        } else {
            listView.setVisibility(View.GONE);
            noRecordsTextView.setVisibility(View.VISIBLE);
        }

        // Calculate and update total and expenses
        updateTotal(day, month, year);
    }

    private void updateTotal(Integer day, Integer month, Integer year) {
        List<tbl_Entry> entries = db.getAllEntries();
        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        for (tbl_Entry entry : entries) {
            Calendar entryCalendar = Calendar.getInstance();
            entryCalendar.setTime(new Date(entry.getDate()));
            if (entryCalendar.get(Calendar.DAY_OF_MONTH) == day &&
                    entryCalendar.get(Calendar.MONTH) == month &&
                    entryCalendar.get(Calendar.YEAR) == year) {
                try {
                    double amount = Double.parseDouble(entry.getEntryTitle());
                    if (entry.getCategory().equalsIgnoreCase(getString(R.string.category_allowance))) {
                        totalIncome += amount;
                    } else {
                        totalExpenses += amount;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        String formattedIncome = String.format("₱%.2f", totalIncome);
        totalTextView.setText(formattedIncome);

        String formattedExpenses = String.format("₱%.2f", totalExpenses);
        expenseTextView.setText(formattedExpenses);

        double availableBalance = totalIncome - totalExpenses;
        String formattedBalance = String.format("₱%.2f", availableBalance);
        balanceTextView.setText(formattedBalance);

        if (totalIncome > 0) {
            totalTextView.setTextColor(getResources().getColor(R.color.incomeColor));
        } else if (totalExpenses > 0) {
            totalTextView.setTextColor(getResources().getColor(R.color.expenseColor));
        } else {
            totalTextView.setTextColor(getResources().getColor(R.color.incomeColor));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORDS_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("record_updated", false)) {
                loadEntries(selectedDate.get(Calendar.DAY_OF_MONTH), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.YEAR));
            }
        }
    }
}
