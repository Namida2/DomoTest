package model;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import interfaces.OrderActivityInterface;
import tools.Pair;

public class OrderActivityModel implements OrderActivityInterface.Model {

    // key, listItems, isAccepted
    private static Map<String, Pair<ArrayList<OrderItem>, Boolean>> allTablesOrdersHashMap;
    private static Map<String, Pair<ArrayList<OrderItem>, Boolean>> notEmptyTablesOrdersHashMap;
    private static ArrayList<TableInfo> tablesInfo;

    private OrderRecyclerViewAdapter adapter;

    private FirebaseFirestore db;
    public static final String COLLECTION_ORDERS_NAME = "orders";
    public static final String COLLECTION_ORDER_ITEMS_NAME = "order_items";
    public static final String DOCUMENT_TABLE = "table_";
    public static final String DOCUMENT_NAME_DELIMITER = "_";
    public static final String DOCUMENT_GUEST_COUNT_FIELD = "guestCount";

    public OrderActivityModel () {
        allTablesOrdersHashMap = new HashMap<>();
        notEmptyTablesOrdersHashMap = new HashMap<>();
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public Map<String, Pair<ArrayList<OrderItem>, Boolean>> getNotEmptyTablesOrdersHashMap() {
        return notEmptyTablesOrdersHashMap;
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
        if (allTablesOrdersHashMap.containsKey(key)) return allTablesOrdersHashMap.get(key);
        else {
            Pair<ArrayList<OrderItem>, Boolean> orderInfo = new Pair<>(new ArrayList<>(), false);
            allTablesOrdersHashMap.put(key, orderInfo);
            return orderInfo;
        }
    }
    @Override
    public ArrayList<OrderItem> getOrderItemsArrayList(int tableNumber) {
        if (allTablesOrdersHashMap.get(DOCUMENT_TABLE + tableNumber) == null) return null;
        return allTablesOrdersHashMap.get(DOCUMENT_TABLE + tableNumber).first;
    }
    @Override
    public Map<String, Pair<ArrayList<OrderItem>, Boolean>> getAllTablesOrdersHashMap() {
        return allTablesOrdersHashMap;
    }
    @Override
    public FirebaseFirestore getDatabase() {
        return db;
    }
    @Override
    public void setTableInfoArrayList(ArrayList<TableInfo> tablesInfo) {
        this.tablesInfo = tablesInfo;
    }
    @Override
    public ArrayList<TableInfo> getTableInfoArrayList() {
        return tablesInfo;
    }
    @Override
    public void setAdapter(OrderRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public OrderRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
