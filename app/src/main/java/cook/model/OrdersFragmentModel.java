package cook.model;

import android.view.View;

import com.example.testfirebase.order.OrderItem;

import java.util.ArrayList;
import java.util.Map;

import cook.adapters.CookTablesRecyclerViewAdapter;
import cook.interfaces.OrdersFragmentInterface;
import tools.Pair;

public class OrdersFragmentModel implements OrdersFragmentInterface.Model {

    public static final char DELIMITER = '_';

    private Map<String, Pair<ArrayList<OrderItem>, Boolean>> ordersHashMap;
    private View view;
    private CookTablesRecyclerViewAdapter adapter;

    @Override
    public void setAdapter(CookTablesRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public CookTablesRecyclerViewAdapter getAdapter() {
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
    public void setOrdersHashMap(Map<String, Pair<ArrayList<OrderItem>, Boolean>> ordersHashMap) {
        this.ordersHashMap = ordersHashMap;
    }
    @Override
    public Map<String, Pair<ArrayList<OrderItem>, Boolean>> getOrdersHashMap() {
        return ordersHashMap;
    }
}
