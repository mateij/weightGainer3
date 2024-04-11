package com.example.weightgainer3;

import java.util.Date;

public class WeightEntry {
    private double weight;
    private String date;
    private int id;

    public WeightEntry(double weight, String date) {
        this.weight = weight;
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%.2f kg   %s", weight, date);

    }
    public int getId() {
        return id;
    }
}

//zdroj chat gpt