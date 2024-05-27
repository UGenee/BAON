package com.bonifacio.baon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.note_list_item, null);
        }

        tbl_Entry note = getItem(position);

        if(note != null) {
            TextView title = (TextView) convertView.findViewById( R.id.list_note_title);
            TextView date = (TextView) convertView.findViewById( R.id.list_date);
            TextView content = convertView.findViewById(R.id.list_category);
            TextView category = convertView.findViewById(R.id.category); // Correct ID for category

            title.setText(note.getEntryTitle());
            date.setText(note.getDateTimeFormatted(context) + "");

            // Correctly show preview of the content (not more than 50 char or more than one line!)
            int toWrap = WRAP_CONTENT_LENGTH;
            int lineBreakIndex = note.getContent().indexOf('\n');

            // Used to wrap/cut the content
            if(note.getContent().length() > WRAP_CONTENT_LENGTH || lineBreakIndex < WRAP_CONTENT_LENGTH) {
                if(lineBreakIndex < WRAP_CONTENT_LENGTH) {
                    toWrap = lineBreakIndex;
                }
                if(toWrap > 0) {
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
        }

        return convertView;
    }

}
