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

public class TablesRecyclerViewAdapter extends RecyclerView.Adapter<TablesRecyclerViewAdapter.ViewHolder> {

    private int TABLES_COUNT = 17;
    private TablesFragmentInterface.MyView view;
    private Disposable disposable;
    public TablesRecyclerViewAdapter(TablesFragmentInterface.MyView view) {
        this.view = view;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_table, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.tableNumber.setText(Integer.toString(position));
        disposable = RxView.clicks(holder.container)
                .debounce(150, TimeUnit.MILLISECONDS)
                .subscribe(next -> {
                    view.startNewActivity(OrderActivity.class);
                }, error -> {
                    Log.d(TAG, error.getMessage());
                }, () -> {
                });
    }

    @Override
    public int getItemCount() {
        return TABLES_COUNT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tableNumber;
        public ConstraintLayout container;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tableNumber = itemView.findViewById(R.id.table_number);
            container = itemView.findViewById(R.id.table_container);
        }
    }
}
