package com.example.weightgainer3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class WeightTrackingActivity extends AppCompatActivity {
    private EditText weightEditText;
    private Button saveWeightButton, homeButton;
    private ListView weightListView;

    private SQLiteDatabase mDatabase;
    private DBHelper mDBHelper;
    private ArrayList<WeightEntry> weightList;
    private WeightAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracking);

        weightEditText = findViewById(R.id.weightEditText);
        saveWeightButton = findViewById(R.id.saveWeightButton);
        homeButton = findViewById(R.id.homeButton);
        weightListView = findViewById(R.id.weightListView);

        mDBHelper = new DBHelper(this);
        mDatabase = mDBHelper.getWritableDatabase();

        weightList = new ArrayList<>();
        mAdapter = new WeightAdapter(this, weightList);
        weightListView.setAdapter(mAdapter);

        saveWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWeight();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeightTrackingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        weightListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                WeightEntry selectedWeight = weightList.get(position);
                deleteWeight(selectedWeight);
                return true; // Indikuje, že akce byla zpracována
            }
        });

        loadWeights();
    }

    private void saveWeight() {
        String weightText = weightEditText.getText().toString().trim();
        if (weightText.isEmpty()) {
            Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight = Double.parseDouble(weightText);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_WEIGHT_VALUE, weight);
        contentValues.put(DBHelper.COLUMN_WEIGHT_DATE, getCurrentDateTime());

        mDatabase.insert(DBHelper.TABLE_WEIGHTS, null, contentValues);

        weightEditText.getText().clear();
        loadWeights();
    }

    private void loadWeights() {
        weightList.clear();

        Cursor cursor = mDatabase.query(
                DBHelper.TABLE_WEIGHTS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                double weight = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_WEIGHT_VALUE));
                String date = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_WEIGHT_DATE));
                weightList.add(new WeightEntry(weight, date));
            } while (cursor.moveToNext());
        }

        cursor.close();
        mAdapter.notifyDataSetChanged();
    }

    private void deleteWeight(WeightEntry weightEntry) {
        mDatabase.delete(
                DBHelper.TABLE_WEIGHTS,
                DBHelper.COLUMN_WEIGHT_ID + " = ?",
                new String[]{String.valueOf(weightEntry.getId())}
        );

        Toast.makeText(this, "Weight deleted successfully", Toast.LENGTH_SHORT).show();

        loadWeights(); // Aktualizace seznamu po smazání
    }

    private String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}

//zdroj chat gpt