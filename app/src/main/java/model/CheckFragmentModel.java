package model;

import android.view.View;

import cook.adapters.CookTablesRecyclerViewAdapter;
import interfaces.CheckFragmentInterface;

public class CheckFragmentModel implements CheckFragmentInterface.Model {

    private View view;
    private CookTablesRecyclerViewAdapter adapter;



    @Override
    public View getView() {
        return view;
    }
    @Override
    public void setView(View view) {
        this.view = view;
    }
    @Override
    public CookTablesRecyclerViewAdapter getAdapter() {
        return adapter;
    }
    @Override
    public void setAdapter(CookTablesRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
}
