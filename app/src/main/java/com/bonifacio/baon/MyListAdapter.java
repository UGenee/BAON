package com.bonifacio.baon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> mList;

    public MyListAdapter(Context context, List<String> list) {
        super(context, 0, list);
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.note_list_item, parent, false);
            holder = new ViewHolder();
            holder.iconImageView = listItemView.findViewById(R.id.item_icon);
            holder.titleTextView = listItemView.findViewById(R.id.list_note_title);
            holder.subtitleTextView = listItemView.findViewById(R.id.list_date);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        // Get the current item from the list
        String currentItem = mList.get(position);

        // Set the text and icon of the views
        holder.titleTextView.setText(currentItem);
        holder.subtitleTextView.setText("Subtitle for " + currentItem);
        // Set your icon here, you can load from resources or URL
        holder.iconImageView.setImageResource(R.drawable.add);

        return listItemView;
    }

    static class ViewHolder {
        ImageView iconImageView;
        TextView titleTextView;
        TextView subtitleTextView;
    }
}