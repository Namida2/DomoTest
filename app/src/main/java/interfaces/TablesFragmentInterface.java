package interfaces;

import android.view.View;

import com.example.testfirebase.adapters.TablesRecyclerViewAdapter;

public interface TablesFragmentInterface {

    interface Model {
        void setView(View view);
        View getView();
        TablesRecyclerViewAdapter getAdapter();
        void setAdapter(TablesRecyclerViewAdapter adapter);
    }
    interface MyView {
        void startNewActivity(Class activity, int tableNumber);
    }
    interface Presenter {
        void setModelState(View view);
        View getView();
        TablesRecyclerViewAdapter getAdapter();
        void onResume();
    }


}
