package com.example.testfirebase.mainActivityFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testfirebase.R;
import com.example.testfirebase.adapters.TablesRecyclerViewAdapter;

import interfaces.TablesFragmentInterface;
import presenters.TablesFragmentPresenter;

public class TablesFragment extends Fragment implements TablesFragmentInterface.MyView {

    private TablesFragmentInterface.Presenter presenter;

    private RecyclerView recyclerView;
    private TablesRecyclerViewAdapter adapter;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TablesFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter.prepare(this, inflater, container);
        return view;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void setAdapter(TablesRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void startNewActivity(Class activity) {
        getActivity().startActivity(new Intent(getActivity(), activity));
    }
}