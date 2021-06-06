package cook.model;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.DetailOrderActivityInterface;

public class DetailOrderActivityModel implements DetailOrderActivityInterface.Model {

    private DetailOrderItemsRecyclerViewAdapter adapter;
    public static final String EXTRA_TAG = "extra_tag";

    @Override
    public void setRecyclerViewAdapter(DetailOrderItemsRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public DetailOrderItemsRecyclerViewAdapter getRecyclerViewAdapter() {
        return adapter;
    }
}
