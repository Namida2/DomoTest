package presenters;

import android.util.Log;

import com.example.testfirebase.DeleteOrderObservable;
import com.example.testfirebase.order.OrderActivity;
import com.example.testfirebase.services.DocumentDishesListenerService;
import com.example.testfirebase.services.DocumentOrdersListenerService;
import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cook.model.OrdersFragmentModel;
import com.example.testfirebase.services.interfaces.DocumentDishesListenerInterface;
import com.example.testfirebase.services.interfaces.DocumentOrdersListenerInterface;

import dialogsTools.ErrorAlertDialog;
import interfaces.DeleteOrderInterface;
import interfaces.OrderActivityInterface;
import interfaces.ToolsInterface;
import io.reactivex.rxjava3.core.Observable;
import model.OrderActivityModel;
import model.SplashScreenActivityModel;
import model.TablesFragmentModel;
import tools.EmployeeData;

public class OrderActivityPresenter implements OrderActivityInterface.Presenter, DocumentDishesListenerInterface.Subscriber,
    DocumentOrdersListenerInterface.Subscriber, DeleteOrderInterface.Subscriber {

    private static final String TAG = "myLogs";
    public static final String GUEST_COUNT_KEY = "guestCount";
    public static final String IS_COMPLETE_KEY = "isComplete";

    private static int TABLE_COUNT = 0;

    private ToolsInterface.Notifiable view;
    private static OrderActivityInterface.Model model;

    public OrderActivityPresenter (ToolsInterface.Notifiable view) {
        this.view = view;
        TABLE_COUNT = TablesFragmentModel.TABLE_COUNT;
        if (model == null) {
            model = new OrderActivityModel();
            model.setAdapter(new OrderRecyclerViewAdapter());
        }
        if(view.getClass() == OrderActivity.class)
            model.getAdapter().setEditOrderConsumer(((OrderActivityInterface.View) view)::showEditOrderItemDialog);

        DocumentDishesListenerService.getService().dishesSubscribe(this);
        DocumentOrdersListenerService.getService().ordersSubscribe(this);
        DeleteOrderObservable.getObservable().subscribe(this);
        Log.d(TAG, "Subscribe to services: SUCCESS");
        Log.d(TAG, "OrderActivityPresenter: CREATED");
    }

    @Override
    public void notifyOrderItemDataSetChanged(OrderItem orderItem) {
        model.getAdapter().notifyOrderItemDataSetChanged(orderItem);
    }

    @Override
    public void removeOrderItem(OrderItem orderItem) {
        model.getAdapter().removeOrderItem(orderItem);
    }

    @Override
    public void notifyAdapterDataSetChanged(OrderItem orderItem) {
        if ( !model.getAdapter().getOrderItemsArrayList().contains(orderItem))
            model.getAdapter().addOrderItem(orderItem);
        else if (view.getClass() == OrderActivity.class) {
            ((OrderActivityInterface.View) view ).onError(ErrorAlertDialog.DISH_ALREADY_ADDED);
        }
    }
    @Override
    public Map<String, ArrayList<OrderItem>> getNotEmptyTablesOrdersHashMap() {
        return model.getNotEmptyTablesOrdersHashMap();
    }
    @Override
    public void setModelDataState(boolean needToNotifyView) {
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
                        //tableInfo.setIsComplete( (boolean) data.get(IS_COMPLETE_KEY) );
                    } catch (Exception e) {
                        Log.d(TAG, "OrderActivityPresenter.setModelDataState tableInfo: " + e.getMessage());
                        e.printStackTrace();
                    }
                    tablesInfo.add(tableInfo);
                }
                if (tablesInfo.size() == 0) {
                    for(int i = 1; i <= TABLE_COUNT; ++i) {
                        if (!model.getAllTablesOrdersHashMap().containsKey( OrderActivityModel.DOCUMENT_TABLE + i )) {
                            model.getAllTablesOrdersHashMap().put(OrderActivityModel.DOCUMENT_TABLE + i, new ArrayList<>());
                        }
                    }
                    view.notifyMe();
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
                                model.getAllTablesOrdersHashMap().put( tableDocument.getTableName(), new ArrayList<>(orderItemsList));
                            }
                            else  Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + task1.getException());
                            model.getNotEmptyTablesOrdersHashMap().putAll(model.getAllTablesOrdersHashMap());
                            if (finalNeedToNotifyView  && model.getAllTablesOrdersHashMap().size() == collectionSize) {
                                try {
                                    view.notifyMe();
                                } catch (Exception e) {}
                            }
                            if( model.getAllTablesOrdersHashMap().size() == collectionSize) {
                                for(int i = 1; i <= TABLE_COUNT; ++i) {
                                    if (!model.getAllTablesOrdersHashMap().containsKey( OrderActivityModel.DOCUMENT_TABLE + i )) {
                                        model.getAllTablesOrdersHashMap().put(OrderActivityModel.DOCUMENT_TABLE + i, new ArrayList<>());
                                    }
                                }
                                Log.d(TAG, "OrderActivityPresenter.setModelDataState: COMPLETE");
                            }
                        });
                    }, error -> {
                        Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + error.getMessage());
                        error.printStackTrace();
                    }, () -> {
                    });
            }
            else {
                Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + task.getException().toString());
                try {
                    view.notifyMe();
                } catch (Exception e) {}
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
    private Observable<Integer> getTablesObservable (ArrayList<TableInfo> tablesInfo) {
        return Observable.create(emitter -> {
            for (int i = 0 ; i < tablesInfo.size(); ++i) {
                emitter.onNext(i);
            }
        });
    }
    @Override
    public void acceptAndWriteOrderToDb(int tableNumber, int guestCount) {
        ArrayList<OrderItem> aldOrderItems = new ArrayList<>();
        aldOrderItems.addAll(model.getOrderItems(tableNumber));
        ArrayList<OrderItem> newOrderItems = model.getAdapter().getOrderItemsArrayList();
        if (model.getAdapter().getOrderItemsArrayList().size() == 0) return;
        model.getOrderItems(tableNumber).clear();
        model.getOrderItems(tableNumber).addAll(model.getAdapter().getOrderItemsArrayList());
        ArrayList<OrderItem> orderItems = model.getOrderItems(tableNumber);
        Map<String, Object> tableInfoHashMap = new HashMap<>();
        tableInfoHashMap.put(OrderActivityModel.DOCUMENT_GUEST_COUNT_FIELD, guestCount);

        DocumentReference docRefOrderInfo = model.getDatabase()
            .collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
            .document(OrderActivityModel.DOCUMENT_TABLE + tableNumber);

        DocumentReference docRefOrdersListener = model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(SplashScreenActivityModel.DOCUMENT_ORDERS_LISTENER_NAME);

        Map<String, Object> data = new HashMap<>();
        data.put(SplashScreenActivityModel.FIELD_FIELD_TABLE_NAME, null);
        docRefOrdersListener.set(data).addOnCompleteListener(taskNull -> {
            if(taskNull.isSuccessful()) {

                model.getDatabase().collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
                    .document(OrderActivityModel.DOCUMENT_TABLE + tableNumber)
                    .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME)
                    .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        ArrayList<DocumentSnapshot> documentSnapshots = new ArrayList<>();
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            documentSnapshots.add(documentSnapshot);
                        }
                        model.getDatabase().runTransaction(transaction -> {
                            for(DocumentSnapshot documentSnapshot : documentSnapshots) {
                                DocumentReference documentReference = model.getDatabase().collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
                                    .document(OrderActivityModel.DOCUMENT_TABLE + tableNumber)
                                    .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME).document(documentSnapshot.getId());
                                transaction.delete(documentReference);
                            }
                            transaction.set(docRefOrderInfo, tableInfoHashMap);
                            for(int i = 0; i < orderItems.size(); ++i) {
                                OrderItem orderItem = orderItems.get(i);
                                transaction.set(model.getDatabase().collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
                                    .document(OrderActivityModel.DOCUMENT_TABLE + tableNumber)
                                    .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME)
                                    .document(orderItem.getName()
                                        + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                                        + orderItem.getCommentary()), orderItem);
                            }
                            data.put(SplashScreenActivityModel.FIELD_FIELD_TABLE_NAME, OrderActivityModel.DOCUMENT_TABLE + tableNumber);
                            transaction.set(docRefOrdersListener, data);
                            return true;
                        });
                    }
                    else Log.d(TAG, "OrderActivityPresenter.acceptAndWriteOrderToDb: " + task.getException());
                }).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d(TAG, "OrderActivityPresenter.acceptAndWriteOrderToDb: SUCCESS");
                    }
                    else Log.d(TAG, "OrderActivityPresenter.acceptAndWriteOrderToDb: " + task.getException());
                });


            } else Log.d(TAG, "OrderActivityPresenter.acceptAndWriteOrderToDb: " + taskNull.getException());
        });
    }
    @Override
    public ArrayList<TableInfo> getTableInfoArrayList() {
        return model.getTableInfoArrayList();
    }
    @Override
    public void onDestroy() {
        DocumentDishesListenerService.getService().dishesUnSubscribe(this);
        DocumentOrdersListenerService.getService().ordersSubscribe(this);
        Log.d(TAG, "Unsubscribe to services");
    }
    //DocumentListenerServices
    @Override
    public void dishesNotifyMe(Object data) {
        if(model.getNotEmptyTablesOrdersHashMap() == null)
            Log.d(TAG, "OrderActivityPresenter.notifyMe: model is null");
        else {
            Map<String, Object> notifiable = (Map<String, Object>) data;
            notifyOrderItems(notifiable);
        }
    }
    @Override
    public void dishesSetLatestData(Map<String, Object> latestData) {
        if(model.getNotEmptyTablesOrdersHashMap() == null)
            Log.d(TAG, "OrderActivityPresenter.notifyMe: model is null");
        else notifyOrderItems(latestData);
    }
    private void notifyOrderItems(Map<String, Object> notifiable) {
        String key;
        String dishName;
        ArrayList<Object> orderItemNames;
        Set<String> keys = notifiable.keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()) {
            key = iterator.next();
            Map<String, ArrayList<OrderItem>> aaa = model.getNotEmptyTablesOrdersHashMap();
            try {
                ArrayList<OrderItem> orderItems = model
                    .getNotEmptyTablesOrdersHashMap()
                    .get(key);
                orderItemNames = (ArrayList<Object>) notifiable.get(key);
                for(int i = 0; i < orderItemNames.size(); ++i) {
                    dishName = (String) ((ArrayList<?>) notifiable.get(key)).get(i);
                    for(int j = 0; j < orderItems.size(); ++j) {
                        if( (orderItems.get(j).getName()
                            + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                            + orderItems.get(j).getCommentary()).equals(dishName))
                            orderItems.get(j).setReady(true);
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "OrderActivityPresenter.notifyOrderItems: " + e.getMessage());
            }
        }
    }
    @Override
    public void ordersNotifyMe(Object data) {
        Map<String, ArrayList<OrderItem>> order = (Map<String, ArrayList<OrderItem>>) data;
        TableInfo tableInfo = DocumentOrdersListenerService.getService().getTableInfo();
        if (!EmployeeData.post.equals(SplashScreenActivityModel.WAITER_POST_NAME)) {
            String tableNumber = tableInfo.getTableName()
                .substring(tableInfo.getTableName()
                .indexOf(OrdersFragmentModel.DELIMITER) + 1);
            DocumentOrdersListenerService.getService().ordersShowNotification(
                DocumentDishesListenerService.TABLE + tableNumber,
                DocumentOrdersListenerService.NEW_ORDER);
        }
        try {
            model.getAllTablesOrdersHashMap().remove(tableInfo.getTableName());
            model.getNotEmptyTablesOrdersHashMap().remove(tableInfo.getTableName());
        } catch (Exception e) {}
        for(int i = 0; i < model.getTableInfoArrayList().size(); ++i) {
            if (model.getTableInfoArrayList().get(i).getTableName().equals(tableInfo.getTableName())) {
                model.getTableInfoArrayList().remove(i); break;
            }
        }
        model.getTableInfoArrayList().add(tableInfo);
        model.getAllTablesOrdersHashMap().putAll(order);
        model.getNotEmptyTablesOrdersHashMap().putAll(order);
    }

    @Override
    public void deleteOrder(String tableName) {
        Map<String, ArrayList<OrderItem>> aaaa = model.getNotEmptyTablesOrdersHashMap();
        Map<String, ArrayList<OrderItem>> bbbbb = model.getAllTablesOrdersHashMap();
        try {
            model.getAllTablesOrdersHashMap().get(tableName).clear();
            model.getNotEmptyTablesOrdersHashMap().remove(tableName);
        } catch (Exception e) {
            Log.d(TAG, "OrderActivityPresenter.deleteOrder: " + e.getMessage());
        }
        Map<String, ArrayList<OrderItem>> A = model.getNotEmptyTablesOrdersHashMap();
    }
}
