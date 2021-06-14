package presenters;

import android.view.View;

import com.example.domo.adapters.TablesRecyclerViewAdapter;
import com.example.domo.order.OrderActivity;

import interfaces.TablesFragmentInterface;
import model.TablesFragmentModel;

public class TablesFragmentPresenter implements TablesFragmentInterface.Presenter {

    private static TablesFragmentInterface.Model model;
    private final TablesFragmentInterface.MyView myView;

    public TablesFragmentPresenter (TablesFragmentInterface.MyView view) {
        this.myView = view;
        if (model == null) {
            model = new TablesFragmentModel();
            model.setAdapter(new TablesRecyclerViewAdapter());
        }
    }
    @Override
    public void setModelState(View view) {
        model.setView(view);
    }
    @Override
    public View getView() {
        return model.getView();
    }
    @Override
    public TablesRecyclerViewAdapter getAdapter() {
        return model.getAdapter();
    }
    @Override
    public void onResume() {
        model.getAdapter().setAcceptTableNumber(tableNumber -> {
            myView.startNewActivity(OrderActivity.class, tableNumber);
        });
        model.getAdapter().resetIsPressed();
    }
}
