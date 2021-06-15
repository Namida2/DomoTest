package presenters;


import android.os.Build;

import androidx.annotation.RequiresApi;

import cook.adapters.CookTablesRecyclerViewAdapter;
import interfaces.CheckFragmentInterface;
import model.CheckFragmentModel;
import model.OrderActivityModel;

public class CheckFragmentPresenter implements CheckFragmentInterface.Presenter {

    private CheckFragmentInterface.View view;
    private CheckFragmentInterface.Model model;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CheckFragmentPresenter (CheckFragmentInterface.View view) {
        this.view = view;
        if(model == null) {
            OrderActivityModel orderActivityModel = new OrderActivityModel();
            model = new CheckFragmentModel();
            model.setAdapter(new CookTablesRecyclerViewAdapter());
            model.getAdapter().setOrdersArrayList(orderActivityModel.getTablesWithAllReadyDishes());
        }
    }
}
