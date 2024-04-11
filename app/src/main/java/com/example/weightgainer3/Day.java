package com.example.weightgainer3;

public class Day {
    private int id;
    private String name;
    private double totalKcal;
    private double totalProtein;

    public Day(int id, String name, double totalKcal, double totalProtein) {
        this.id = id;
        this.name = name;
        this.totalKcal = totalKcal;
        this.totalProtein = totalProtein;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTotalKcal() {
        return totalKcal;
    }

    public double getTotalProtein() {
        return totalProtein;
    }
}

//zdroj chat gpt