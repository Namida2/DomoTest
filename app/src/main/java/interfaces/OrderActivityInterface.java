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
        void setOrderRecyclerView(RecyclerView orderRecyclerView);
        void setAdapter(OrderRecyclerViewAdapter adapter);
        void setView(android.view.View view);
        OrderRecyclerViewAdapter getAdapter();
        RecyclerView getOrderRecyclerView();
        android.view.View getView();
        Consumer<Pair<OrderItem, String>> getNotifyOrderAdapterConsumer ();
        ArrayList<OrderItem> getOrderArrayList (int tableNumber);
        Map<String, ArrayList<OrderItem>> getOrdersList();
        FirebaseFirestore getDb();
    }
    interface View {
        void setOrderRecyclerView(RecyclerView orderRecyclerView);
        RecyclerView prepareOrderRecyclerView();
        void setOrderRecyclerViewConsumer(Consumer<Pair<OrderItem, String>> notifyOrderAdapterConsumer);
    }
    interface Presenter {
        void orderRecyclerViewOnActivityDestroy(boolean orderWasAccepted, int tableNumber);
        void setViewModelState();
        Consumer<Pair<OrderItem, String>> getNotifyOrderAdapterConsumer ();
        void writeOrderToDb(int tableNumber, int guestCount);
    }

}
