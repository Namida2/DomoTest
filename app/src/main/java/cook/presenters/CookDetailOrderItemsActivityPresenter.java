package cook.presenters;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.testfirebase.DeleteOrderObservable;
import com.example.testfirebase.services.DocumentDishesListenerService;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.example.testfirebase.services.DocumentOrdersListenerService;
import com.example.testfirebase.services.interfaces.DocumentOrdersListenerInterface;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cook.ReadyDish;
import cook.adapters.CookDetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.CookDetailOrderActivityInterface;
import cook.model.DetailOrderActivityModel;
import com.example.testfirebase.services.interfaces.DocumentDishesListenerInterface;

import cook.model.OrdersFragmentModel;
import interfaces.DeleteOrderInterface;
import model.OrderActivityModel;
import model.SplashScreenActivityModel;
import tools.EmployeeData;

import static registration.LogInActivity.TAG;

public class CookDetailOrderItemsActivityPresenter implements CookDetailOrderActivityInterface.Presenter,
    DocumentDishesListenerInterface.Subscriber, DocumentOrdersListenerInterface.Subscriber, DeleteOrderInterface.Subscriber {

    private CookDetailOrderActivityInterface.View view;
    private static CookDetailOrderActivityInterface.Model model;

    public CookDetailOrderItemsActivityPresenter(CookDetailOrderActivityInterface.View view) {
        this.view = view;
        if (model == null) {
            model = new DetailOrderActivityModel();
            CookDetailOrderItemsRecyclerViewAdapter adapter = new CookDetailOrderItemsRecyclerViewAdapter();
            model.setRecyclerViewAdapter(adapter);
        }
        DocumentDishesListenerService.getService().dishesSubscribe(this);
        DocumentOrdersListenerService.getService().ordersSubscribe(this);
        DeleteOrderObservable.getObservable().subscribe(this);
    }
    @Override
    public void setDishState(ReadyDish readyDish) {
        Map<String, Object> readyHaspMap = new HashMap<>();
//        if (readyDish.getOrderItem().isReady()) return;
//        readyDish.getOrderItem().setReady(true);
        readyHaspMap.put(OrderActivityModel.DOCUMENT_READY_FIELD, false);
        model.getRecyclerViewAdapter().notifyItemChanged(readyDish.getPosition());
        model.getDatabase()
            .collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
            .document(readyDish.getTableInfo().getTableName())
            .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME)
            .document(readyDish.getOrderItem().getName()
                + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                + readyDish.getOrderItem().getCommentary())
            .set(readyHaspMap, SetOptions.merge()).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Log.d(TAG,"DetailOrderActivityPresenter.setDishState writing: SUCCESS" );
                    model.getDatabase().collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
                        .document(SplashScreenActivityModel.DOCUMENT_DISHES_LISTENER_NAME)
                        .update(readyDish.getTableInfo().getTableName(), FieldValue.arrayUnion(
                            readyDish.getOrderItem().getName()
                                + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                                + readyDish.getOrderItem().getCommentary()
                        )).addOnCompleteListener(taskNotify -> {
                            if(task.isSuccessful()) {
                                Log.d(TAG,"DetailOrderActivityPresenter.setDishState notify: SUCCESS" );
                            } else Log.d(TAG, "DetailOrderActivityPresenter.setDishState error: " + taskNotify.getException());
                    });
                } else {
                    Log.d(TAG,"DetailOrderActivityPresenter.setDishState error: " + task.getException() );
                }
        });
    }
    @Override
    public CookDetailOrderItemsRecyclerViewAdapter getAdapter(String tableNumber) {
        Log.d(TAG, OrderActivityModel.DOCUMENT_TABLE + tableNumber);
        OrderActivityModel orderActivityModel = new OrderActivityModel();

        ArrayList<TableInfo> tableInfoArrayList = orderActivityModel.getTableInfoArrayList();
        TableInfo tableInfo = new TableInfo();
        for(int i = 0; i < tableInfoArrayList.size(); ++i) {
            if(tableInfoArrayList.get(i).getTableName().equals(OrderActivityModel.DOCUMENT_TABLE + tableNumber)) {
                tableInfo = tableInfoArrayList.get(i);
                break;
            }
        }
        model.getRecyclerViewAdapter().setOrderItemsData(
            orderActivityModel.getNotEmptyTablesOrdersHashMap().get(OrderActivityModel.DOCUMENT_TABLE + tableNumber),
            tableInfo
        );
        return model.getRecyclerViewAdapter();
    }

    @Override
    public void onResume() {
        model.getRecyclerViewAdapter().setAcceptedDishConsumer( dishData -> {
            view.showSetDishReadyDialog(dishData);
        });
    }

    @Override
    public void dishesNotifyMe(Object data) {
        Map<String, Object> notifiable = (Map<String, Object>) data;
        notifyOrderItems(notifiable);
    }
    @Override
    public void dishesSetLatestData(Map<String, Object> latestData) {
        notifyOrderItems(latestData);
    }
    private void notifyOrderItems(Map<String, Object> notifiable) {
        String key;
        String dishName;
        ArrayList<Object> orderItemNames;
        Set<String> keys = notifiable.keySet();
        Iterator<String> iterator = keys.iterator();
        OrderActivityModel orderActivityModel = new OrderActivityModel();
        while(iterator.hasNext()) {
            key = iterator.next();
            try {
                ArrayList<OrderItem> orderItems = orderActivityModel
                    .getNotEmptyTablesOrdersHashMap()
                    .get(key);
                orderItemNames = (ArrayList<Object>) notifiable.get(key);
                for (int i = 0; i < orderItemNames.size(); ++i) {
                    dishName = (String) ((ArrayList<?>) notifiable.get(key)).get(i);
                    for (int j = 0; j < orderItems.size(); ++j) {
                        if ((orderItems.get(j).getName()
                            + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                            + orderItems.get(j).getCommentary()).equals(dishName)) {
                            orderItems.get(j).setReady(true);
                            model.getRecyclerViewAdapter().notifyItemChanged(j);
                        }
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "DetailOrderItemsActivityPresenter.notifyOrderItems: " + e.getMessage());
            }
        }
    }

    @Override
    public void deleteOrder(String tableName) {
        OrderActivityModel orderActivityModel = new OrderActivityModel();
        Map<String, ArrayList<OrderItem>> aaaa = orderActivityModel.getNotEmptyTablesOrdersHashMap();
        model.getRecyclerViewAdapter().setOrderItemsData(new ArrayList<>(), new TableInfo());
        model.getRecyclerViewAdapter().notifyDataSetChanged();
    }

    @Override
    public void ordersNotifyMe(Object data) {
        String tableName = OrderActivityModel.DOCUMENT_TABLE + view.getTableNumber();
        OrderActivityModel orderActivityModel = new OrderActivityModel();
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

        model.getRecyclerViewAdapter().setOrderItemsData(
            orderActivityModel.getNotEmptyTablesOrdersHashMap().get(tableName),
            tableInfo
        );
        model.getRecyclerViewAdapter().notifyDataSetChanged();
    }
}
