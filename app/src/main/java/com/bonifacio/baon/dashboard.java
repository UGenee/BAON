package com.bonifacio.baon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
<<<<<<< HEAD
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
        // Format the selected date as "MMMM, yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
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
<<<<<<< HEAD
                return Long.compare(rhs.getDate(), lhs.getDate());
=======
                Date left = new Date(lhs.getDate());
                Date right = new Date(rhs.getDate());
                return right.compareTo(left);
=======
>>>>>>> e209816b0977861b78676d0cf713618bda80803b
            }
        });

        // Update visibility of NoRecords TextView based on entries size
        if (entries != null && entries.size() > 0) {
            // Prepare adapter for customized ListView
            final EntryAdapter ea = new EntryAdapter(getApplicationContext(), R.layout.note_list_item, (ArrayList<tbl_Entry>) entries);
            listView.setAdapter(ea);
            listView.setVisibility(View.VISIBLE);

            // If an item of listView is clicked
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get its ID
                    int Id = ((tbl_Entry) listView.getItemAtPosition(position)).getEntryId();
                    // Open CreateActivity, passing the retrieved ID
                    Intent viewNoteIntent = new Intent(getApplicationContext(), records.class);
                    viewNoteIntent.putExtra("Id", Id + "");
                    startActivityForResult(viewNoteIntent, RECORDS_REQUEST_CODE); // Modify this line
                }
            });
            // Hide NoRecords TextView
            noRecordsTextView.setVisibility(View.GONE);
        } else {
            // If no entries found, hide the list view or show a message
            listView.setVisibility(View.GONE);
            noRecordsTextView.setVisibility(View.VISIBLE);
        }

        // Calculate and update the total and expenses
        updateTotal(day, month, year);
    }

