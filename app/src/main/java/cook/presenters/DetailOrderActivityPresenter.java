package cook.presenters;

import android.util.Log;

import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.DetailOrderActivityInterface;
import cook.model.DetailOrderActivityModel;
import interfaces.OrderActivityInterface;
import model.OrderActivityModel;
import tools.Pair;

import static registration.LogInActivity.TAG;

public class DetailOrderActivityPresenter implements DetailOrderActivityInterface.Presenter {

    private DetailOrderActivityInterface.View view;
    private static DetailOrderActivityInterface.Model model;

    public DetailOrderActivityPresenter (DetailOrderActivityInterface.View view) {
        this.view = view;
        if (model == null) {
            model = new DetailOrderActivityModel();
            DetailOrderItemsRecyclerViewAdapter adapter = new DetailOrderItemsRecyclerViewAdapter();
            model.setRecyclerViewAdapter(adapter);

        }
    }

    @Override
    public void setDishState(Pair<OrderItem, TableInfo> dishData) {
        Map<String, Object> readyHaspMap = new HashMap<>();
        dishData.first.setReady(true);
        readyHaspMap.put(OrderActivityModel.DOCUMENT_READY_FIELD, true);
        model.getDatabase()
            .collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
            .document(dishData.second.getTableName())
            .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME)
            .document(dishData.first.getName()
                + OrderActivityModel.DOCUMENT_NAME_DELIMITER
                + dishData.first.getCommentary())
            .set(readyHaspMap, SetOptions.merge()).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Log.d(TAG,"DetailOrderActivityPresenter.setDishState: SUCCESS" );
                } else {
                    Log.d(TAG,"DetailOrderActivityPresenter.setDishState: " + task.getException() );
                }
        });
    }

    @Override
    public DetailOrderItemsRecyclerViewAdapter getAdapter(String tableNumber) {
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
            orderActivityModel.getNotEmptyTablesOrdersHashMap().get(OrderActivityModel.DOCUMENT_TABLE + tableNumber).first,
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

}
