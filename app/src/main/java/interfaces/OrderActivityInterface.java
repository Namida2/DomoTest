package interfaces;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
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
        void setAllTablesOrdersHashMap (Map<String, Pair<ArrayList<OrderItem>, Boolean>> allTablesOrdersHashMap);
        void setNotEmptyTablesOrdersHashMap (Map<String, Pair<ArrayList<OrderItem>, Boolean>> notEmptyTablesOrdersHashMap) ;
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> getNotEmptyTablesOrdersHashMap();
        FirebaseFirestore getDatabase();
        void setTableInfoArrayList(ArrayList<TableInfo> tablesInfo);
        ArrayList<TableInfo> getTableInfoArrayList();
    }
    interface View {
        void setOrderRecyclerViewConsumer(Consumer<Pair<OrderItem, String>> notifyOrderAdapterConsumer);
    }
    interface Presenter {
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> getNotEmptyTablesOrdersHashMap();
        void setModelDataState(boolean needToNotifyView);
        OrderRecyclerViewAdapter getOrderRecyclerViewAdapter(int ableNumber);
        void orderRecyclerViewOnActivityDestroy(int tableNumber);
        Consumer<Pair<OrderItem, String>> getOrderNotifyAdapterConsumer();
        void acceptAndWriteOrderToDb(int tableNumber, int guestCount);
        ArrayList<TableInfo> getTableInfoArrayList();
    }
}
