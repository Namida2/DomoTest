package com.example.testfirebase.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.OrderActivity;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import interfaces.TablesFragmentInterface;
import io.reactivex.rxjava3.disposables.Disposable;

import static registration.LogInActivity.TAG;

public class TablesRecyclerViewAdapter extends RecyclerView.Adapter<TablesRecyclerViewAdapter.ViewHolder> implements TablesFragmentInterface.Adapter.Presenter {

    private int TABLES_COUNT = 17;
    private TablesFragmentInterface.MyView view;
    private Disposable disposable;
    public TablesRecyclerViewAdapter(TablesFragmentInterface.MyView view) {
        this.view = view;
    }

    @Override
    public void startNewActivity(int tableNumber) {
        view.startNewActivity(OrderActivity.class, tableNumber);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements TablesFragmentInterface.Adapter.View {
        public TextView tableNumber;
        public ConstraintLayout container;
        public TablesFragmentInterface.Adapter.Presenter presenter;
        public ViewHolder(@NonNull @NotNull View itemView, TablesFragmentInterface.Adapter.Presenter presenter) {
            super(itemView);
            this.presenter = presenter;
            tableNumber = itemView.findViewById(R.id.table_number);
            container = itemView.findViewById(R.id.table_container);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)  {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_table, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.tableNumber.setText(Integer.toString(position + 1));
        disposable = RxView.clicks(holder.container)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(next -> {
                    holder.presenter.startNewActivity(position + 1);
                }, error -> {
                    Log.d(TAG, error.getMessage());
                }, () -> {
                });
    }

    @Override
    public int getItemCount() {
        return TABLES_COUNT;
    }

}
