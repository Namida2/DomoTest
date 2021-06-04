package cook.interfaces;

import com.example.testfirebase.order.OrderItem;

import java.util.ArrayList;
import java.util.Map;

import tools.Pair;

public interface OrdersFragmentInterface {
    interface Model {
         void setOrdersHashMap (Map<String, Pair<ArrayList<OrderItem>, Boolean>> ordersHashMap);
    }
    interface View {

    }
    interface Presenter {

    }
}
