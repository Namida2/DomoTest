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
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import interfaces.OrderActivityInterface;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

import static registration.LogInActivity.TAG;

public class GuestsCountRecyclerViewAdapter extends RecyclerView.Adapter<GuestsCountRecyclerViewAdapter.ViewHolder> implements OrderActivityInterface.GuestsCountAdapter.Presenter {

    private int GUESTS_COUNT = 8;
    private OrderActivityInterface.Activity.MyView view;
    public GuestsCountRecyclerViewAdapter (OrderActivityInterface.Activity.MyView view) {
        this.view = view;
    }

    @Override
    public void setGuestsCountToView(int guestsCount) {
        view.setGuestsCount(guestsCount);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OrderActivityInterface.GuestsCountAdapter.MyView {
        OrderActivityInterface.GuestsCountAdapter.Presenter presenter;
        TextView guests_count;
        ConstraintLayout container;

        public ViewHolder(@NonNull @NotNull View itemView, OrderActivityInterface.GuestsCountAdapter.Presenter presenter) {
            super(itemView);
            this.presenter = presenter;
            guests_count = itemView.findViewById(R.id.guests_count);
            container = itemView.findViewById(R.id.container);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder( inflater.inflate(R.layout.layout_guests_count, parent, false), this );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.guests_count.setText(Integer.toString(position));
        RxView.clicks(holder.container)
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> {
                    holder.presenter.setGuestsCountToView(position);
                }, error -> {
                    Log.d(TAG, error.getMessage());
                }, () -> { });
    }

    @Override
    public int getItemCount() {
        return GUESTS_COUNT;
    }

}
