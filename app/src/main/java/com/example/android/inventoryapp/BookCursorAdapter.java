package com.example.android.inventoryapp;

/**
 * Created by Meenakshi on 9/24/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.BookContract;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }
    /**
     * This method binds the Book data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        // Find the columns of Book attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
        // Read the Book attributes from the Cursor for the current pet
        String BookName = cursor.getString(nameColumnIndex);
        String BookQuantity = cursor.getString(quantityColumnIndex);
        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(BookQuantity)) {
            BookQuantity = context.getString(R.string.unknown_supplier);
        }
        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(BookName);
        summaryTextView.setText(BookQuantity);
    }
}

