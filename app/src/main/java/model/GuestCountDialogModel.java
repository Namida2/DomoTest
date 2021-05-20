package model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import interfaces.OrderActivityInterface;

public class GuestCountDialogModel implements OrderActivityInterface.Model {

    private RecyclerView GuestsCountRecyclerView;
    private GuestsCountRecyclerViewAdapter guestsCountRecyclerViewAdapter;
    private View GuestsCountDialogView;

    @Override
    public RecyclerView getGuestCountRecyclerView() {
        return GuestsCountRecyclerView;
    }

    @Override
    public GuestsCountRecyclerViewAdapter getGuestsCountRecyclerViewAdapter() {
        return guestsCountRecyclerViewAdapter;
    }

    @Override
    public View getGuestCountDialogView() {
        return GuestsCountDialogView;
    }

    public void setGuestsCountRecyclerViewAdapter(GuestsCountRecyclerViewAdapter guestsCountRecyclerViewAdapter) {
        this.guestsCountRecyclerViewAdapter = guestsCountRecyclerViewAdapter;
    }

    public void setGuestsCountRecyclerView(RecyclerView guestsCountRecyclerView) {
        GuestsCountRecyclerView = guestsCountRecyclerView;
    }

    public void setGuestsCountDialogView(View guestsCountDialogView) {
        GuestsCountDialogView = guestsCountDialogView;
    }
}
