package interfaces;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import tools.Pair;

public interface OrderActivityInterface {
    interface Model {
        FirebaseFirestore getDatabase();
        OrderRecyclerViewAdapter getAdapter();
        ArrayList<TableInfo> getTableInfoArrayList();
        void setAdapter(OrderRecyclerViewAdapter adapter);
        void setTableInfoArrayList(ArrayList<TableInfo> tablesInfo);
        ArrayList<OrderItem> getOrderItemsArrayList(int tableNumber);
        Pair<ArrayList<OrderItem>, Boolean> getOrderInfo(int tableNumber);
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> getAllTablesOrdersHashMap();
        void setAllTablesOrdersHashMap (Map<String, Pair<ArrayList<OrderItem>, Boolean>> allTablesOrdersHashMap);
        void setNotEmptyTablesOrdersHashMap (Map<String, Pair<ArrayList<OrderItem>, Boolean>> notEmptyTablesOrdersHashMap);
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> getNotEmptyTablesOrdersHashMap();
    }
    interface View {
    }
    interface Presenter {
        void notifyAdapterDataSetChanged(OrderItem orderItem);
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> getNotEmptyTablesOrdersHashMap();
        void setModelDataState(boolean needToNotifyView);
        OrderRecyclerViewAdapter getOrderRecyclerViewAdapter(int ableNumber);
        void orderRecyclerViewOnActivityDestroy(int tableNumber);
        void acceptAndWriteOrderToDb(int tableNumber, int guestCount);
        ArrayList<TableInfo> getTableInfoArrayList();
        void onDestroy();
    }
}
