package cook.presenters;

import android.view.View;

import cook.adapters.CookTablesRecyclerViewAdapter;
import cook.interfaces.OrdersFragmentInterface;
import cook.model.OrdersFragmentModel;
import model.OrderActivityModel;

public class TablesFragmentPresenter implements OrdersFragmentInterface.Presenter {

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
}
