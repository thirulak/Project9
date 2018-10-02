package com.example.android.inventoryapp;

/**
 * Created by Meenakshi on 9/24/2018.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    public void bindView(View view, final Context context, final Cursor cursor) {
        final int mQuantity;
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView buyTextView = (TextView) view.findViewById(R.id.buy);

        // Find the columns of Book attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
        final int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
        // Read the Book attributes from the Cursor for the current book
        final String BookName = cursor.getString(nameColumnIndex);
        final int BookQuantity = cursor.getInt(quantityColumnIndex);
        final String BookPrice = cursor.getString(priceColumnIndex);


        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(BookName);
        quantityTextView.setText(String.valueOf(BookQuantity));
        priceTextView.setText(BookPrice);
        // OnClickListener for Sale button
        // When clicked it reduces the number in stock by 1.
        final String id = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry._ID));

        buyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BookQuantity > 0) {
                    Uri currentBookUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, Long.parseLong(id));
                    ContentValues values = new ContentValues();
                    values.put(BookContract.BookEntry.COLUMN_QUANTITY, BookQuantity - 1);
                    context.getContentResolver().update(currentBookUri, values, null, null);
                    swapCursor(cursor);
                    // Check if out of stock to display toast
                    if (BookQuantity == 1) {
                        Toast.makeText(context, R.string.toast_out_of_stock, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}

