package interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

import tools.Pair;


public interface OrderActivityInterface {
    interface Model {
        ArrayList<OrderItem> getOrderItemsArrayList(int tableNumber);
        void setAdapter(OrderRecyclerViewAdapter adapter);
        OrderRecyclerViewAdapter getAdapter();
        Consumer<Pair<OrderItem, String>> getNotifyOrderAdapterConsumer ();
        public Pair<ArrayList<OrderItem>, Boolean> getOrderInfo(int tableNumber);
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> getAllTablesOrdersHashMap();
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> getNotEmptyTablesOrdersHashMap()
        FirebaseFirestore getDatabase();
    }
    interface View {
        void setOrdersListForThisTable();
        void setOrderRecyclerViewConsumer(Consumer<Pair<OrderItem, String>> notifyOrderAdapterConsumer);
    }
    interface Presenter {
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> getNotEmptyTablesOrdersHashMap();
        void setModelDataState(boolean needToNotifyView);
        OrderRecyclerViewAdapter getOrderRecyclerViewAdapter(int ableNumber);
        void orderRecyclerViewOnActivityDestroy(int tableNumber);
        Consumer<Pair<OrderItem, String>> getOrderNotifyAdapterConsumer();
        void acceptAndWriteOrderToDb(int tableNumber, int guestCount);
    }
}
