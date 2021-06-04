package presenters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import dialogsTools.ErrorAlertDialog;
import interfaces.OrderActivityInterface;
import io.reactivex.rxjava3.core.Observable;
import model.OrderActivityModel;
import model.TablesFragmentModel;
import tools.Pair;

import static model.MenuDialogModel.DISHES_COLLECTION_NAME;

public class OrderActivityPresenter implements OrderActivityInterface.Presenter {

    private static final String TAG = "myLogs";
    private static final String GUEST_COUNT_KEY = "guestCount";
    private static final String IS_COMPLETE_KEY = "isComplete";

    private static int TABLE_COUNT = 0;

    private OrderActivityInterface.View view;
    private static OrderActivityInterface.Model model;

    public OrderActivityPresenter () {
        TABLE_COUNT = TablesFragmentModel.TABLE_COUNT;
        if (model == null) {
            model = new OrderActivityModel();
            model.setAdapter(new OrderRecyclerViewAdapter());
        }
        Log.d(TAG, "OrderActivityPresenter was create");
    }

    public OrderActivityPresenter (OrderActivityInterface.View view) {
        this.view = view;
        TABLE_COUNT = TablesFragmentModel.TABLE_COUNT;
        if (model == null) {
            model = new OrderActivityModel();
            model.setAdapter(new OrderRecyclerViewAdapter());
        }
        Log.d(TAG, "OrderActivityPresenter was create");
    }

    @Override
    public Map<String, Pair<ArrayList<OrderItem>, Boolean>> getOrdersHashMap() {
        return model.getOrdersHashMap();
    }

    @Override
    public void setModelDataState(boolean needToNotifyView) {
        if(view == null && needToNotifyView) {
            Log.d(TAG, "OrderActivityPresenter.setModelDataState: View is null, can not notify view!");
            needToNotifyView = false;
        }
        final boolean finalNeedToNotifyView = needToNotifyView;
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
                        Log.d(TAG, "OrderActivityPresenter.setModelDataState tableInfo: " + e.getMessage());
                    }
                    tables.add(tableInfo);
                }
                final int collectionSize = tables.size();
                getTablesObservable(tables)
                    .subscribe( tableDocumentPosition -> {
                        TableInfo tableDocument = tables.get(tableDocumentPosition);
                        model.getDatabase().collection( OrderActivityModel.COLLECTION_ORDERS_NAME )
                            .document( tableDocument.getTableName() )
                            .collection( OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME )
                            .get().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                List<OrderItem> orderItemsList = task1.getResult().toObjects(OrderItem.class);
                                model.getOrdersHashMap().put( tableDocument.getTableName(), new Pair<>( new ArrayList<>(orderItemsList), true) );
                            }
                            else  Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + task1.getException());
                            Map<String, Pair<ArrayList<OrderItem>, Boolean>> a = model.getOrdersHashMap();
                            if (finalNeedToNotifyView  && model.getOrdersHashMap().size() == collectionSize)
                                view.setOrdersListForThisTable();
                            if( model.getOrdersHashMap().size() == collectionSize) {
                                for(int i = 1; i <= TABLE_COUNT; ++i){
                                    if (!model.getOrdersHashMap().containsKey( OrderActivityModel.DOCUMENT_TABLE + i )) {
                                        model.getOrdersHashMap().put(OrderActivityModel.DOCUMENT_TABLE + i, new Pair<>(new ArrayList<>(), false));
                                        a = model.getOrdersHashMap();
                                    }
                                }
                                Log.d(TAG, "OrderActivityPresenter.setModelDataState: COMPLETE");
                            }
                        });
                    }, error -> {
                        Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + error.getMessage());
                    }, () -> {
                    });
            }
            else {
                Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + task.getException().toString());
                //view.onMenuDialogError(ErrorAlertDialog.INTERNET_CONNECTION);
                //add a category "SOMETHING WRONG"
            }
        });
    }
    @Override
    public OrderRecyclerViewAdapter getOrderRecyclerViewAdapter(int tableNumber) {
        if(model.getOrderItemsArrayList(tableNumber) == null)
            setModelDataState(true);
        else {
            model.getAdapter().setOrdersArrayList(model.getOrderItemsArrayList(tableNumber));
            return model.getAdapter();
        }
        return null;
    }
    @Override
    public  Consumer<Pair<OrderItem, String>> getOrderNotifyAdapterConsumer() {
        return model.getNotifyOrderAdapterConsumer();
    }
    private Observable<Integer> getTablesObservable (ArrayList<TableInfo> tablesInfo) {
        return Observable.create(emitter -> {
            for (int i = 0 ; i < tablesInfo.size(); ++i) {
                emitter.onNext(i);
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
                            .set(orderItem, SetOptions.merge()).addOnCompleteListener(task -> {
                            if(task.isSuccessful())
                                Log.d(TAG, orderItem.getName()
                                    + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                                    + orderItem.getCommentary() + ": SUCCESS");
                            else Log.d(TAG, "OrderActivityPresenter.writeOrderToDb: " + task.getException());
                        });
                    }
                    Log.d(TAG, "OrderActivityPresenter.acceptAndWriteOrderToDb: COMPLETE");
                }
                else Log.d(TAG, "OrderActivityPresenter.writeOrderToDb: " + guestCountTask.getException());
        });
    }
    @Override
    public void orderRecyclerViewOnActivityDestroy(int tableNumber) {
        if ( !model.getOrderInfo(tableNumber).second )
            model.getOrdersHashMap().get(OrderActivityModel.DOCUMENT_TABLE + tableNumber).first = new ArrayList<>();
    }

}
