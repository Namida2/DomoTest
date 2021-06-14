package cook.interfaces;

import com.example.testfirebase.order.OrderItem;

import java.util.ArrayList;
import java.util.Map;

import cook.adapters.CookTablesRecyclerViewAdapter;
import tools.Pair;

public interface OrdersFragmentInterface {
    interface Model {
        void setAdapter(CookTablesRecyclerViewAdapter adapter);
        CookTablesRecyclerViewAdapter getAdapter();
        void setView(android.view.View view);
        android.view.View getView();
        void setOrdersHashMap (Map<String, Pair<ArrayList<OrderItem>, Boolean>> ordersHashMap);
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> getOrdersHashMap ();
    }
    interface View {
        void startDetailOrderActivity(String tableNumber);
    }
    interface Presenter {
        void onResume();
        void setModelState(android.view.View view);
        CookTablesRecyclerViewAdapter getAdapter ();
        android.view.View getView();
        void setView(android.view.View view);
    }
}
