package cook.presenters;

import android.view.View;

import com.example.testfirebase.DocumentOrdersListenerService;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;

import java.util.ArrayList;
import java.util.Map;

import cook.adapters.CookTablesRecyclerViewAdapter;
import cook.interfaces.OrdersFragmentInterface;
import cook.model.OrdersFragmentModel;
import interfaces.DocumentOrdersListenerInterface;
import model.OrderActivityModel;
import tools.Pair;

public class TablesFragmentPresenter implements OrdersFragmentInterface.Presenter, DocumentOrdersListenerInterface.Subscriber {

    private OrdersFragmentInterface.View view;
    private static OrdersFragmentInterface.Model model;

    public TablesFragmentPresenter(OrdersFragmentInterface.View view) {
        this.view = view;
        OrderActivityModel orderActivityModel = new OrderActivityModel();
        if(model == null) {
            model = new OrdersFragmentModel();
            model.setOrdersHashMap(orderActivityModel.getNotEmptyTablesOrdersHashMap());
            model.setAdapter(new CookTablesRecyclerViewAdapter());
            model.getAdapter().setOrdersArrayList(orderActivityModel.getNotEmptyTablesOrdersHashMap());
        } else model.getAdapter().setOrdersArrayList(orderActivityModel.getNotEmptyTablesOrdersHashMap());
        DocumentOrdersListenerService.getService().ordersSubscribe(this);
    }
    @Override
    public void onResume() {
        model.getAdapter().setAcceptOrderArrayList(tableNumber -> {
            view.startDetailOrderActivity(tableNumber);
        });
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
    public void ordersNotifyMe(Object data) {
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> order = (Map<String, Pair<ArrayList<OrderItem>, Boolean>>) data;
        TableInfo tableInfo = DocumentOrdersListenerService.getService().getTableInfo();
        OrderActivityModel orderActivityModel = new OrderActivityModel();
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> aaa = orderActivityModel.getAllTablesOrdersHashMap();
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> bbb = orderActivityModel.getNotEmptyTablesOrdersHashMap();
        try {
            orderActivityModel.getAllTablesOrdersHashMap().remove(tableInfo.getTableName());
            orderActivityModel.getNotEmptyTablesOrdersHashMap().remove(tableInfo.getTableName());
        } catch (Exception e) {}
        ArrayList<TableInfo> ttt = orderActivityModel.getTableInfoArrayList();
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
}
