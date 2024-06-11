package com.bonifacio.baon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter<tbl_Entry> {

    Context context;
    public static final int WRAP_CONTENT_LENGTH = 50;

    public EntryAdapter(Context context, int resource, ArrayList<tbl_Entry> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_list_item, parent, false);
        }

        tbl_Entry note = getItem(position);

        if (note != null) {
            TextView title = convertView.findViewById(R.id.list_note_title);
            TextView date = convertView.findViewById(R.id.list_date);
            TextView content = convertView.findViewById(R.id.list_category);
            TextView category = convertView.findViewById(R.id.category);
            ImageView icon = convertView.findViewById(R.id.item_icon); // Add this line

            // Set title to whatever the user inputted in the budget_edit
            title.setText("₱" + note.getEntryTitle());

            // Set date
            date.setText(note.getDateTimeFormatted(context));

            // Correctly show preview of the content (not more than 50 char or more than one line!)
            int toWrap = WRAP_CONTENT_LENGTH;
            int lineBreakIndex = note.getContent().indexOf('\n');

            // Used to wrap/cut the content
            if (note.getContent().length() > WRAP_CONTENT_LENGTH || lineBreakIndex < WRAP_CONTENT_LENGTH) {
                if (lineBreakIndex < WRAP_CONTENT_LENGTH) {
                    toWrap = lineBreakIndex;
                }
                if (toWrap > 0) {
                    content.setText(note.getContent().substring(0, toWrap) + "...");
                } else {
                    content.setText(note.getContent());
                }
            } else {
                // If less than 50 chars, leave it as is
                content.setText(note.getContent());
            }

            // Set the category text
            category.setText(note.getCategory());

            // Set the text color based on the category
            if (note.getCategory().equalsIgnoreCase("Income")) {
                title.setTextColor(getContext().getResources().getColor(R.color.incomeColor)); // Green for Income
                content.setTextColor(getContext().getResources().getColor(R.color.incomeColor)); // Green for Income
            } else if (note.getCategory().equalsIgnoreCase("Expense")) {
                title.setTextColor(getContext().getResources().getColor(R.color.expenseColor)); // Red for Expense
                content.setTextColor(getContext().getResources().getColor(R.color.expenseColor)); // Red for Expense
            }

            // Set the image based on the category
            icon.setImageResource(SpinnerImages.getImageResource(note.getCategory()));
        }

        return convertView;
    }
}
