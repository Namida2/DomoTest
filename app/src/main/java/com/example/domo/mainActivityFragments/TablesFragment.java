package com.example.domo.mainActivityFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.domo.R;


import interfaces.TablesFragmentInterface;
import presenters.TablesFragmentPresenter;

public class TablesFragment extends Fragment implements TablesFragmentInterface.MyView {

    public static final String EXTRA_TAG = "tableNumber";
    private TablesFragmentInterface.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TablesFragmentPresenter(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = presenter.getView();
        if(contentView != null) return contentView;
        contentView = inflater.inflate(R.layout.fragment_tables, container, false);
        RecyclerView recyclerView = contentView.findViewById(R.id.tables_recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(presenter.getAdapter());
        presenter.setModelState(contentView);
        return contentView;
    }
    @Override
    public void startNewActivity(Class activity, int tableNumber) {
        getActivity().startActivity(new Intent( getContext(), activity).putExtra(EXTRA_TAG, tableNumber) );
    }
    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }
}