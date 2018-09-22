package com.example.android.inventoryapp.data;

/**
 * Created by Meenakshi on 9/22/2018.
 */


import android.provider.BaseColumns;

/**
 * API Contract for the Books app.
 */
public final class BookContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BookContract() {
    }

    /**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single book.
     */
    public static final class BookEntry implements BaseColumns {

        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Title of the book.
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "product_name";

        /**
         * Author of the book.
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_AUTHOR = "product_author";

        /**
         * Supplier of the book.
         * <p>
         * The only possible values are {@link #SUPPLIER_SELECT}, {@link #SUPPLIER_1},
         * or {@link #SUPPLIER_2}.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";

        /**
         * Supplier Phone of the book.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";

        /**
         * Quantity of the book.
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * Price of the book.
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Possible values for the supplier of the book.
         */
        public static final int SUPPLIER_SELECT = 0;
        public static final int SUPPLIER_1 = 1;
        public static final int SUPPLIER_2 = 2;
        public static final int SUPPLIER_3 = 3;
        public static final int SUPPLIER_4 = 4;
    }

}