<<<<<<< HEAD
    private void updateTotal(Integer day, Integer month, Integer year) {
        // Get all entries from the database
        List<tbl_Entry> entries = db.getAllEntries();

        // Calculate the total sum of the budget entries and total expenses for the selected date
        double totalAmount = 0.0;
        double totalExpenses = 0.0;
        boolean hasIncome = false;
        boolean hasExpense = false;

        for (tbl_Entry entry : entries) {
            Calendar entryCalendar = Calendar.getInstance();
            entryCalendar.setTime(new Date(entry.getDate()));
            if (entryCalendar.get(Calendar.DAY_OF_MONTH) == day &&
                    entryCalendar.get(Calendar.MONTH) == month &&
                    entryCalendar.get(Calendar.YEAR) == year) {
                String amountString = entry.getEntryTitle();
                try {
                    double amount = Double.parseDouble(amountString);

                    if (entry.getCategory().equalsIgnoreCase("Expense")) {
                        totalExpenses += amount;
                        hasExpense = true;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // Format the total with the Peso sign and set it to the total TextView
        String formattedTotal = String.format("₱%.2f", totalAmount);
        totalTextView.setText(formattedTotal);

        // Format the expenses with the Peso sign and set it to the expense TextView
        String formattedExpenses = String.format("₱%.2f", totalExpenses);
        expenseTextView.setText(formattedExpenses);

=======
    @Override
    protected void onResume() {
        super.onResume();

        // Access the database
        db = new DatabaseHandler(getApplicationContext());
        loadEntries(selectedDate.get(Calendar.DAY_OF_MONTH), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.YEAR));
    }

    private void updateDateTextView() {
        // Format the selected date as "MMMM yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String dateString = dateFormat.format(selectedDate.getTime());
        dateTextView.setText(dateString);
    }

    private void showDatePickerDialog() {
        // Create and show DatePickerDialog with the selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Update the calendar with the selected date
                        selectedDate.set(Calendar.YEAR, selectedYear);
                        selectedDate.set(Calendar.MONTH, selectedMonth);
                        selectedDate.set(Calendar.DAY_OF_MONTH, selectedDay);

                        // Update the TextView with the selected date
                        updateDateTextView();

                        // Load entries for the selected day, month, and year
                        loadEntries(selectedDay, selectedMonth, selectedYear);

                        // Hide noRecordsTextView if it's the current day
                        Calendar currentCalendar = Calendar.getInstance();
                        if (selectedYear == currentCalendar.get(Calendar.YEAR) &&
                                selectedMonth == currentCalendar.get(Calendar.MONTH) &&
                                selectedDay == currentCalendar.get(Calendar.DAY_OF_MONTH)) {
                            noRecordsTextView.setVisibility(View.GONE);
                        }
                    }
                }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void loadEntries(Integer day, Integer month, Integer year) {
        listView.setAdapter(null);
        List<tbl_Entry> entries = db.getAllEntries();

        // Filter entries by selected day, month, and year if provided
        if (day != null && month != null && year != null) {
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
        }

        // Sort the list according to date in reverse chronological order
        Collections.sort(entries, new Comparator<tbl_Entry>() {
            @Override
            public int compare(tbl_Entry lhs, tbl_Entry rhs) {
                Date left = new Date(lhs.getDate());
                Date right = new Date(rhs.getDate());
                return right.compareTo(left);
>>>>>>> 03dff656d6cf0ae73c00930fa3257cf8f820cdbc
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
                    String entryTitle = entry.getEntryTitle();
                    // Check if the entryTitle contains only numeric characters
                    if (entryTitle.matches("\\d+(\\.\\d+)?")) {
                        double amount = Double.parseDouble(entryTitle);
                        if (entry.getCategory().equalsIgnoreCase(getString(R.string.category_allowance))) {
                            totalIncome += amount;
                        } else if (entry.getCategory().equalsIgnoreCase(getString(R.string.Food)) ||
                                entry.getCategory().equalsIgnoreCase(getString(R.string.Transport)) ||
                                entry.getCategory().equalsIgnoreCase(getString(R.string.Miscellaneous))) {
                            totalExpenses += amount;
                        }
                    } else {
                        Log.e("Error", "Invalid amount: " + entryTitle);
                    }
                } catch (NumberFormatException e) {
                    // Handle the exception, e.g., log the error or show a toast
                    Log.e("Error", "Invalid amount: " + entry.getEntryTitle());
                }
            }
        }

        String formattedIncome = String.format("+₱%.2f", totalIncome);
        totalTextView.setText(formattedIncome);

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 222e0dca437266ab5fb5cb9c2c4f0457161c758c
        // Set income color to green if totalIncome >= 0
        if (totalIncome >= 0) {
            totalTextView.setTextColor(Color.GREEN);
            Log.d("TotalIncome", "Total Income: " + totalIncome);
<<<<<<< HEAD
        } else {
            totalTextView.setTextColor(getResources().getColor(R.color.expenseColor));
        }

        String formattedExpenses = String.format("-₱%.2f", totalExpenses);
=======
=======
        // Format the expenses with the Peso sign and set it to the expense TextView
        String formattedExpenses = String.format("₱%.2f", totalExpenses);
>>>>>>> 222e0dca437266ab5fb5cb9c2c4f0457161c758c
        expenseTextView.setText(formattedExpenses);

>>>>>>> e209816b0977861b78676d0cf713618bda80803b
        // Set the text color based on the type of entries
        if (hasIncome && !hasExpense) {
            totalTextView.setTextColor(getResources().getColor(R.color.incomeColor)); // Green for Income
        } else if (!hasIncome && hasExpense) {
            totalTextView.setTextColor(getResources().getColor(R.color.expenseColor)); // Red for Expense
<<<<<<< HEAD
=======
        } else if (hasIncome && hasExpense) {
            totalTextView.setTextColor(getResources().getColor(R.color.incomeColor)); // Green for mixed entries
>>>>>>> e209816b0977861b78676d0cf713618bda80803b
>>>>>>> 03dff656d6cf0ae73c00930fa3257cf8f820cdbc
        } else {
            totalTextView.setTextColor(getResources().getColor(R.color.expenseColor));
        }

        String formattedExpenses = String.format("-₱%.2f", totalExpenses);
        expenseTextView.setText(formattedExpenses);

        double availableBalance = totalIncome - totalExpenses;
        String formattedBalance = String.format("₱%.2f", availableBalance);
        balanceTextView.setText(formattedBalance);

        // Set balance text color
        balanceTextView.setTextColor(getResources().getColor(R.color.white));
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
