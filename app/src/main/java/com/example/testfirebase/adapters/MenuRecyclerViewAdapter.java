package com.example.testfirebase.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

    public static final int ADDITIONAL_SIZE = 1;
    private Consumer<Dish> acceptDishConsumer;
    private ArrayList<DishCategoryInfo<String, Integer>> categoryNames;
    private Map<String, List<Dish>> menu;
    private ArrayList<Object> menuItems;
    private RecyclerView categoryNamesRecyclerView;
    private Context context;

    private AtomicBoolean isPressed = new AtomicBoolean(false);

    public MenuRecyclerViewAdapter (Map<String, List<Dish>> menu, ArrayList<DishCategoryInfo<String, Integer>> categoryNames, RecyclerView categoryNamesRecyclerView) {
        this.menu = menu;
        this.categoryNamesRecyclerView = categoryNamesRecyclerView;
        this.categoryNames = categoryNames;
        menuItems = new ArrayList<>();
        menuItems.add(categoryNamesRecyclerView);
        for(int i = 0; i < this.categoryNames.size(); ++i){
            DishCategoryInfo <String, Integer> categoryNameInfo = categoryNames.get(i);
            menuItems.add(categoryNameInfo);
            menuItems.addAll(menu.get(categoryNameInfo.categoryName));
        }
    }

    public void setAcceptDishConsumer(Consumer<Dish> acceptDishConsumer) {
        this.acceptDishConsumer = acceptDishConsumer;
    }
    class CategoryNamesRecyclerViewViewViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout container;
        public CategoryNamesRecyclerViewViewViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
        }
    }
    class CategoryNameViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        public CategoryNameViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.line);
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
            if(position == categoryNames.get(i).categoryNamePosition + ADDITIONAL_SIZE) return 1;
        return 2;
    }
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.layout_category_names_container, parent, false);
                return new CategoryNamesRecyclerViewViewViewHolder(view);
            case 1:
                view = inflater.inflate(R.layout.layout_menu_category, parent, false);
                return new CategoryNameViewHolder(view);
            case 2:
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
                CategoryNamesRecyclerViewViewViewHolder categoryNamesRecyclerViewViewViewHolder = (CategoryNamesRecyclerViewViewViewHolder) holder;
                RecyclerView recyclerView = (RecyclerView) menuItems.get(position);
                if(recyclerView.getParent() != null)
                    ((ViewGroup) recyclerView.getParent()).removeView(recyclerView);
                categoryNamesRecyclerViewViewViewHolder.container.addView(recyclerView);
                break;
            case 1:
                CategoryNameViewHolder categoryNameViewHolder = (CategoryNameViewHolder) holder;
                DishCategoryInfo<String, Integer> dishCategoryInfo = (DishCategoryInfo<String, Integer>) menuItems.get(position);
                categoryNameViewHolder.categoryName.setText(dishCategoryInfo.categoryName);
                break;
            case 2:
                MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) holder;
                Dish dish = (Dish) menuItems.get(position);
                menuItemViewHolder.name.setText(dish.getName());
                menuItemViewHolder.weight.setText(dish.getWeight());
                menuItemViewHolder.cost.setText(dish.getCost());
                Animations.Companion.showView(menuItemViewHolder.menuItemContainer);
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
