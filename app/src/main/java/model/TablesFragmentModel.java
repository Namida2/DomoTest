package model;

import android.view.View;

import com.example.domo.adapters.TablesRecyclerViewAdapter;

import interfaces.TablesFragmentInterface;

public class TablesFragmentModel implements TablesFragmentInterface.Model {

    private TablesRecyclerViewAdapter adapter;
    private View view;
    public static int TABLE_COUNT = 17;

    @Override
    public void setView(View view) {
        this.view = view;
    }
    @Override
    public View getView() {
        return view;
    }
    @Override
    public TablesRecyclerViewAdapter getAdapter() {
        return adapter;
    }
    @Override
    public void setAdapter(TablesRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

}
