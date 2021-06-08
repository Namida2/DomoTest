package presenters;

import android.util.Log;

import com.example.testfirebase.adapters.DetailOrderItemsRecyclerViewAdapter;
import com.example.testfirebase.order.TableInfo;

import java.util.ArrayList;

import interfaces.DetailOrderItemsActivityInterface;
import model.DetailOrderItemsActivityModel;
import model.OrderActivityModel;

import static registration.LogInActivity.TAG;

public class DetailOrderItemsActivityPresenter implements DetailOrderItemsActivityInterface.Presenter {

    private DetailOrderItemsActivityInterface.View view;
    private DetailOrderItemsActivityInterface.Model model;

    public DetailOrderItemsActivityPresenter (DetailOrderItemsActivityInterface.View view) {
        this.view = view;
        if(model == null) {
            model = new DetailOrderItemsActivityModel();
            model.setRecyclerViewAdapter(new DetailOrderItemsRecyclerViewAdapter());
        }
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
}
