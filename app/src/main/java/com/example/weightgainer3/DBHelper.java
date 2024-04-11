package com.example.weightgainer3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "weight_gainer.db";
    public static final int DATABASE_VERSION = 3;

    public static final String TABLE_DAYS = "days";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY_NAME = "day_name";
    public static final String COLUMN_TOTAL_KCAL = "total_kcal";
    public static final String COLUMN_TOTAL_PROTEIN = "total_protein";

    // Přidání nové tabulky pro sledování váhy
    public static final String TABLE_WEIGHTS = "weights";
    public static final String COLUMN_WEIGHT_ID = "_id";
    public static final String COLUMN_WEIGHT_DATE = "date";
    public static final String COLUMN_WEIGHT_VALUE = "value";

    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Vytvoření tabulky pro dny
        String createTableDaysQuery = "CREATE TABLE " + TABLE_DAYS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DAY_NAME + " TEXT, " +
                COLUMN_TOTAL_KCAL + " REAL, " +
                COLUMN_TOTAL_PROTEIN + " REAL" +
                ")";
        db.execSQL(createTableDaysQuery);

        // Vytvoření tabulky pro váhy
        String createTableWeightsQuery = "CREATE TABLE " + TABLE_WEIGHTS + " (" +
                COLUMN_WEIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WEIGHT_DATE + " TEXT, " +
                COLUMN_WEIGHT_VALUE + " REAL" +
                ")";
        db.execSQL(createTableWeightsQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Odstranění obou tabulek při aktualizaci
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHTS);
        onCreate(db);
    }
}


//zdroj chat gpt