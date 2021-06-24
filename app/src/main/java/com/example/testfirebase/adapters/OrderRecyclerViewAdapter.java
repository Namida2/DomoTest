package com.example.testfirebase.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.OrderItem;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import model.OrderActivityModel;
import tools.Animations;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    private ArrayList<OrderItem> orderItemsArrayList;
    private Consumer<OrderItem> editOrderConsumer;

    public void setOrderItemsArrayList(ArrayList<OrderItem> orderItemsArrayList) {
        this.orderItemsArrayList = new ArrayList<>();
        this.orderItemsArrayList.addAll(orderItemsArrayList);
    }
    public ArrayList<OrderItem> getOrderItemsArrayList() {
        return orderItemsArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout container;
        public RelativeLayout container_large;
        public TextView categoryName;
        public TextView name;
        public TextView weight;
        public TextView cost;
        public TextView count;
        public TextView commentary;
        public TextView commentaryTitle;
        public ImageView isReady;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.order_item_container);
            categoryName = itemView.findViewById(R.id.category_name);
            name = itemView.findViewById(R.id.dish_name);
            weight = itemView.findViewById(R.id.dish_weight);
            cost = itemView.findViewById(R.id.dish_cost);
            count = itemView.findViewById(R.id.dish_count);
            commentary = itemView.findViewById(R.id.commentary);
            commentaryTitle = itemView.findViewById(R.id.text);
            isReady = itemView.findViewById(R.id.is_ready);
            container_large = itemView.findViewById(R.id.order_item_container_large);
        }
    }
    public void setEditOrderConsumer(Consumer<OrderItem> editOrderConsumer  ) {
        this.editOrderConsumer = editOrderConsumer;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_order_item, parent, false);
        return new ViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.categoryName.setText(orderItemsArrayList.get(position).getCategoryName());
        holder.name.setText(orderItemsArrayList.get(position).getName());
        holder.weight.setText(orderItemsArrayList.get(position).getWeight());
        holder.cost.setText(orderItemsArrayList.get(position).getCost());
        holder.count.setText(orderItemsArrayList.get(position).getCount() + " шт");

        holder.commentaryTitle.setVisibility(View.VISIBLE);
        holder.commentary.setVisibility(View.VISIBLE);
        if (orderItemsArrayList.get(position).getCommentary().equals("")) {
            holder.commentaryTitle.setVisibility(View.GONE);
            holder.commentary.setVisibility(View.GONE);
        } else holder.commentary.setText(orderItemsArrayList.get(position).getCommentary() + " ");
        if (orderItemsArrayList.get(position).isReady())
            holder.isReady.setVisibility(View.VISIBLE);
        else holder.isReady.setVisibility(View.GONE);

        RxView.clicks(holder.container_large)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                editOrderConsumer.accept(orderItemsArrayList.get(position));
            });
        RxView.clicks(holder.container)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                editOrderConsumer.accept(orderItemsArrayList.get(position));
            });

        Animations.Companion.showView(holder.container);
    }
    @Override
    public int getItemCount() {
        return orderItemsArrayList.size();
    }
    public void addOrderItem(OrderItem order) {
        this.orderItemsArrayList.add(order);
        this.notifyDataSetChanged();
    }
    public void notifyOrderItemDataSetChanged (OrderItem orderItem) {
        for(int i = 0; i < orderItemsArrayList.size(); ++i) {
            if( (orderItemsArrayList.get(i).getName()
                + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                + orderItemsArrayList.get(i).getCommentary())
                .equals(orderItem.getName()
                    + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                    + orderItem.getCommentary())) {
                this.notifyItemChanged(i);
                break;
            }
        }
    }
    public void removeOrderItem (OrderItem orderItem) {
        for(int i = 0; i < orderItemsArrayList.size(); ++i) {
            if( (orderItemsArrayList.get(i).getName()
                + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                + orderItemsArrayList.get(i).getCommentary())
                .equals(orderItem.getName()
                    + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                    + orderItem.getCommentary())) {
                orderItemsArrayList.remove(i);
                this.notifyDataSetChanged();
                break;
            }
        }
    }

}
