package com.example.testfirebase.order;

public class OrderItem {
    private String name;
    private String categoryName;
    private String cost;
    private String weight;
    private String description;

    private String commentary;
    private int count;

    public OrderItem (Dish dish, String commentary, int count) {
        this.name = dish.getName();
        this.categoryName = dish.getCategoryName();
        this.commentary = dish.getCost();
        this.weight = dish.getWeight();
        this.cost = dish.getCost();
        this.description = dish.getDescription();
        this.commentary = commentary;
        this.count = count;
    }
    public String getName() {
        return name;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public String getWeight() {
        return weight;
    }
    public int getCount() {
        return count;
    }
    public String getCommentary() {
        return commentary;
    }
    public String getCost() {
        return cost;
    }
    public String getDescription() {
        return description;
    }
}
