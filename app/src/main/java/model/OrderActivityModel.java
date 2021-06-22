package model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import interfaces.OrderActivityInterface;
import tools.Pair;

public class OrderActivityModel implements OrderActivityInterface.Model {

    // key, listItems, isAccepted
    private static Map<String, ArrayList<OrderItem>> allTablesOrdersHashMap;
    private static Map<String, ArrayList<OrderItem>> notEmptyOrdersHashMap;
    private static ArrayList<TableInfo> tablesInfo;

    private OrderRecyclerViewAdapter adapter;

    private FirebaseFirestore db;
    public static final String COLLECTION_ORDERS_NAME = "orders";
    public static final String COLLECTION_ORDER_ITEMS_NAME = "order_items";
    public static final String DOCUMENT_TABLE = "table_";
    public static final String DOCUMENT_NAME_DELIMITER = "_";
    public static final String DOCUMENT_GUEST_COUNT_FIELD = "guestCount";
    public static final String DOCUMENT_READY_FIELD = "ready";
    public static final String DOCUMENT_DELETE_ORDER_LISTENER = "delete_order_listener";

    public OrderActivityModel () {
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public Map<String, ArrayList<OrderItem>> getNotEmptyTablesOrdersHashMap() {
        return notEmptyOrdersHashMap;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Map<String, ArrayList<OrderItem>> getTablesWithAllReadyDishes() {
        Map<String, ArrayList<OrderItem>> tablesWithAllReadyDishes = new HashMap<>();
        notEmptyOrdersHashMap.forEach( (key, orderItems) -> {
            boolean allReady = true;
            for(int i = 0; i < orderItems.size(); ++i)
                if(!orderItems.get(i).isReady()) allReady = false;
            if(allReady) tablesWithAllReadyDishes.put(key, orderItems);
        });

        return null;
    }
    @Override
    public ArrayList<OrderItem> getOrderItems(int tableNumber) {
        String key = DOCUMENT_TABLE + tableNumber;
        if (allTablesOrdersHashMap.containsKey(key)) return allTablesOrdersHashMap.get(key);
        else {
            ArrayList<OrderItem> arrayList = new ArrayList<>();
            allTablesOrdersHashMap.put(key, arrayList);
            return arrayList;
        }
    }
    @Override
    public ArrayList<OrderItem> getOrderItemsArrayList(int tableNumber) {
        if (allTablesOrdersHashMap.get(DOCUMENT_TABLE + tableNumber) == null) return null;
        return allTablesOrdersHashMap.get(DOCUMENT_TABLE + tableNumber);
    }
    @Override
    public Map<String, ArrayList<OrderItem>> getAllTablesOrdersHashMap() {
        return allTablesOrdersHashMap;
    }
    @Override
    public void setAllTablesOrdersHashMap(Map<String, ArrayList<OrderItem>> allTablesOrdersHashMap) {
        OrderActivityModel.allTablesOrdersHashMap = allTablesOrdersHashMap;
    }
    @Override
    public void setNotEmptyTablesOrdersHashMap(Map<String, ArrayList<OrderItem>> notEmptyTablesOrdersHashMap) {
        OrderActivityModel.notEmptyOrdersHashMap = notEmptyTablesOrdersHashMap;
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
