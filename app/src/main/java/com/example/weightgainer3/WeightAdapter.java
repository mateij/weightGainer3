package com.example.weightgainer3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WeightAdapter extends ArrayAdapter<WeightEntry> {

    private ArrayList<WeightEntry> weightList;
    private Context mContext;

    public WeightAdapter(Context context, ArrayList<WeightEntry> weightList) {
        super(context, 0, weightList);
        this.mContext = context;
        this.weightList = weightList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeightEntry weightEntry = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView weightTextView = convertView.findViewById(android.R.id.text1);
        weightTextView.setText(weightEntry.toString());

        return convertView;
    }
}