package interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;

import java.util.ArrayList;
import java.util.function.Consumer;

import tools.Pair;

public interface OrderActivityInterface {
    interface Model {
        void setOrderItemArrayList(ArrayList<Pair<Dish, Pair<String, Integer>>> orderItemArrayList);
        void setOrderRecyclerView(RecyclerView orderRecyclerView);
        void setAdapter(OrderRecyclerViewAdapter adapter);
        void setView(android.view.View view);
        ArrayList<Pair<Dish, Pair<String, Integer>>> getOrderItemArrayList();
        OrderRecyclerViewAdapter getAdapter();
        RecyclerView getOrderRecyclerView();
        android.view.View getView();
        Consumer<Pair<Dish, Pair<String, Integer>>> getNotifyOrderAdapterConsumer ();
    }
    interface View {
        Pair<RecyclerView, OrderRecyclerViewAdapter> prepareOrderRecyclerView();
    }
    interface Presenter {
        void setViewModelState();
    }
}
