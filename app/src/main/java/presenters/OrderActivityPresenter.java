package presenters;

import android.util.Log;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import cook.model.DetailOrderActivityModel;
import interfaces.OrderActivityInterface;
import interfaces.ToolsInterface;
import io.reactivex.rxjava3.core.Observable;
import model.OrderActivityModel;
import model.TablesFragmentModel;
import tools.Pair;

import static registration.LogInActivity.TAG;

public class OrderActivityPresenter implements OrderActivityInterface.Presenter {

    private static final String TAG = "myLogs";
    private static final String GUEST_COUNT_KEY = "guestCount";
    private static final String IS_COMPLETE_KEY = "isComplete";

    private static int TABLE_COUNT = 0;

    private ToolsInterface.Notifiable view;
    private static OrderActivityInterface.Model model;

    public OrderActivityPresenter () {
        TABLE_COUNT = TablesFragmentModel.TABLE_COUNT;
        if (model == null) {
            model = new OrderActivityModel();
            model.setAdapter(new OrderRecyclerViewAdapter());
        }
        Log.d(TAG, "OrderActivityPresenter was create");
    }
    public OrderActivityPresenter (ToolsInterface.Notifiable view) {
        this.view = view;
        TABLE_COUNT = TablesFragmentModel.TABLE_COUNT;
        if (model == null) {
            model = new OrderActivityModel();
            model.setAdapter(new OrderRecyclerViewAdapter());
        }
        Log.d(TAG, "OrderActivityPresenter is created");
    }
    @Override
    public Map<String, Pair<ArrayList<OrderItem>, Boolean>> getNotEmptyTablesOrdersHashMap() {
        return model.getNotEmptyTablesOrdersHashMap();
    }
    @Override
    public void setModelDataState(boolean needToNotifyView) {
        if(view == null && needToNotifyView) {
            Log.d(TAG, "OrderActivityPresenter.setModelDataState: View is null, can not notify view!");
            needToNotifyView = false;
        }
        final boolean finalNeedToNotifyView = needToNotifyView;
        ArrayList<TableInfo> tablesInfo = new ArrayList<>();
        model.setTableInfoArrayList(tablesInfo);
        model.setAllTablesOrdersHashMap(new HashMap<>());
        model.setNotEmptyTablesOrdersHashMap(new HashMap<>());
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
                    tablesInfo.add(tableInfo);
                }
                final int collectionSize = tablesInfo.size();
                getTablesObservable(tablesInfo)
                    .subscribe( tableDocumentPosition -> {
                        TableInfo tableDocument = tablesInfo.get(tableDocumentPosition);
                        model.getDatabase().collection( OrderActivityModel.COLLECTION_ORDERS_NAME )
                            .document( tableDocument.getTableName() )
                            .collection( OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME )
                            .get().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                List<OrderItem> orderItemsList = task1.getResult().toObjects(OrderItem.class);
                                model.getAllTablesOrdersHashMap().put( tableDocument.getTableName(), new Pair<>( new ArrayList<>(orderItemsList), true) );
                            }
                            else  Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + task1.getException());
                           // Map<String, Pair<ArrayList<OrderItem>, Boolean>> a = model.getOrdersHashMap();
                            model.getNotEmptyTablesOrdersHashMap().putAll(model.getAllTablesOrdersHashMap());
                            if (finalNeedToNotifyView  && model.getAllTablesOrdersHashMap().size() == collectionSize) {
                                view.notifyMe();
                            }
                            if( model.getAllTablesOrdersHashMap().size() == collectionSize) {
                                for(int i = 1; i <= TABLE_COUNT; ++i){
                                    if (!model.getAllTablesOrdersHashMap().containsKey( OrderActivityModel.DOCUMENT_TABLE + i )) {
                                        model.getAllTablesOrdersHashMap().put(OrderActivityModel.DOCUMENT_TABLE + i, new Pair<>(new ArrayList<>(), false));
                                        //a = model.getOrdersHashMap();
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
                view.notifyMe();
                //view.onMenuDialogError(ErrorAlertDialog.INTERNET_CONNECTION);
                //add a category "SOMETHING WRONG"
            }
        });
    }
    @Override
    public OrderRecyclerViewAdapter getOrderRecyclerViewAdapter(int tableNumber) {
        if(model.getOrderItemsArrayList(tableNumber) == null) setModelDataState(true);
        else {
            model.getAdapter().setOrderItemsArrayList(model.getOrderItemsArrayList(tableNumber));
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
        Map<String, Object> tableInfoHashMap = new HashMap<>();

        tableInfoHashMap.put(OrderActivityModel.DOCUMENT_GUEST_COUNT_FIELD, guestCount);
        tableInfoHashMap.put(OrderActivityModel.DOCUMENT_IS_COMPLETE_FIELD, false);

        model.getDatabase().collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
            .document(OrderActivityModel.DOCUMENT_TABLE + tableNumber)
            .set(tableInfoHashMap).addOnCompleteListener(guestCountTask -> {
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
    public ArrayList<TableInfo> getTableInfoArrayList() {
        return model.getTableInfoArrayList();
    }

    @Override
    public void orderRecyclerViewOnActivityDestroy(int tableNumber) {
        if ( !model.getOrderInfo(tableNumber).second )
            model.getAllTablesOrdersHashMap().get(OrderActivityModel.DOCUMENT_TABLE + tableNumber).first = new ArrayList<>();
    }
}
