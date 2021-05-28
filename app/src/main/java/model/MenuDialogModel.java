package model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import interfaces.MenuDialogOrderActivityInterface;
import tools.Pair;

import com.example.testfirebase.order.DishCategoryInfo;

public class MenuDialogModel implements MenuDialogOrderActivityInterface.Model {

    public static final String MENU_COLLECTION_NAME = "menu";
    public static final String DISHES_COLLECTION_NAME = "dishes";

    private View view;
    private RecyclerView recyclerView;
    private MenuRecyclerViewAdapter adapter;

    private Map<String, List<Dish>> menu;
    private ArrayList<DishCategoryInfo<String, Integer>> categoryNames;

    private FirebaseFirestore db;

    public MenuDialogModel () {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public Map<String, Object> getModelState() {
        return null;
    }
    @Override
    public void setView(View view) {
        this.view = view;
    }
    @Override
    public View getView() {
        return view;
    }
    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    @Override
    public MenuRecyclerViewAdapter getMenuItemAdapter() {
        return adapter;
    }
    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
    @Override
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
