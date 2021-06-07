package cook.interfaces;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;

public interface DetailOrderActivityInterface {
    interface Model {
        void setRecyclerViewAdapter(DetailOrderItemsRecyclerViewAdapter adapter);
        DetailOrderItemsRecyclerViewAdapter getRecyclerViewAdapter();
    }
    interface View {
        void showSetDishReadyDialog (String dishName);
        void setViewData();
    }
    interface Presenter {
        void onResume();
        DetailOrderItemsRecyclerViewAdapter getAdapter(String tableNumber);
    }
}
