package com.example.testfirebase.order;

public class Dish {
    public static int position = 0;
    public int myPosition;
    private String name;
    private String categoryName;
    private String cost;
    private String weight;
    private String description;

    public Dish () {
        this.myPosition = ++position;
    }

    public static void incrementPosition () {
        ++position;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getName() {
        return name;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

