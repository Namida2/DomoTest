package cook.presenters;

import android.util.Log;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.DetailOrderActivityInterface;
import cook.model.DetailOrderActivityModel;
import model.OrderActivityModel;

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
    public DetailOrderItemsRecyclerViewAdapter getAdapter(String tableNumber) {
        Log.d(TAG, OrderActivityModel.DOCUMENT_TABLE + tableNumber);
        OrderActivityModel orderActivityModel = new OrderActivityModel();
        model.getRecyclerViewAdapter().setOrderItemsData(
            orderActivityModel.getNotEmptyTablesOrdersHashMap().get(OrderActivityModel.DOCUMENT_TABLE + tableNumber).first,
            orderActivityModel.getTableInfoArrayList()
        );
        return model.getRecyclerViewAdapter();
    }

    @Override
    public void onResume() {
        model.getRecyclerViewAdapter().setAcceptedDishConsumer( dishData -> {

        });
    }

}
