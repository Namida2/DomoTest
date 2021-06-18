package cook.presenters;

import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import com.example.testfirebase.DeleteOrderObservable;
import com.example.testfirebase.services.DocumentDishesListenerService;
import com.example.testfirebase.services.DocumentOrdersListenerService;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cook.adapters.CookTablesRecyclerViewAdapter;
import cook.interfaces.OrdersFragmentInterface;
import cook.model.OrdersFragmentModel;
import com.example.testfirebase.services.interfaces.DocumentDishesListenerInterface;
import com.example.testfirebase.services.interfaces.DocumentOrdersListenerInterface;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import interfaces.DeleteOrderInterface;
import model.OrderActivityModel;
import model.SplashScreenActivityModel;

import static registration.LogInActivity.TAG;

public class OrdersFragmentPresenter implements OrdersFragmentInterface.Presenter, DocumentOrdersListenerInterface.Subscriber,
    DocumentDishesListenerInterface.Subscriber, DeleteOrderInterface.Subscriber {

    private OrdersFragmentInterface.View view;
    private OrderActivityModel orderActivityModel;
    private static OrdersFragmentInterface.Model model;

    public OrdersFragmentPresenter(OrdersFragmentInterface.View view) {
        this.view = view;
        orderActivityModel = new OrderActivityModel();
        if(model == null) {
            model = new OrdersFragmentModel();
            model.setOrdersHashMap(orderActivityModel.getNotEmptyTablesOrdersHashMap());
            model.setAdapter(new CookTablesRecyclerViewAdapter());
        }
        model.getAdapter().setOrdersArrayList(orderActivityModel.getNotEmptyTablesOrdersHashMap());
        model.getAdapter().notifyDataSetChanged();
        DocumentOrdersListenerService.getService().ordersSubscribe(this);
        DocumentDishesListenerService.getService().dishesSubscribe(this);
        DeleteOrderObservable.getObservable().subscribe(this);
    }
    @Override
    public void ordersNotifyMe(Object data) {
        Map<String, ArrayList<OrderItem>> order = (Map<String, ArrayList<OrderItem>>) data;
        TableInfo tableInfo = DocumentOrdersListenerService.getService().getTableInfo();
        try {
            orderActivityModel.getAllTablesOrdersHashMap().remove(tableInfo.getTableName());
            orderActivityModel.getNotEmptyTablesOrdersHashMap().remove(tableInfo.getTableName());
        } catch (Exception e) {}
        for(int i = 0; i < orderActivityModel.getTableInfoArrayList().size(); ++i) {
            if (orderActivityModel.getTableInfoArrayList().get(i).getTableName().equals(tableInfo.getTableName())) {
                orderActivityModel.getTableInfoArrayList().remove(i); break;
            }
        }
        orderActivityModel.getTableInfoArrayList().add(tableInfo);
        orderActivityModel.getAllTablesOrdersHashMap().putAll(order);
        orderActivityModel.getNotEmptyTablesOrdersHashMap().putAll(order);
        model.getAdapter().setOrdersArrayList(orderActivityModel.getNotEmptyTablesOrdersHashMap());
        model.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void deleteOrderFromDatabase(String tableNumber) {
        String tableName = OrderActivityModel.DOCUMENT_TABLE + tableNumber;
        DocumentReference docRefOrdersTableName =  model.getDatabase()
            .collection(OrderActivityModel.COLLECTION_ORDERS_NAME).document(tableName);

        DocumentReference docRefListenersDishesListener = model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(SplashScreenActivityModel.DOCUMENT_DISHES_LISTENER_NAME);

        DocumentReference docRefListenersDeleteOrderListener = model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(orderActivityModel.DOCUMENT_DELETE_ORDER_LISTENER);

        Map<String, Object> deleteTableName = new HashMap<>();
        deleteTableName.put(SplashScreenActivityModel.FIELD_TABLE_NAME, null);
        docRefListenersDeleteOrderListener.set(deleteTableName);

        model.getDatabase().collection(OrderActivityModel.COLLECTION_ORDERS_NAME).document(tableName)
            .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME)
            .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                ArrayList<DocumentSnapshot> documentSnapshots = new ArrayList<>();
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    documentSnapshots.add(documentSnapshot);
                }
                model.getDatabase().runTransaction(transaction -> { // не ходить лишний рах в бд, а взять локальные данные для удаления
                    for(DocumentSnapshot documentSnapshot : documentSnapshots) {
                        DocumentReference documentReference = model.getDatabase().collection(OrderActivityModel.COLLECTION_ORDERS_NAME).document(tableName)
                            .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME).document(documentSnapshot.getId());
                        transaction.delete(documentReference);
                    }
                    DocumentReference documentReferenceDeleteOrder = model.getDatabase()
                        .collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
                        .document(OrderActivityModel.DOCUMENT_DELETE_ORDER_LISTENER);
                    transaction.delete(docRefOrdersTableName)
                        .update(docRefListenersDishesListener, tableName, null)
                        .update(docRefListenersDeleteOrderListener, SplashScreenActivityModel.FIELD_TABLE_NAME, tableName);
                    return true;
                });
            }
            else Log.d(TAG, "OrdersFragmentPresenter.deleteOrderFromDatabase: " + task.getException());
        }).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                Log.d(TAG, "OrdersFragmentPresenter.deleteOrderFromDatabase: SUCCESS");
            else Log.d(TAG, "OrdersFragmentPresenter.deleteOrderFromDatabase: " + task.getException());
        });
    }
    @Override
    public void deleteOrder(String tableName) {
        Map<String, ArrayList<OrderItem>> aaaa = orderActivityModel.getNotEmptyTablesOrdersHashMap();

        model.getAdapter().setOrdersArrayList(new HashMap<>());
        model.getAdapter().notifyDataSetChanged();
        model.getAdapter().setOrdersArrayList(orderActivityModel.getNotEmptyTablesOrdersHashMap());
        model.getAdapter().notifyDataSetChanged();
    }
    private void notifyOrderItems(Map<String, Object> notifiable) {
        String key;
        String dishName;
        ArrayList<Object> orderItemNames;
        Set<String> keys = notifiable.keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()) {
            key = iterator.next();
            try {
                ArrayList<OrderItem> orderItems = orderActivityModel
                    .getNotEmptyTablesOrdersHashMap().get(key);
                orderItemNames = (ArrayList<Object>) notifiable.get(key);
                for (int i = 0; i < orderItemNames.size(); ++i) {
                    dishName = (String) ((ArrayList<?>) notifiable.get(key)).get(i);
                    for (int j = 0; j < orderItems.size(); ++j) {
                        if ((orderItems.get(j).getName()
                            + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                            + orderItems.get(j).getCommentary()).equals(dishName)) {
                            orderItems.get(j).setReady(true);
                        }
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "DetailOrderItemsActivityPresenter.notifyOrderItems: " + e.getMessage());
            }
        }
    }
    @Override
    public void setModelState(View view) {
        model.setView(view);
    }
    @Override
    public CookTablesRecyclerViewAdapter getAdapter( ) {
        return model.getAdapter();
    }
    @Override
    public View getView() {
        return model.getView();
    }
    @Override
    public void setView(View view) {
        model.setView(view);
    }
    @Override
    public void dishesNotifyMe(Object data) {
        Map<String, Object> notifiable = (Map<String, Object>) data;
        model.getAdapter().setOrdersArrayList(new OrderActivityModel().getNotEmptyTablesOrdersHashMap());
        for(String key : notifiable.keySet()) {
            model.getAdapter().notifyTable(key);
        }
    }
    @Override
    public void dishesSetLatestData(Map<String, Object> latestData) {
        notifyOrderItems(latestData);
        model.getAdapter().setOrdersArrayList(new OrderActivityModel().getNotEmptyTablesOrdersHashMap());
        for(String key : latestData.keySet()) {
            model.getAdapter().notifyTable(key);
        }
    }
    @Override
    public void onResume() {
        model.getAdapter().setAcceptOrderArrayListConsumer(tableInfo -> {
            view.startDetailOrderActivity(tableInfo);
        });
    }
}
