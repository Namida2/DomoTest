package com.example.testfirebase.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.Dish;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import interfaces.MenuDialogOrderActivityInterface;
import model.MenuDialogModel;
import tools.Pair;


public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private ArrayList<Pair<String, Integer>> categoryName;
    private Map<String, List<Dish>> menu;

    private String lastCategoryName;

    public MenuRecyclerViewAdapter (Map<String, List<Dish>> menu, ArrayList<Pair<String, Integer>> categoryName) {
        this.menu = menu;
        this.categoryName = categoryName;
    }

    class CategoryNameViewHolder extends RecyclerView.ViewHolder {
        private Button categoryName;
        public CategoryNameViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }

    class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView weight;
        private TextView cost;
        public MenuItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dish_name);
            weight = itemView.findViewById(R.id.dish_weight);
            cost = itemView.findViewById(R.id.dish_cost);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 ) return 0;
        for(int i = 0; i < categoryName.size()-1; ++i) {
            if(position == menu.get( categoryName.get(i)).size() )
                return 0;
        }
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
                view = inflater.inflate(R.layout.layout_category, parent, false);
                return new CategoryNameViewHolder(view);
            case 1:
                view = inflater.inflate(R.layout.layout_menu_item, parent, false);
                return new MenuItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                lastCategoryName = categoryName.get(position).categoryName;
                CategoryNameViewHolder categoryNameViewHolder = (CategoryNameViewHolder) holder;
                categoryNameViewHolder.categoryName.setText(categoryName.get(position).categoryName);
                break;
            case 1:
                MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) holder;
                //menuItemViewHolder.name.setText( menu.get(lastCategoryName).get().getName() );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemCount() {
        AtomicInteger itemCount = new AtomicInteger();
        categoryName.forEach(item -> {
            itemCount.incrementAndGet(); //for category
            itemCount.addAndGet(item.categorySize); //for dishes in category
        });
        return itemCount.get();
    }


}
