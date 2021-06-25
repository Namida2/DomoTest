package com.example.testfirebase.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.DishCategoryInfo;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class CategoryNamesRecyclerViewAdapter extends RecyclerView.Adapter<CategoryNamesRecyclerViewAdapter.ViewHolder> {

    private ArrayList<DishCategoryInfo<String, Integer>> categoryNames;
    private RecyclerView menuRecyclerView;
    private Context context;

    public CategoryNamesRecyclerViewAdapter(ArrayList<DishCategoryInfo<String, Integer>> categoryNames) {
        this.categoryNames = categoryNames;
    }
    public void setMenuRecyclerView(RecyclerView menuRecyclerView) {
        this.menuRecyclerView = menuRecyclerView;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.layout_category_names_item, null);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.categoryName.setText(categoryNames.get(position).categoryName);
        RxView.clicks(holder.categoryName)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context);
                smoothScroller.setTargetPosition(categoryNames.get(position).categoryNamePosition + MenuRecyclerViewAdapter.ADDITIONAL_SIZE + 5);
                menuRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
            });
    }
    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button categoryName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name_button);
        }
    }

}
