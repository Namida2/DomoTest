package presenters;

import android.util.Log;

import com.example.testfirebase.DocumentListenerService;
import com.example.testfirebase.adapters.DetailOrderItemsRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import interfaces.DetailOrderItemsActivityInterface;
import interfaces.DocumentListenerServiceInterface;
import model.DetailOrderItemsActivityModel;
import model.OrderActivityModel;
import tools.Pair;

import static android.content.Context.KEYGUARD_SERVICE;
import static registration.LogInActivity.TAG;

public class DetailOrderItemsActivityPresenter implements DetailOrderItemsActivityInterface.Presenter, DocumentListenerServiceInterface.Subscriber{

    private DetailOrderItemsActivityInterface.View view;
    private DetailOrderItemsActivityInterface.Model model;

    public DetailOrderItemsActivityPresenter (DetailOrderItemsActivityInterface.View view) {
        this.view = view;
        if(model == null) {
            model = new DetailOrderItemsActivityModel();
            model.setRecyclerViewAdapter(new DetailOrderItemsRecyclerViewAdapter());
        }
        DocumentListenerService.getService().subscribe(this);
    }

    @Override
    public void onDestroy() {
        DocumentListenerService.getService().unSubscribe(this);
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
        if(tableInfo.getTableName() == null)
            Log.d(TAG, "DetailOrderItemsActivityPresenter.getAdapter: table not found." );
        model.getRecyclerViewAdapter().setOrderItemsData(
            orderActivityModel.getNotEmptyTablesOrdersHashMap().get(OrderActivityModel.DOCUMENT_TABLE + tableNumber).first,
            tableInfo
        );
        return model.getRecyclerViewAdapter();
    }

    @Override
    public void notifyMe(Object data) {
        Map<String, Object> notifiable = (Map<String, Object>) data;
    }

    @Override
    public void setLatestData(Map<String, Object> latestData) {
        String key;
        String dishName;
        ArrayList<Object> orderItemNames;
        OrderActivityModel orderActivityModel = new OrderActivityModel();
        Set<String> keys = latestData.keySet();

        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()) {
            key = iterator.next();
            //Map<String, Pair<ArrayList<OrderItem>, Boolean>> aaa = orderActivityModel.getNotEmptyTablesOrdersHashMap();
            ArrayList<OrderItem> orderItemsArrayList = orderActivityModel
                .getNotEmptyTablesOrdersHashMap()
                .get(key).first;
            orderItemNames = (ArrayList<Object>) latestData.get(key);
            //dishName
            for(int i = 0; i < orderItemsArrayList.size(); ++i) {
                if (orderItemsArrayList.get(i).getName() == name) {

                }
            }
        }
    }
}
