package com.example.weightgainer3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DayAdapter extends ArrayAdapter<Day> {

    private Context mContext;
    private int mResource;

    public DayAdapter(Context context, int resource, ArrayList<Day> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView dayNameTextView = convertView.findViewById(R.id.dayNameTextView);
        TextView totalKcalTextView = convertView.findViewById(R.id.totalKcalTextView);
        TextView totalProteinTextView = convertView.findViewById(R.id.totalProteinTextView);

        Day day = getItem(position);

        dayNameTextView.setText(day.getName());
        totalKcalTextView.setText(String.format("%.0f", day.getTotalKcal()) + " kcal");
        totalProteinTextView.setText(String.format("%.0f", day.getTotalProtein()) + " g");

        return convertView;
    }
}


//zdroj chat gpt