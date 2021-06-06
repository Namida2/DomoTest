package cook.presenters;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.DetailOrderActivityInterface;

public class DetailOrderActivityPresenter implements DetailOrderActivityInterface.Presenter {

    private DetailOrderActivityInterface.View view;
    private static DetailOrderActivityInterface.Model model;

    public DetailOrderActivityPresenter (DetailOrderActivityInterface.View view) {
        this.view = view;
        model.setRecyclerViewAdapter(new DetailOrderItemsRecyclerViewAdapter());
    }

}
