package cook.presenters;

import android.view.View;

import com.example.testfirebase.DocumentListenerService;
import com.example.testfirebase.order.OrderItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cook.adapters.CookTablesRecyclerViewAdapter;
import cook.interfaces.OrdersFragmentInterface;
import cook.model.OrdersFragmentModel;
import interfaces.DocumentListenerServiceInterface;
import model.OrderActivityModel;
import tools.Pair;

public class TablesFragmentPresenter implements OrdersFragmentInterface.Presenter, DocumentListenerServiceInterface.Subscriber {

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
        DocumentListenerService.getService().subscribe(this);
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
    public void notifyMe(Object data) {
        Map<String, Object> notifiable = (Map<String, Object>) data;
        setReadyDishStatus(notifiable);
    }
    @Override
    public void setLatestData(Map<String, Object> latestData) {
        setReadyDishStatus(latestData);
    }
    private void setReadyDishStatus(Map<String, Object> notifiable) {
        String key;
        String dishName;
        ArrayList<Object> orderItemNames;
        OrderActivityModel orderActivityModel = new OrderActivityModel();
        Set<String> keys = notifiable.keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()) {
            key = iterator.next();
            Map<String, Pair<ArrayList<OrderItem>, Boolean>> aaa = orderActivityModel.getNotEmptyTablesOrdersHashMap();
            ArrayList<OrderItem> orderItems = orderActivityModel
                .getNotEmptyTablesOrdersHashMap()
                .get(key).first;
            orderItemNames = (ArrayList<Object>) notifiable.get(key);
//            for(int i = 0; i < orderItemNames.size(); ++i) {
//                dishName = (String) ((ArrayList<?>) notifiable.get(key)).get(i);
//                for(int j = 0; j < orderItems.size(); ++j) {
//                    if( (orderItems.get(i).getName() + OrderActivityModel.DOCUMENT_NAME_DELIMITER + orderItems.get(i).getCommentary()).equals(dishName))
//                        orderItems.get(i).setReady(true);
//                }
//            }
        }
    }
}
