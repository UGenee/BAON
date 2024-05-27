package com.bonifacio.baon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "YourDatabaseName";
    private static final String TBL_ENTRY = "tbl_Entry";
    private static final String ENTRYID = "entryId";
    private static final String ENTRYTITLE = "entryTitle";
    private static final String CONTENT = "content";
    private static final String DATE = "date";
    private static final String CATEGORY = "category";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_ENTRY = "CREATE TABLE " + TBL_ENTRY + "("
                + ENTRYID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ENTRYTITLE + " TEXT,"
                + CONTENT + " TEXT,"
                + DATE + " TEXT,"
                + CATEGORY + " TEXT)";
        db.execSQL(CREATE_TABLE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ENTRY);
        // Create tables again
        onCreate(db);
    }

    public void addEntry(tbl_Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ENTRYTITLE, entry.getEntryTitle());
        values.put(CONTENT, entry.getContent());
        values.put(DATE, entry.getDate());
        values.put(CATEGORY, entry.getCategory());
        // Inserting Row
        db.insert(TBL_ENTRY, null, values);
        db.close(); // Closing database connection
    }

    public tbl_Entry getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TBL_ENTRY, new String[]{ENTRYID, ENTRYTITLE, CONTENT, DATE, CATEGORY},
                ENTRYID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        tbl_Entry entry = new tbl_Entry(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Long.parseLong(cursor.getString(3)),
                cursor.getString(4));
        // return note
        return entry;
    }

    public List<tbl_Entry> getAllEntries() {
        List<tbl_Entry> entryList = new ArrayList<tbl_Entry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_ENTRY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                tbl_Entry entry = new tbl_Entry();
                entry.setEntryId(Integer.parseInt(cursor.getString(0)));
                entry.setEntryTitle(cursor.getString(1));
                entry.setContent(cursor.getString(2));
                entry.setDate(Long.parseLong(cursor.getString(3)));
                entry.setCategory(cursor.getString(4));
                // Adding entry to list
                entryList.add(entry);
            } while (cursor.moveToNext());
        }
        // return entry list
        return entryList;
    }

    public int updateEntry(tbl_Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ENTRYTITLE, entry.getEntryTitle());
        values.put(CONTENT, entry.getContent());
        values.put(DATE, entry.getDate());
        values.put(CATEGORY, entry.getCategory());
        // updating row
        return db.update(TBL_ENTRY, values, ENTRYID + " = ?",
                new String[]{String.valueOf(entry.getEntryId())});
    }

    public void deleteEntry(tbl_Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_ENTRY, ENTRYID + " = ?",
                new String[]{String.valueOf(entry.getEntryId())});
        db.close();
    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + TBL_ENTRY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

}
