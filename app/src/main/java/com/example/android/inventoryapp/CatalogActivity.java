package com.example.android.inventoryapp;
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.data.BookContract.BookEntry;
import com.example.android.inventoryapp.data.BookDbHelper;


/**
 * Displays list of books that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new BookDbHelper(this);
    }

    // After the user has clicked Save in the Activity
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the books database.
     */
    @SuppressLint("SetTextI18n")
    private void displayDatabaseInfo() {
        // Define my projection (column names)
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRODUCT_AUTHOR,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_PRICE
        };

        Cursor cursor = getContentResolver().query(
                BookEntry.CONTENT_URI, //the content URI
                projection,            //The column to return for  each row
                null,          //selection criteria
                null,       //selection criteria
                null);        //the sort order for returned rows

        // Display and log the table info e.g.: "The Books table contains 2 books."
        TextView displayView = findViewById(R.id.text_view_book);
        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String cursorText = getString(R.string.cursor_message, cursor.getCount());
        Log.v("CatalogActivity", cursorText);
        displayView.setText(cursorText);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The books table contains <number of rows in Cursor> books.
            // _id - title - author - supplier - phone - quantity - price
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The Books table contains " + cursor.getCount() + " Book.\n\n");
            displayView.append("\n\n" + BookEntry._ID + " - " +
                    BookEntry.COLUMN_PRODUCT_NAME + " - " +
                    BookEntry.COLUMN_PRODUCT_AUTHOR + " - " +
                    BookEntry.COLUMN_SUPPLIER_NAME + " - " +
                    BookEntry.COLUMN_SUPPLIER_PHONE + " - " +
                    BookEntry.COLUMN_QUANTITY + " - " +
                    BookEntry.COLUMN_PRICE + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_AUTHOR);
            int supplierColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE);
            int weightColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentAuthor = cursor.getString(authorColumnIndex);
                int currentSupplier = cursor.getInt(supplierColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentAuthor + " - " +
                        convertSupplier(currentSupplier) + " - " +
                        currentPhone + " - " +
                        currentWeight + " - " +
                        currentPrice));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void insertBook() {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, "Harrypotter");
        values.put(BookEntry.COLUMN_PRODUCT_AUTHOR, "J.K.Rowling");
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, BookEntry.SUPPLIER_1);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE, "555-777-7788");
        values.put(BookEntry.COLUMN_QUANTITY, 3);
        values.put(BookEntry.COLUMN_PRICE, 300);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertBook();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method that converts Supplier's ID to its string value
     *
     * @param supplierId is the ID from the Supplier spinner
     * @return Supplier's full name
     */
    public String convertSupplier(int supplierId) {

        if (supplierId != 0) {
            if (supplierId == 1) {
                return getString(R.string.supplier_1);
            } else if (supplierId == 2) {
                return getString(R.string.supplier_2);
            } else if (supplierId == 3) {
                return getString(R.string.supplier_3);
            } else if (supplierId == 4) {
                return getString(R.string.supplier_4);
            } else {
                return getString(R.string.supplier_unknown);
            }
        } else {
            return getString(R.string.supplier_unknown);
        }
    }
}


