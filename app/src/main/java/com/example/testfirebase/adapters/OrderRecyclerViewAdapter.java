package com.example.testfirebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.Dish;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import tools.Pair;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Pair<Dish, Pair<Integer, String>>> dishesArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName;
        public TextView name;
        public TextView weight;
        public TextView cost;
        public TextView count;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            name = itemView.findViewById(R.id.dish_name);
            weight = itemView.findViewById(R.id.dish_weight);
            cost = itemView.findViewById(R.id.dish_cost);
            count = itemView.findViewById(R.id.dish_count);
        }
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_order_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.categoryName.setText(dishesArrayList.get(position).first.getCategoryName());
        holder.name.setText(dishesArrayList.get(position).first.getName());
        holder.weight.setText(dishesArrayList.get(position).first.getWeight());
        holder.cost.setText(dishesArrayList.get(position).first.getCost());
        holder.count.setText(dishesArrayList.get(position).second.first.toString());
    }
    @Override
    public int getItemCount() {
        return dishesArrayList.size();
    }
    public void addDish(Pair<Dish, Pair<Integer, String>> order) {
        this.dishesArrayList.add(order);
        this.notifyDataSetChanged();
    }

}
