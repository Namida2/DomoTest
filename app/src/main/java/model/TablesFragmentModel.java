package model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.TablesRecyclerViewAdapter;

import interfaces.TablesFragmentInterface;

public class TablesFragmentModel implements TablesFragmentInterface.Model {
    private RecyclerView recyclerView;
    private TablesRecyclerViewAdapter adapter;
    private View view;

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    @Override
    public TablesRecyclerViewAdapter getAdapter() {
        return adapter;
    }
    @Override
    public void setAdapter(TablesRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
    @Override
    public View getView() {
        return view;
    }
    @Override
    public void setView(View view) {
        this.view = view;
    }

}
