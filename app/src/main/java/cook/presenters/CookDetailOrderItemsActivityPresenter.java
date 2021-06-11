package cook.presenters;

import android.util.Log;

import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cook.ReadyDish;
import cook.adapters.CookDetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.CookDetailOrderActivityInterface;
import cook.model.DetailOrderActivityModel;
import model.OrderActivityModel;
import model.SplashScreenActivityModel;

import static registration.LogInActivity.TAG;

public class CookDetailOrderItemsActivityPresenter implements CookDetailOrderActivityInterface.Presenter {

    private CookDetailOrderActivityInterface.View view;
    private static CookDetailOrderActivityInterface.Model model;

    public CookDetailOrderItemsActivityPresenter(CookDetailOrderActivityInterface.View view) {
        this.view = view;
        if (model == null) {
            model = new DetailOrderActivityModel();
            CookDetailOrderItemsRecyclerViewAdapter adapter = new CookDetailOrderItemsRecyclerViewAdapter();
            model.setRecyclerViewAdapter(adapter);
        }
    }
    @Override
    public void setDishState(ReadyDish readyDish) {
        Log.d(TAG, Thread.currentThread().getName());
        Map<String, Object> readyHaspMap = new HashMap<>();
        readyDish.getOrderItem().setReady(true);
        ////
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
                            }
                            else Log.d(TAG, "DetailOrderActivityPresenter.setDishState error: " + taskNotify.getException());
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
