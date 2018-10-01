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

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp.data.BookContract.BookEntry;

/**
 * Displays list of books that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 0;
    BookCursorAdapter mCursorAdapter;

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
        // Find the ListView which will be populated with the pet data
        ListView BookListView = (ListView) findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        BookListView.setEmptyView(emptyView);
        //setup up an adapter to create a list item for each row of Bookdata in the Cursor.
        //There is no Book data yet(until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new BookCursorAdapter(this, null);
        BookListView.setAdapter(mCursorAdapter);
        //setup item click listener
        BookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //create a new intent to go to {@Link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                //form the content URI that represents the specific Book that was clicked on
                //by appending the "id"(passed as input to this method)onto the
                //{@link BookEntry#CONTENT_URI}
                //for example the URI would be "content://com.example.android.books/books/2"
                //if the book with ID 2 is clicked on
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                //set the uri on the data field of the intent
                intent.setData(currentBookUri);
                //Launch the @link(EditorActivity) to display the data for the current Book
                startActivity(intent);
            }
        });
        //kick off the loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }
    public void onBuyClick(long id, int quantity) {
        Uri currentProductUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
        Log.v("CatalogActivity", "Uri: " + currentProductUri);
        quantity--;
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_QUANTITY, quantity);
        int rowsEffected = getContentResolver().update(currentProductUri, values, null, null);
    }


    // After the user has clicked Save in the Activity
    private void insertBook() {
        // Create a content values object where column names are the keys
        //and Books attributes are the values
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, "Harrypotter");
        values.put(BookEntry.COLUMN_PRODUCT_AUTHOR, "J.K.Rowling");
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, BookEntry.SUPPLIER_1);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE, "555-777-7788");
        values.put(BookEntry.COLUMN_QUANTITY, 3);
        values.put(BookEntry.COLUMN_PRICE, 300);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link BookEntry#CONTENT_URI} to indicate that we want to insert
        // into the Books database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }

    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
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
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_QUANTITY
        };
        //This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //{update @link BookCursorAdapter} with this new Cursor contining updated Booksdata
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //callback called when the data is need to be deleted
        mCursorAdapter.swapCursor(null);
    }
}


