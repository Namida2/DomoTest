package cook.interfaces;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;

public interface DetailOrderActivityInterface {
    interface Model {
        void setRecyclerViewAdapter(DetailOrderItemsRecyclerViewAdapter adapter);
        DetailOrderItemsRecyclerViewAdapter getRecyclerViewAdapter();
    }
    interface View {
        void setViewData();
    }
    interface Presenter {
        DetailOrderItemsRecyclerViewAdapter getAdapter(String tableNumber);
    }
}
