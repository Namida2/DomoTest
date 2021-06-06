package cook.presenters;

import android.util.Log;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.OrderItem;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.DetailOrderActivityInterface;
import cook.model.DetailOrderActivityModel;
import model.OrderActivityModel;
import presenters.OrderActivityPresenter;

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
        model.getRecyclerViewAdapter().setOrderItemsArrayList(

           new OrderActivityModel().getNotEmptyTablesOrdersHashMap().get(OrderActivityModel.DOCUMENT_TABLE + tableNumber).first
        );
        return model.getRecyclerViewAdapter();
    }

}
