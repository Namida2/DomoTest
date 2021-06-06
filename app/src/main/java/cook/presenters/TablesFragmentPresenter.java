package cook.presenters;

import android.view.View;

import cook.adapters.CookTablesRecyclerViewAdapter;
import cook.interfaces.TablesFragmentInterface;
import cook.model.TablesFragmentModel;
import interfaces.OrderActivityInterface;
import presenters.OrderActivityPresenter;

public class TablesFragmentPresenter implements TablesFragmentInterface.Presenter {

    private TablesFragmentInterface.View view;
    private static TablesFragmentInterface.Model model;
    private OrderActivityInterface.Presenter orderActivityPresenter;

    public TablesFragmentPresenter(TablesFragmentInterface.View view) {
        this.view = view;
        if(model == null) {
            model = new TablesFragmentModel();
            orderActivityPresenter = new OrderActivityPresenter();
            model.setOrdersHashMap(orderActivityPresenter.getNotEmptyTablesOrdersHashMap());
            model.setAdapter(new CookTablesRecyclerViewAdapter(orderActivityPresenter.getNotEmptyTablesOrdersHashMap()));
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
