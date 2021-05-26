package com.example.testfirebase.order;

public class DishCategoryInfo<F, S> {

    public F categoryName;
    public S categorySize;
    public S categoryNamePosition;

    public DishCategoryInfo(F first, S second) {
        this.categoryName = first;
        this.categorySize = second;
    }

}
