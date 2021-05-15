package com.example.testfirebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;

import org.jetbrains.annotations.NotNull;

public class TablesRecyclerViewAdapter extends RecyclerView.Adapter<TablesRecyclerViewAdapter.ViewHolder> {

    private int TABLES_COUNT = 17;

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
    }

    @Override
    public int getItemCount() {
        return TABLES_COUNT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tableNumber;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tableNumber = itemView.findViewById(R.id.table_number);
        }
    }
}
