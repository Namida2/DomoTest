package model;


import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import interfaces.MenuDialogOrderActivityInterface;

import com.example.testfirebase.order.DishCategoryInfo;

public class MenuDialogModel implements MenuDialogOrderActivityInterface.Model {

    public static final String MENU_COLLECTION_NAME = "menu";
    public static final String DISHES_COLLECTION_NAME = "dishes";

    private MenuRecyclerViewAdapter adapter;

    private Map<String, List<Dish>> menu;
    private ArrayList<DishCategoryInfo<String, Integer>> categoryNames;

    private FirebaseFirestore db;

    public MenuDialogModel () {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public MenuRecyclerViewAdapter getMenuItemAdapter() {
        return adapter;
    }

    public void setMenuItemAdapter(MenuRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public FirebaseFirestore getDatabase() {
        return db;
    }
    @Override
    public Map<String, List<Dish>> getMenu() {
        return menu;
    }
    @Override
    public ArrayList<DishCategoryInfo<String, Integer>> getCategoryNames() {
        return categoryNames;
    }
    @Override
    public void setCategoryNames(ArrayList<DishCategoryInfo<String, Integer>> categoryNames) {
        this.categoryNames = categoryNames;
    }
    @Override
    public void setMenu(Map<String, List<Dish>> menu) {
        this.menu = menu;
    }

}
