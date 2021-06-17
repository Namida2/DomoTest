package cook.presenters;

import android.util.Log;
import android.view.View;

import com.example.testfirebase.DeleteOrderObservable;
import com.example.testfirebase.services.DocumentDishesListenerService;
import com.example.testfirebase.services.DocumentOrdersListenerService;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cook.adapters.CookTablesRecyclerViewAdapter;
import cook.interfaces.OrdersFragmentInterface;
import cook.model.OrdersFragmentModel;
import com.example.testfirebase.services.interfaces.DocumentDishesListenerInterface;
import com.example.testfirebase.services.interfaces.DocumentOrdersListenerInterface;

import interfaces.DeleteOrderInterface;
import model.OrderActivityModel;
import tools.Pair;

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
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> order = (Map<String, Pair<ArrayList<OrderItem>, Boolean>>) data;
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
        model.getDatabase()
            .collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
            .document(tableName).delete().addOnCompleteListener(task -> {
                if(task.isSuccessful())
                    DeleteOrderObservable.getObservable().notifySubscribers(tableName);
                else Log.d(TAG, "DetailOrderItemsActivityPresenter.deleteOrder: " + task.getException());
        });
    }
    @Override
    public void deleteOrder(String tableName) {
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> aaaa = orderActivityModel.getNotEmptyTablesOrdersHashMap();
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
                    .getNotEmptyTablesOrdersHashMap()
                    .get(key).first;
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
                break;
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
        model.getAdapter().setAcceptOrderArrayList( tableInfo -> {
            view.startDetailOrderActivity(tableInfo);
        });
    }
}
