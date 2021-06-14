package model;

import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import interfaces.GuestCountDialogOrderActivityInterface;

public class GuestCountDialogModel implements GuestCountDialogOrderActivityInterface.Model {

    private GuestsCountRecyclerViewAdapter guestsCountRecyclerViewAdapter;

    @Override
    public GuestsCountRecyclerViewAdapter getGuestsCountRecyclerViewAdapter() {
        return guestsCountRecyclerViewAdapter;
    }
    @Override
    public void setGuestsCountRecyclerViewAdapter(GuestsCountRecyclerViewAdapter guestsCountRecyclerViewAdapter) {
        this.guestsCountRecyclerViewAdapter = guestsCountRecyclerViewAdapter;
    }
}
