package com.example.testfirebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.google.android.material.transition.Hold;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class GuestsCountRecyclerViewAdapter extends RecyclerView.Adapter<GuestsCountRecyclerViewAdapter.ViewHolder> {

    private int GUESTS_COUNT = 8;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView guests_count;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            guests_count = itemView.findViewById(R.id.guests_count);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder( inflater.inflate(R.layout.layout_guests_count, parent, false) );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.guests_count.setText(position);
    }

    @Override
    public int getItemCount() {
        return GUESTS_COUNT;
    }

}
