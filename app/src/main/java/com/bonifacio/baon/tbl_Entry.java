package com.bonifacio.baon;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class tbl_Entry implements Serializable {
    public int entryId;
    public String entryTitle;
    public String content;
    public long date;
    public String category;

    public tbl_Entry() {
    }

    public tbl_Entry(String entryTitle, String content, long date, String category) {
        this.entryTitle = entryTitle;
        this.content = content;
        this.date = date;
        this.category = category;
    }

    public tbl_Entry(int entryId, String entryTitle, String content, long date, String category) {
        this.entryId = entryId;
        this.entryTitle = entryTitle;
        this.content = content;
        this.date = date;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getEntryTitle() {
        return entryTitle;
    }

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateTimeFormatted(Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy", context.getResources().getConfiguration().locale);
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(new Date(date));
    }
}