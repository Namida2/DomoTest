package model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderActivity;
import com.example.testfirebase.order.OrderItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import interfaces.OrderActivityInterface;
import tools.Pair;

public class OrderActivityModel implements OrderActivityInterface.Model {

    private Map<String, ArrayList<OrderItem>> ordersList;
    private Map<Pair<String, Boolean>, ArrayList<OrderItem>> newOrdersList;

    private View view;
    private RecyclerView orderRecyclerView;
    private OrderRecyclerViewAdapter adapter;

    private FirebaseFirestore db;
    public static final String COLLECTION_ORDERS = "orders";
    public static final String COLLECTION_ORDER_ITEMS = "order_items";
    public static final String DOCUMENT_TABLE = "table_";
    public static final String DOCUMENT_NAME_DELIMITER = "_";
    public static final String DOCUMENT_GUEST_COUNT_FIELD = "guestCount";


    public OrderActivityModel () {
        this.ordersList = new HashMap<>();
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public ArrayList<OrderItem> getOrderArrayList (int tableNumber) {
        String key = Integer.toString(tableNumber);
        if (ordersList.containsKey(key)) return ordersList.get(key);
        else {
            ArrayList<OrderItem> arrayList = new ArrayList<>();
            ordersList.put(key, arrayList);
            return arrayList;
        }
    }
    @Override
    public Map<String, ArrayList<OrderItem>> getOrdersList() {
        return ordersList;
    }
    @Override
    public FirebaseFirestore getDb() {
        return db;
    }
    @Override
    public Consumer<Pair<OrderItem, String>> getNotifyOrderAdapterConsumer() {
        return order -> {
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
