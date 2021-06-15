package interfaces;

import cook.adapters.CookTablesRecyclerViewAdapter;

public interface CheckFragmentInterface {
    interface Model {
        android.view.View getView();
        void setView(android.view.View view);
        CookTablesRecyclerViewAdapter getAdapter();
        void setAdapter(CookTablesRecyclerViewAdapter adapter);
    }
    interface View {

    }
    interface Presenter {
        void getView();
    }
}
