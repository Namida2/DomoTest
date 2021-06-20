package cook.model;

import android.view.View;

import com.example.testfirebase.order.OrderItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import cook.adapters.CookOrdersRecyclerViewAdapter;
import cook.interfaces.OrdersFragmentInterface;

public class OrdersFragmentModel implements OrdersFragmentInterface.Model {

    public static final char DELIMITER = '_';

    private Map<String, ArrayList<OrderItem>> ordersHashMap;
    private CookOrdersRecyclerViewAdapter adapter;
    private FirebaseFirestore db;
    private View view;

    public OrdersFragmentModel () {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void setAdapter(CookOrdersRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public CookOrdersRecyclerViewAdapter getAdapter() {
        return adapter;
    }
    @Override
    public void setView(View view) {
        this.view = view;
    }
    @Override
    public View getView() {
        return view;
    }

    @Override
    public FirebaseFirestore getDatabase() {
        return db;
    }

    @Override
    public void setOrdersHashMap(Map<String, ArrayList<OrderItem>> ordersHashMap) {
        this.ordersHashMap = ordersHashMap;
    }
    @Override
    public Map<String, ArrayList<OrderItem>> getOrdersHashMap() {
        return ordersHashMap;
    }
}
