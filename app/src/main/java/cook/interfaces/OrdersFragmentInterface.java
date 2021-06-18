package cook.interfaces;

import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import cook.adapters.CookTablesRecyclerViewAdapter;
import tools.Pair;

public interface OrdersFragmentInterface {
    interface Model {
        android.view.View getView();
        FirebaseFirestore getDatabase();
        void setView(android.view.View view);
        CookTablesRecyclerViewAdapter getAdapter();
        void setAdapter(CookTablesRecyclerViewAdapter adapter);
        Map<String, ArrayList<OrderItem>> getOrdersHashMap ();
        void setOrdersHashMap (Map<String, ArrayList<OrderItem>> ordersHashMap);
    }
    interface View {
        void startDetailOrderActivity(TableInfo tableInfo);
    }
    interface Presenter {
        void onResume();
        void deleteOrderFromDatabase (String tableNumber);
        void setModelState(android.view.View view);
        CookTablesRecyclerViewAdapter getAdapter ();
        android.view.View getView();
        void setView(android.view.View view);
    }
}
