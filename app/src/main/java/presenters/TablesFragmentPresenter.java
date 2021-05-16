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
    private TablesFragmentInterface.MyView view;

    public TablesFragmentPresenter (TablesFragmentInterface.MyView view) {
        this.view = view;
        if (model == null)
            model = new TablesFragmentModel();
    }

    @Override
    public void prepare(LayoutInflater inflater, ViewGroup container) {
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
            model.setAdapter(new TablesRecyclerViewAdapter());
        }
        if(model.getRecyclerView() == null) {
            Log.d(TAG, "RecyclerView is null");
            recyclerView = mView.findViewById(R.id.tables_recycle_view);
            recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 2));
            recyclerView.setAdapter(model.getAdapter());
            model.setRecyclerView(recyclerView);
        }
        view.setView(model.getView());
        view.setRecyclerView(model.getRecyclerView());
        view.setAdapter(model.getAdapter());
    }
}
