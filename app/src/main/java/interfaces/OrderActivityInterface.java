package interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;

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
    }
    interface View {
        void setOrderRecyclerView(RecyclerView orderRecyclerView);
        RecyclerView prepareOrderRecyclerView( );
        void setOrderRecyclerViewConsumer(Consumer<Pair<OrderItem, String>> notifyOrderAdapterConsumer);
    }
    interface Presenter {
        void orderRecyclerViewOnActivityDestroy();
        void setViewModelState();
        Consumer<Pair<OrderItem, String>> getNotifyOrderAdapterConsumer ();
    }


}
