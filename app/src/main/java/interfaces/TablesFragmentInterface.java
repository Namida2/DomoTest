package interfaces;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.TablesRecyclerViewAdapter;
import com.jakewharton.rxbinding4.view.ViewAttachEvent;

public interface TablesFragmentInterface {

    interface Model {
        View getView();
        void setView(View view);
        RecyclerView getRecyclerView();
        void setRecyclerView(RecyclerView recyclerView);
        TablesRecyclerViewAdapter getAdapter();
        void setAdapter(TablesRecyclerViewAdapter adapter);
    }
    interface MyView {
        void setView(View view);
        void startNewActivity(Class activity, int tableNumber);
    }
    interface Presenter {
        void prepare(TablesFragmentInterface.MyView view, LayoutInflater inflater, ViewGroup container);
    }


    interface Adapter {
        interface View {

        }
        interface Presenter {
            void startNewActivity (int tableNumber);

        }
    }

}
