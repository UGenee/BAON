package com.bonifacio.baon;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class dashboard extends AppCompatActivity {

    private android.widget.ImageView ImageView;
    private android.widget.ListView ListView;
    private android.widget.TextView TextView;
    TextView balanceTextView = (TextView);
    ImageView btnAdd = (ImageView);
    ListView listView = (ListView);

    DatabaseHandler db;

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


        listView = findViewById(R.id.list_item);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), records.class));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Access the database
        db = new DatabaseHandler(getApplicationContext());
        listView.setAdapter(null);
        ArrayList<tbl_Entry> entries = db.getAllEntry();

        // Sort the list according to date in reverse chronological order
        Collections.sort(entries, new Comparator<tbl_Entry>() {
            @Override
            public int compare(tbl_Entry lhs, tbl_Entry rhs) {
                Date left = new Date(lhs.getDate());
                Date right = new Date(rhs.getDate());
                if (left.before(right)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        // method call -> logic for the ttl bal & ttl expense

        // The list is not empty, so load content
        if (entries != null && entries.size() > 0) {
            // Prepare adapter for customized ListView
            final EntryAdapter ea = new EntryAdapter(getApplicationContext(), R.layout.note_list_item, entries);
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
                    startActivity(viewNoteIntent);
                }
            });
        }
    }


    }
