package cook.model;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.DetailOrderActivityInterface;

public class DetailOrderActivityModel implements DetailOrderActivityInterface.Model {

    private DetailOrderItemsRecyclerViewAdapter adapter;

    @Override
    public void setRecyclerViewAdapter(DetailOrderItemsRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
}
