package model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderActivity;
import com.example.testfirebase.order.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import interfaces.OrderActivityInterface;
import tools.Pair;

public class OrderActivityModel implements OrderActivityInterface.Model {

    private Map<String, ArrayList<OrderItem>> ordersList;

    private View view;
    private RecyclerView orderRecyclerView;
    private OrderRecyclerViewAdapter adapter;

    public OrderActivityModel () {
        this.ordersList = new HashMap<>();
    }

    @Override
    public Consumer<Pair<OrderItem, String>> getNotifyOrderAdapterConsumer() {
        return order -> {
            if (ordersList.containsKey(order.second)) ordersList.get(order.second).add(order.first);
            else {
                ArrayList<OrderItem> arrayList = new ArrayList<>();
                arrayList.add(order.first);
                ordersList.put(order.second, arrayList);
            }
            adapter.addOrder(order.first);
        };
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
