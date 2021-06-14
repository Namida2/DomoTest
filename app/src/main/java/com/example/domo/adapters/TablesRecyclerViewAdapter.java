package com.example.domo.adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domo.R;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.rxjava3.disposables.Disposable;

import static registration.LogInActivity.TAG;

public class TablesRecyclerViewAdapter extends RecyclerView.Adapter<TablesRecyclerViewAdapter.ViewHolder> {

    private int TABLES_COUNT = 17;
    private Disposable disposable;
    private boolean isPressed = false;
    private Consumer<Integer> acceptTableNumber;

    public void setAcceptTableNumber (Consumer<Integer> acceptTableNumber) {
        this.acceptTableNumber = acceptTableNumber;
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
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)  {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_table, parent, false));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.tableNumber.setText(Integer.toString(position + 1));
        disposable = RxView.clicks(holder.container)
            .debounce(120, TimeUnit.MILLISECONDS)
            .subscribe(next -> {
                if (!isPressed) {
                    isPressed = true;
                    try {
                        acceptTableNumber.accept(position + 1);
                    }
                    catch (Exception e) {
                        Log.d(TAG, "TablesRecyclerViewAdapter.onBindViewHolder+++: " + e.getMessage());
                    }
                }
            }, error -> {
                Log.d(TAG, "TablesRecyclerViewAdapter.onBindViewHolder: " + error.getMessage());
            }, () -> {
            });
    }

    @Override
    public int getItemCount() {
        return TABLES_COUNT;
    }
    public void resetIsPressed () {
        this.isPressed = false;
    }
}
