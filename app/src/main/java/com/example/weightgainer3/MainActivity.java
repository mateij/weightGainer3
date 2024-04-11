package com.example.weightgainer3;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView kcalTextView, proteinTextView;
    private EditText kcalEditText, proteinEditText, weightEditText;
    private Button addButton, saveButton, historyButton, trackWeightButton;

    private SQLiteDatabase mDatabase;
    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kcalTextView = findViewById(R.id.kcalTextView);
        proteinTextView = findViewById(R.id.proteinTextView);
        kcalEditText = findViewById(R.id.kcalEditText);
        proteinEditText = findViewById(R.id.proteinEditText);
        weightEditText = findViewById(R.id.weightEditText);
        addButton = findViewById(R.id.addButton);
        saveButton = findViewById(R.id.saveButton);
        historyButton = findViewById(R.id.historyButton);
        trackWeightButton = findViewById(R.id.trackWeightButton);

        mDBHelper = new DBHelper(this);
        mDatabase = mDBHelper.getWritableDatabase();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndAdd();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveDialog();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentValuesToSharedPreferences();
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        trackWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeightTrackingActivity.class);
                startActivity(intent);
            }
        });

        loadSavedValuesFromSharedPreferences(); // Načtení uložených hodnot po obnovení aktivity
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("kcalValue", kcalTextView.getText().toString());
        outState.putString("proteinValue", proteinTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String kcalValue = savedInstanceState.getString("kcalValue");
        String proteinValue = savedInstanceState.getString("proteinValue");
        kcalTextView.setText(kcalValue);
        proteinTextView.setText(proteinValue);
    }

    private void calculateAndAdd() {
        // Získání hodnot z EditText polí
        String kcalText = kcalEditText.getText().toString();
        String proteinText = proteinEditText.getText().toString();
        String weightText = weightEditText.getText().toString();

        // Kontrola, zda jsou všechna pole vyplněna
        if (kcalText.isEmpty() || proteinText.isEmpty() || weightText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Odebrání jednotek a převod na čísla
        double kcalPer100g = Double.parseDouble(kcalText.replace(" kcal", ""));
        double proteinPer100g = Double.parseDouble(proteinText.replace(" g", ""));
        double weight = Double.parseDouble(weightText);

        // Výpočet celkových hodnot
        double totalKcal = Double.parseDouble(kcalTextView.getText().toString().replace(" kcal", ""));
        double totalProtein = Double.parseDouble(proteinTextView.getText().toString().replace(" g", ""));
        totalKcal += Math.round((kcalPer100g / 100) * weight);
        totalProtein += Math.round((proteinPer100g / 100) * weight);

        // Aktualizace zobrazených hodnot s jednotkami
        kcalTextView.setText(String.format("%.0f", totalKcal) + " kcal");
        proteinTextView.setText(String.format("%.0f", totalProtein) + " g");

        // Vymazání EditText polí
        kcalEditText.getText().clear();
        proteinEditText.getText().clear();
        weightEditText.getText().clear();
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Day");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dayName = input.getText().toString();
                saveDay(dayName);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void saveDay(String dayName) {
        // Získání hodnot z textových polí
        String kcalText = kcalTextView.getText().toString();
        String proteinText = proteinTextView.getText().toString();

        // Získání hodnot kalorií a proteinů
        double totalKcal = Double.parseDouble(kcalText.replace(" kcal", ""));
        double totalProtein = Double.parseDouble(proteinText.replace(" g", ""));

        // Vytvoření nového záznamu pro den
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_DAY_NAME, dayName);
        contentValues.put(DBHelper.COLUMN_TOTAL_KCAL, totalKcal);
        contentValues.put(DBHelper.COLUMN_TOTAL_PROTEIN, totalProtein);

        // Vložení záznamu do databáze
        mDatabase.insert(DBHelper.TABLE_DAYS, null, contentValues);

        // Resetování zobrazených hodnot
        kcalTextView.setText("0 kcal");
        proteinTextView.setText("0 g");

        Toast.makeText(this, "Day saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void saveCurrentValuesToSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_kcal", kcalTextView.getText().toString());
        editor.putString("current_protein", proteinTextView.getText().toString());
        editor.apply();
    }

    private void loadSavedValuesFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String kcal = preferences.getString("current_kcal", "0 kcal");
        String protein = preferences.getString("current_protein", "0 g");
        kcalTextView.setText(kcal);
        proteinTextView.setText(protein);
    }
}