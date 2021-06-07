package cook.interfaces;

import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;
import tools.Pair;

public interface DetailOrderActivityInterface {
    interface Model {
        FirebaseFirestore getDatabase();
        void setRecyclerViewAdapter(DetailOrderItemsRecyclerViewAdapter adapter);
        DetailOrderItemsRecyclerViewAdapter getRecyclerViewAdapter();
    }
    interface View {
        void showSetDishReadyDialog (Pair<OrderItem, TableInfo> dishData);
        void setViewData();
    }
    interface Presenter {
        void onResume();
        void setDishState(Pair<OrderItem, TableInfo> dishData);
        DetailOrderItemsRecyclerViewAdapter getAdapter(String tableNumber);
    }
}
