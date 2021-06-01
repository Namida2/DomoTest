package presenters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderActivity;
import com.example.testfirebase.order.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import interfaces.OrderActivityInterface;
import model.OrderActivityModel;
import tools.Pair;

public class OrderActivityPresenter implements OrderActivityInterface.Presenter {

    private static final String TAG = "myLogs";
    private OrderActivityInterface.View view;
    private static OrderActivityInterface.Model model;

    public OrderActivityPresenter (OrderActivityInterface.View view, int tableNumber) {
        this.view = view;
        if (model == null) {
            model = new OrderActivityModel();
            setViewModelState();
            model.getAdapter().setOrdersArrayList(model.getOrderArrayList(tableNumber));
        }
        else {
            model.getAdapter().setOrdersArrayList(model.getOrderArrayList(tableNumber));
            view.setOrderRecyclerViewConsumer(model.getNotifyOrderAdapterConsumer());
            view.setOrderRecyclerView(model.getOrderRecyclerView());
        }
    }
    @Override
    public void writeOrderToDb(int tableNumber, int guestCount) {
        ArrayList<OrderItem> orderItems = model.getOrderArrayList(tableNumber);
        Map<String, Object> guestCountHashMap = new HashMap<>();
        guestCountHashMap.put(OrderActivityModel.DOCUMENT_GUEST_COUNT_FIELD, guestCount);
        model.getDb().collection(OrderActivityModel.COLLECTION_ORDERS)
            .document(OrderActivityModel.DOCUMENT_TABLE + tableNumber)
            .set(guestCountHashMap).addOnCompleteListener(guestCountTask -> {
                if(guestCountTask.isSuccessful()) {

                    for(int i = 0; i < orderItems.size(); ++i) {
                        OrderItem orderItem = orderItems.get(i);
                        model.getDb().collection(OrderActivityModel.COLLECTION_ORDERS)
                            .document(OrderActivityModel.DOCUMENT_TABLE + tableNumber)
                            .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS)
                            .document(orderItem.getName()
                                + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                                + orderItem.getCommentary())
                            .set(orderItem).addOnCompleteListener(task -> {
                            if(task.isSuccessful())
                                Log.d(TAG, orderItem.getName()
                                    + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                                    + orderItem.getCommentary() + ": SUCCESS");
                            else Log.d(TAG, "OrderActivityPresenter.writeOrderToDb: " + task.getException());
                        });
                    }
                }

                else Log.d(TAG, "OrderActivityPresenter.writeOrderToDb: " + guestCountTask.getException());
        });
    }
    @Override
    public void orderRecyclerViewOnActivityDestroy(boolean orderWasAccepted, int tableNumber) {
        RecyclerView recyclerView = model.getOrderRecyclerView();
        ((ViewGroup) recyclerView.getParent()).removeView(recyclerView);
        if ( !orderWasAccepted ) {
            model.getOrdersList().remove(Integer.toString(tableNumber));
        }
    }
    @Override
    public void setViewModelState() {
        RecyclerView recyclerView = view.prepareOrderRecyclerView();
        model.setOrderRecyclerView(recyclerView);
        model.setAdapter( (OrderRecyclerViewAdapter) recyclerView.getAdapter() );
        view.setOrderRecyclerViewConsumer(model.getNotifyOrderAdapterConsumer());
    }
    @Override
    public Consumer<Pair<OrderItem, String>> getNotifyOrderAdapterConsumer() {
        return model.getNotifyOrderAdapterConsumer();
    }
}
