package com.example.android.inventoryapp;

/**
 * Created by Meenakshi on 9/22/2018.
 */


import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.BookContract.BookEntry;

/**
 * Allows user to create a new book or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field to enter the book's name
     */
    private EditText mProductNameEditText;

    /**
     * EditText field to enter the book's author
     */
    private EditText mProductAuthorEditText;

    /**
     * EditText field to enter the book's supplier
     */
    private Spinner mSupplierSpinner;

    /**
     * EditText field to enter the book's phone
     */
    private EditText mSupplierPhoneEditText;

    /**
     * EditText field to enter the book's quantity
     */
    private EditText mQuantityEditText;

    /**
     * EditText field to enter the book's price
     */
    private EditText mPriceEditText;

    /**
     * Values for validation
     */
    private String productNameString;
    private String productAuthorString;
    private String supplierPhoneString;
    private int quantityInt;
    private int priceInt;

    /**
     * Supplier of the book. The possible valid values are in the BookContract.java file:
     * {@link BookEntry#SUPPLIER_SELECT}, {@link BookEntry#SUPPLIER_1}, {@link BookEntry#SUPPLIER_2
     * {@link BookEntry#SUPPLIER_3}, {@link BookEntry#SUPPLIER_4}.
     */
    private int mSupplierName = BookEntry.SUPPLIER_SELECT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mProductNameEditText = findViewById(R.id.edit_product_name);
        mProductAuthorEditText = findViewById(R.id.edit_product_author);
        mSupplierSpinner = findViewById(R.id.spinner_supplier);
        mSupplierPhoneEditText = findViewById(R.id.edit_supplier_phone);
        mQuantityEditText = findViewById(R.id.edit_product_quantity);
        mPriceEditText = findViewById(R.id.edit_product_price);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the supplier of the book.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter supplierSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        supplierSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierSpinner.setAdapter(supplierSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_1))) {
                        mSupplierName = BookEntry.SUPPLIER_1;
                    } else if (selection.equals(getString(R.string.supplier_2))) {
                        mSupplierName = BookEntry.SUPPLIER_2;
                    } else if (selection.equals(getString(R.string.supplier_3))) {
                        mSupplierName = BookEntry.SUPPLIER_3;
                    } else if (selection.equals(getString(R.string.supplier_4))) {
                        mSupplierName = BookEntry.SUPPLIER_4;
                    } else {
                        mSupplierName = BookEntry.SUPPLIER_SELECT;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplierName = BookEntry.SUPPLIER_SELECT;
            }
        });
    }

    /**
     * Get user input from editor and save new book into the database
     */
    private void insertBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String productNameString = mProductNameEditText.getText().toString().trim();
        String productAuthorString = mProductAuthorEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        int quantityInt = Integer.parseInt(quantityString);
        String priceString = mPriceEditText.getText().toString().trim();
        int priceInt = Integer.parseInt(priceString);


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, productNameString);
        values.put(BookEntry.COLUMN_PRODUCT_AUTHOR, productAuthorString);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, mSupplierName);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);
        values.put(BookEntry.COLUMN_QUANTITY, quantityInt);
        values.put(BookEntry.COLUMN_PRICE, priceInt);

        // Insert a new pet into the provider, returning the content URI for the new pet.
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_Book_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_Book_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // save with field validation
                if (validation()) {
                    Toast.makeText(getApplicationContext(), R.string.toast_saving_product_full, Toast.LENGTH_LONG).show();
                    // Save book to database
                    insertBook();
                    // Exit activity
                    finish();
                    return true;
                } else {
                    // Update toast with error message
                    Toast.makeText(getApplicationContext(), R.string.toast_error_saving_product_full, Toast.LENGTH_LONG).show();
                }
                // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * General validation that checks if all required fields are filled in before
     * saving.
     *
     * @return true if pass, false if fail.
     */

    public boolean validation() {
        // Get text from editor and trim off any leading white space
        productNameString = mProductNameEditText.getText().toString().trim();
        productAuthorString = mProductAuthorEditText.getText().toString().trim();
        supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        // check if Quantity has invalid ""
        try {
            quantityInt = Integer.parseInt(mQuantityEditText.getText().toString().trim());
        } catch (NumberFormatException ex) { // handle your exception
            quantityInt = -1;
        }

        // Check if price has invalid ""
        try {
            priceInt = Integer.parseInt(mPriceEditText.getText().toString().trim());
        } catch (NumberFormatException ex) { // handle your exception
            priceInt = -1;
        }

        // Check required fields are filled in
        return !productNameString.equals("") &&
                // No author specified
                !productAuthorString.equals("") &&
                // No quantity specified
                quantityInt != -1 &&
                // no price specified
                priceInt != -1 &&
                // no supplier selected
                mSupplierName != 0;
    }
}