package model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import interfaces.GuestCountDialogOrderActivityInterface;

public class GuestCountDialogModel implements GuestCountDialogOrderActivityInterface.Model {

    private RecyclerView guestsCountRecyclerView;
    private GuestsCountRecyclerViewAdapter guestsCountRecyclerViewAdapter;
    private View guestsCountDialogView;

    @Override
    public RecyclerView getGuestCountRecyclerView() {
        return guestsCountRecyclerView;
    }

    @Override
    public GuestsCountRecyclerViewAdapter getGuestsCountRecyclerViewAdapter() {
        return guestsCountRecyclerViewAdapter;
    }

    @Override
    public View getGuestCountDialogView() {
        return guestsCountDialogView;
    }

    @Override
    public void setGuestCountRecyclerView(RecyclerView guestsCountRecyclerView) {
        this.guestsCountRecyclerView = guestsCountRecyclerView;
    }

    @Override
    public void setGuestsCountRecyclerViewAdapter(GuestsCountRecyclerViewAdapter guestsCountRecyclerViewAdapter) {
        this.guestsCountRecyclerViewAdapter = guestsCountRecyclerViewAdapter;
    }

    @Override
    public void setGuestCountDialogView(View guestsCountDialogView) {
        this.guestsCountDialogView = guestsCountDialogView;
    }


}
