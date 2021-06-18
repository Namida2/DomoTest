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
        ArrayList<OrderItem> getOrderInfo(int tableNumber);
        Map<String, ArrayList<OrderItem>> getAllTablesOrdersHashMap();
        void setAllTablesOrdersHashMap (Map<String, ArrayList<OrderItem>> allTablesOrdersHashMap);
        void setNotEmptyTablesOrdersHashMap (Map<String, ArrayList<OrderItem>> notEmptyTablesOrdersHashMap);
        Map<String, ArrayList<OrderItem>> getNotEmptyTablesOrdersHashMap();
        Map<String, ArrayList<OrderItem>> getTablesWithAllReadyDishes();
    }
    interface View {
    }
    interface Presenter {
        void notifyAdapterDataSetChanged(OrderItem orderItem);
        Map<String, ArrayList<OrderItem>> getNotEmptyTablesOrdersHashMap();
        void setModelDataState(boolean needToNotifyView);
        OrderRecyclerViewAdapter getOrderRecyclerViewAdapter(int ableNumber);
        void acceptAndWriteOrderToDb(int tableNumber, int guestCount);
        ArrayList<TableInfo> getTableInfoArrayList();
        void onDestroy();
    }
}
