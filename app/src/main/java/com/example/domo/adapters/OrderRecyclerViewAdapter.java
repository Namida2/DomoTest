package com.example.domo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domo.R;
import com.example.domo.order.OrderItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import model.OrderActivityModel;
import tools.Animations;
import tools.Pair;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    private ArrayList<OrderItem> orderItemsArrayList;

    public void setOrderItemsArrayList(ArrayList<OrderItem> orderItemsArrayList) {
        this.orderItemsArrayList = orderItemsArrayList;
    }

    public ArrayList<OrderItem> getOrderItemsArrayList() {
        return orderItemsArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout container;
        public TextView categoryName;
        public TextView name;
        public TextView weight;
        public TextView cost;
        public TextView count;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.order_item_container);
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
        holder.categoryName.setText(orderItemsArrayList.get(position).getCategoryName());
        holder.name.setText(orderItemsArrayList.get(position).getName());
        holder.weight.setText(orderItemsArrayList.get(position).getWeight());
        holder.cost.setText(orderItemsArrayList.get(position).getCost());
        holder.count.setText(orderItemsArrayList.get(position).getCount() + " шт");
        Animations.Companion.startAnimationViewShowing(holder.container);
    }
    @Override
    public int getItemCount() {
        return orderItemsArrayList.size();
    }
    public void addOrder(OrderItem order) {
        OrderActivityModel model = new OrderActivityModel();
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> orderItems = model.getAllTablesOrdersHashMap();
        ArrayList<OrderItem> adapterItems = getOrderItemsArrayList();
        this.orderItemsArrayList.add(order);
        this.notifyDataSetChanged();
    }
}
