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
import model.SplashScreenActivityModel;
import tools.Pair;
import tools.UserData;

public class TablesFragmentPresenter implements OrdersFragmentInterface.Presenter, DocumentOrdersListenerInterface.Subscriber {

    private OrdersFragmentInterface.View view;
    private static OrdersFragmentInterface.Model model;

    public TablesFragmentPresenter(OrdersFragmentInterface.View view) {
        this.view = view;
        if(model == null) {
            model = new OrdersFragmentModel();
            OrderActivityModel orderActivityModel = new OrderActivityModel();
            model.setOrdersHashMap(orderActivityModel.getNotEmptyTablesOrdersHashMap());
            model.setAdapter(new CookTablesRecyclerViewAdapter(orderActivityModel.getNotEmptyTablesOrdersHashMap()));
        }
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
        if (!UserData.post.equals(SplashScreenActivityModel.WAITER_POST_NAME))
            DocumentOrdersListenerService.getService().ordersShowNotification(tableInfo.getTableName(), DocumentOrdersListenerService.NEW_ORDER);

        Map<String, Pair<ArrayList<OrderItem>, Boolean>> aaa = model.getAllTablesOrdersHashMap();
        Map<String, Pair<ArrayList<OrderItem>, Boolean>> bbb = model.getNotEmptyTablesOrdersHashMap();
        try {
            model.getAllTablesOrdersHashMap().remove(tableInfo.getTableName());
            model.getNotEmptyTablesOrdersHashMap().remove(tableInfo.getTableName());
        } catch (Exception e) {}
        ArrayList<TableInfo> ttt = model.getTableInfoArrayList();
        for(int i = 0; i < model.getTableInfoArrayList().size(); ++i) {
            if (model.getTableInfoArrayList().get(i).getTableName().equals(tableInfo.getTableName())) {
                model.getTableInfoArrayList().remove(i); break;
            }
        }
        model.getTableInfoArrayList().add(tableInfo);
        model.getAllTablesOrdersHashMap().putAll(order);
        model.getNotEmptyTablesOrdersHashMap().putAll(order);
    }
}
