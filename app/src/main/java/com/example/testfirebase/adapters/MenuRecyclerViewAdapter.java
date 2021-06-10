package com.example.testfirebase.adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.Dish;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import com.example.testfirebase.order.DishCategoryInfo;
import com.jakewharton.rxbinding4.view.RxView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import tools.Animations;

import static registration.LogInActivity.TAG;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Consumer<Dish> acceptDishConsumer;
    private ArrayList<DishCategoryInfo<String, Integer>> categoryNames;
    private Map<String, List<Dish>> menu;
    private ArrayList<Object> menuItems;

    private AtomicBoolean isPressed = new AtomicBoolean(false);

    public MenuRecyclerViewAdapter (Map<String, List<Dish>> menu, ArrayList<DishCategoryInfo<String, Integer>> categoryNames) {
        this.menu = menu;
        this.categoryNames = categoryNames;
        menuItems = new ArrayList<>();
        for(int i = 0; i < this.categoryNames.size(); ++i){
            DishCategoryInfo <String, Integer> categoryNameInfo = categoryNames.get(i);
            menuItems.add(categoryNameInfo);
            menuItems.addAll(menu.get(categoryNameInfo.categoryName));
        }
    }

    public void setAcceptDishConsumer(Consumer<Dish> acceptDishConsumer) {
        this.acceptDishConsumer = acceptDishConsumer;
    }

    class CategoryNameViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        public CategoryNameViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
    class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView weight;
        private TextView cost;
        private ConstraintLayout menuItemContainer;
        public MenuItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dish_name);
            weight = itemView.findViewById(R.id.dish_weight);
            cost = itemView.findViewById(R.id.dish_cost);
            menuItemContainer = itemView.findViewById(R.id.menu_item_container);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0 ) return 0;
        for(int i = 0; i < categoryNames.size(); ++i)
            if(position == categoryNames.get(i).categoryNamePosition) return 0;
        return 1;
    }
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.layout_menu_category, parent, false);
                return new CategoryNameViewHolder(view);
            case 1:
                view = inflater.inflate(R.layout.layout_menu_item, parent, false);
                return new MenuItemViewHolder(view);
        }
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                CategoryNameViewHolder categoryNameViewHolder = (CategoryNameViewHolder) holder;
                DishCategoryInfo<String, Integer> dishCategoryInfo = (DishCategoryInfo<String, Integer>) menuItems.get(position);
                categoryNameViewHolder.categoryName.setText(dishCategoryInfo.categoryName);
                break;
            case 1:
                MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) holder;
                Dish dish = (Dish) menuItems.get(position);
                menuItemViewHolder.name.setText (dish.getName());
                menuItemViewHolder.weight.setText(dish.getWeight());
                menuItemViewHolder.cost.setText(dish.getCost());
                Animations.Companion.startAnimationViewShowing(menuItemViewHolder.menuItemContainer);
                RxView.clicks(menuItemViewHolder.menuItemContainer)
                    .debounce(150, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(item -> {
                        if(!isPressed.get()) {
                            isPressed.set(true);
                            acceptDishConsumer.accept(dish);
                        }
                    }, error -> {
                        Log.d(TAG, "MenuRecyclerViewAdapter.onBindViewHolder: " + error.getMessage());
                    }, () -> { });
                break;
        }
    }
    public void resetIsPressed() {
        isPressed.set(false);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemCount() {
        return menuItems.size();
    }
}
