package model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import interfaces.OrderActivityInterface;
import tools.Pair;

public class OrderActivityModel implements OrderActivityInterface.Model {

    private Map<String, Pair<ArrayList<OrderItem>, Boolean>> ordersHashMap;

    private View view;
    private RecyclerView orderRecyclerView;
    private OrderRecyclerViewAdapter adapter;

    private FirebaseFirestore db;
    public static final String COLLECTION_ORDERS_NAME = "orders";
    public static final String COLLECTION_ORDER_ITEMS_NAME = "order_items";
    public static final String DOCUMENT_TABLE = "table_";
    public static final String DOCUMENT_NAME_DELIMITER = "_";
    public static final String DOCUMENT_GUEST_COUNT_FIELD = "guestCount";

    public OrderActivityModel () {
        this.ordersHashMap = new HashMap<>();
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public Consumer<Pair<OrderItem, String>> getNotifyOrderAdapterConsumer() {
        return order -> {
            adapter.addOrder(order.first);
        };
    }
    @Override
    public Pair<ArrayList<OrderItem>, Boolean> getOrderInfo(int tableNumber) {
        String key = DOCUMENT_TABLE + tableNumber;
        if (ordersHashMap.containsKey(key)) return ordersHashMap.get(key);
        else {
            Pair<ArrayList<OrderItem>, Boolean> orderInfo = new Pair<>(new ArrayList<>(), false);
            ordersHashMap.put(key, orderInfo);
            return orderInfo;
        }
    }
    @Override
    public Map<String, Pair<ArrayList<OrderItem>, Boolean>> getOrdersHashMap() {
        return ordersHashMap;
    }
    @Override
    public FirebaseFirestore getDatabase() {
        return db;
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
