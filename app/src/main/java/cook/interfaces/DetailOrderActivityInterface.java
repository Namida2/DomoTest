package cook.interfaces;

import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import cook.ReadyDish;
import cook.adapters.DetailOrderItemsRecyclerViewAdapter;
import tools.Pair;

public interface DetailOrderActivityInterface {
    interface Model {
        FirebaseFirestore getDatabase();
        void setRecyclerViewAdapter(DetailOrderItemsRecyclerViewAdapter adapter);
        DetailOrderItemsRecyclerViewAdapter getRecyclerViewAdapter();
    }
    interface View {
        void showSetDishReadyDialog (ReadyDish dishData);
        void setViewData();
    }
    interface Presenter {
        void onResume();
        void setDishState(ReadyDish dishData);
        DetailOrderItemsRecyclerViewAdapter getAdapter(String tableNumber);
    }
}
