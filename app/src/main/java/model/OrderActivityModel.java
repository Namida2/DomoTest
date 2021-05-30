package model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;

import java.util.ArrayList;
import java.util.function.Consumer;

import interfaces.OrderActivityInterface;
import tools.Pair;

public class OrderActivityModel implements OrderActivityInterface.Model {

    private ArrayList<Pair<Dish, Pair<String, Integer>>> orderItemArrayList;

    private View view;
    private RecyclerView orderRecyclerView;
    private OrderRecyclerViewAdapter adapter;

    @Override
    public Consumer<Pair<Dish, Pair<String, Integer>>> getNotifyOrderAdapterConsumer() {
        return order -> {
            adapter.addOrder(order);
        };
    }
    @Override
    public void setOrderItemArrayList(ArrayList<Pair<Dish, Pair<String, Integer>>> orderItemArrayList) {
        this.orderItemArrayList = orderItemArrayList;
    }
    @Override
    public void setOrderRecyclerView(RecyclerView orderRecyclerView) {
        this.orderRecyclerView = orderRecyclerView;
    }
    @Override
    public void setAdapter(OrderRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public void setView(View view) {
        this.view = view;
    }
    @Override
    public ArrayList<Pair<Dish, Pair<String, Integer>>> getOrderItemArrayList() {
        return orderItemArrayList;
    }
    @Override
    public OrderRecyclerViewAdapter getAdapter() {
        return adapter;
    }
    @Override
    public RecyclerView getOrderRecyclerView() {
        return orderRecyclerView;
    }
    @Override
    public View getView() {
        return view;
    }

}
