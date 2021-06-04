package cook.model;

import com.example.testfirebase.order.OrderItem;

import java.util.ArrayList;
import java.util.Map;

import cook.interfaces.OrdersFragmentInterface;
import tools.Pair;

public class OrdersFragmentModel implements OrdersFragmentInterface.Model {

    private Map<String, Pair<ArrayList<OrderItem>, Boolean>> ordersHashMap;

    @Override
    public void setOrdersHashMap(Map<String, Pair<ArrayList<OrderItem>, Boolean>> ordersHashMap) {
        this.ordersHashMap = ordersHashMap;
    }
}
