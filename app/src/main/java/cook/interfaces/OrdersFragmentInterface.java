package cook.interfaces;

import android.view.View;

import com.example.testfirebase.order.OrderItem;

import java.util.ArrayList;
import java.util.Map;

import tools.Pair;

public interface OrdersFragmentInterface {
    interface Model {
        void setView(View view);
        android.view.View getView();
        void setOrdersHashMap (Map<String, Pair<ArrayList<OrderItem>, Boolean>> ordersHashMap);
    }
    interface View {

    }
    interface Presenter {
        android.view.View getView();
    }
}
