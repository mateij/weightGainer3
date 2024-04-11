package com.example.weightgainer3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private DBHelper mDBHelper;
    private ArrayList<Day> daysList;
    private DayAdapter mAdapter;
    private Button homeButton; // Přidáno tlačítko pro domů
    private TextView hintTextView; // Textové pole pro zobrazení upozornění

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mDBHelper = new DBHelper(this);
        mDatabase = mDBHelper.getReadableDatabase();

        daysList = new ArrayList<>();
        mAdapter = new DayAdapter(this, R.layout.day_item, daysList);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        hintTextView = findViewById(R.id.hintTextView);

        loadDays();

        // Dlouhý posluchač na seznamu pro smazání záznamu po dlouhém stisknutí
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Day selectedDay = daysList.get(position);
                deleteDay(selectedDay);
                return true; // Indikuje, že akce byla zpracována
            }
        });

        homeButton = findViewById(R.id.homeButton); // Inicializace tlačítka pro domů
        homeButton.setOnClickListener(new View.OnClickListener() { // Posluchač pro tlačítko Domů
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadDays() {
        daysList.clear();

        Cursor cursor = mDatabase.query(
                DBHelper.TABLE_DAYS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DAY_NAME));
                int totalKcal = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL_KCAL));
                int totalProtein = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_TOTAL_PROTEIN));

                Day day = new Day(id, name, totalKcal, totalProtein);
                daysList.add(day);

            } while (cursor.moveToNext());
        }

        cursor.close();
        mAdapter.notifyDataSetChanged();
    }

    private void deleteDay(Day day) {
        mDatabase.delete(
                DBHelper.TABLE_DAYS,
                DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(day.getId())}
        );

        Toast.makeText(this, "Day deleted successfully", Toast.LENGTH_SHORT).show();

        loadDays(); // Aktualizace seznamu po smazání
    }
}

//zdroj chat gpt