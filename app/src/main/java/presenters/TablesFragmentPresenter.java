package presenters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.adapters.TablesRecyclerViewAdapter;

import interfaces.TablesFragmentInterface;
import model.TablesFragmentModel;

import static registration.LogInActivity.TAG;


public class TablesFragmentPresenter implements TablesFragmentInterface.Presenter {

    private static TablesFragmentInterface.Model model;
    private final TablesFragmentInterface.MyView myView;

    public TablesFragmentPresenter (TablesFragmentInterface.MyView view) {
        this.myView = view;
        if (model == null)
            model = new TablesFragmentModel();
    }

    @Override
    public void prepare(TablesFragmentInterface.MyView view, LayoutInflater inflater, ViewGroup container) {
        View mView;
        RecyclerView recyclerView;
        if(model.getView() == null) {
            Log.d(TAG, "View is null");
            mView = inflater.inflate(R.layout.fragment_tables, container, false);
            model.setView(mView);
        }
        else mView = model.getView();
        if(model.getAdapter() == null) {
            Log.d(TAG, "Adapter is null");
            model.setAdapter(new TablesRecyclerViewAdapter(view));
        }
        if(model.getRecyclerView() == null) {
            Log.d(TAG, "RecyclerView is null");
            recyclerView = mView.findViewById(R.id.tables_recycle_view);
            recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 2));
            recyclerView.setAdapter(model.getAdapter());
            model.setRecyclerView(recyclerView);
        }
        myView.setView(model.getView());
    }

    @Override
    public void onResume() {
        model.getAdapter().resetIsPressed();
    }

}
