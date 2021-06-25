package com.example.testfirebase.order;

import androidx.annotation.Nullable;

public class OrderItem {
    private String name;
    private String categoryName;
    private String cost;
    private String weight;
    private String description;

    private boolean ready;
    private String commentary;
    private int count;

    public OrderItem () {} // for FirebaseFirestore

    public void copy (OrderItem orderItem) {
        this.name = orderItem.name;
        this.categoryName = orderItem.categoryName;
        this.cost = orderItem.cost;
        this.weight = orderItem.weight;
        this.description = orderItem.description;
        this.ready = orderItem.ready;
        this.commentary = orderItem.commentary;
        this.count = orderItem.count;
    }

    public int getInt () {
        return 9;
    }

    public OrderItem (Dish dish, String commentary, int count) {
        this.name = dish.getName();
        this.categoryName = dish.getCategoryName();
        this.commentary = dish.getCost();
        this.weight = dish.getWeight();
        this.cost = dish.getCost();
        this.description = dish.getDescription();
        this.commentary = commentary;
        this.count = count;
        this.ready = false;
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
    public boolean isReady() {
        return ready;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }


    @Override
    public int hashCode() {
        return (name + commentary).hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null || obj.getClass() != OrderItem.class) return false;
        OrderItem orderItem = (OrderItem) obj;
        return (name + commentary).equals(orderItem.getName() + orderItem.getCommentary());
    }
}
