package presenters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import dialogsTools.ErrorAlertDialog;
import interfaces.OrderActivityInterface;
import io.reactivex.rxjava3.core.Observable;
import model.OrderActivityModel;
import tools.Pair;

import static model.MenuDialogModel.DISHES_COLLECTION_NAME;

public class OrderActivityPresenter implements OrderActivityInterface.Presenter {

    private static final String TAG = "myLogs";
    private static final String GUEST_COUNT_KEY = "guestCount";
    private static final String IS_COMPLETE_KEY = "isComplete";

    private OrderActivityInterface.View view;
    private static OrderActivityInterface.Model model;

    public OrderActivityPresenter (OrderActivityInterface.View view, int tableNumber) {
        this.view = view;
        if (model == null) {
            model = new OrderActivityModel();
            setModelDataState();
            setViewModelState();
            model.getAdapter().setOrdersArrayList(model.getOrderInfo(tableNumber).first);
        }
        else {
            model.getAdapter().setOrdersArrayList(model.getOrderInfo(tableNumber).first);
            view.setOrderRecyclerViewConsumer(model.getNotifyOrderAdapterConsumer());
            view.setOrderRecyclerView(model.getOrderRecyclerView());
        }
    }

    public void setModelDataState() {
        ArrayList<TableInfo> tables = new ArrayList<>();

        model.getDatabase().collection(OrderActivityModel.COLLECTION_ORDERS_NAME).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    TableInfo tableInfo = new TableInfo();
                    tableInfo.setTableName(documentSnapshot.getId());
                    Map<String, Object> data = documentSnapshot.getData();
                    try {
                        tableInfo.setGuestCount( (long) data.get(GUEST_COUNT_KEY) );
                        tableInfo.setIsComplete( (boolean) data.get(IS_COMPLETE_KEY) );
                    } catch (Exception e) {
                        Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + e.getMessage());
                    }
                    tables.add(tableInfo);
                }

                getTablesObservable(tables)
                    .subscribe( tableDocument -> {
                        model.getDatabase().collection( OrderActivityModel.COLLECTION_ORDERS_NAME )
                            .document( tableDocument.getTableName() )
                            .collection( OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME )
                            .get().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                List<OrderItem> orderItemsList = task1.getResult().toObjects(OrderItem.class);
                                model.getOrdersHashMap().put( tableDocument.getTableName(), new Pair<>( new ArrayList<>(orderItemsList), true) );
                            }
                            else  Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + task1.getException());
                        });
                    }, error -> {
                        Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + error.getMessage());
                    }, () -> {
                    });
            }
            else {
                Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + task.getException().toString());
                //view.onMenuDialogError(ErrorAlertDialog.INTERNET_CONNECTION);
                // add a category "SOMETHING WRONG"
            }
        });
    }

    private Observable<TableInfo> getTablesObservable (ArrayList<TableInfo> tablesInfo) {
        return Observable.create(emitter -> {
            for (int i = 0 ; i < tablesInfo.size(); ++i) {
                emitter.onNext(tablesInfo.get(i));
            }
        });
    }

    @Override
    public void acceptAndWriteOrderToDb(int tableNumber, int guestCount) {
        model.getOrderInfo(tableNumber).second = true;
        ArrayList<OrderItem> orderItems = model.getOrderInfo(tableNumber).first;
        Map<String, Object> guestCountHashMap = new HashMap<>();
        guestCountHashMap.put(OrderActivityModel.DOCUMENT_GUEST_COUNT_FIELD, guestCount);
        model.getDatabase().collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
            .document(OrderActivityModel.DOCUMENT_TABLE + tableNumber)
            .set(guestCountHashMap).addOnCompleteListener(guestCountTask -> {
                if(guestCountTask.isSuccessful()) {

                    for(int i = 0; i < orderItems.size(); ++i) {
                        OrderItem orderItem = orderItems.get(i);
                        model.getDatabase().collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
                            .document(OrderActivityModel.DOCUMENT_TABLE + tableNumber)
                            .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME)
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
    public void orderRecyclerViewOnActivityDestroy(int tableNumber) {
        RecyclerView recyclerView = model.getOrderRecyclerView();
        ((ViewGroup) recyclerView.getParent()).removeView(recyclerView);
        if ( !model.getOrderInfo(tableNumber).second )
            model.getOrdersHashMap().remove(OrderActivityModel.DOCUMENT_TABLE + tableNumber);
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
