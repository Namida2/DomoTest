package presenters;

import com.example.domo.adapters.GuestsCountRecyclerViewAdapter;

import interfaces.GuestCountDialogOrderActivityInterface;
import model.GuestCountDialogModel;


public class GuestCountDialogOrderActivityPresenter implements GuestCountDialogOrderActivityInterface.Activity.Presenter {

    private GuestCountDialogOrderActivityInterface.Activity.MyView view;
    private GuestCountDialogOrderActivityInterface.Model model;

    public GuestCountDialogOrderActivityPresenter(GuestCountDialogOrderActivityInterface.Activity.MyView view) {
        this.view = view;
        if (model == null) {
            model = new GuestCountDialogModel();
            model.setGuestsCountRecyclerViewAdapter(new GuestsCountRecyclerViewAdapter(view::setGuestsCountTextView));
        }
    }
    @Override
    public GuestsCountRecyclerViewAdapter getGuestCountAdapter() {
        return model.getGuestsCountRecyclerViewAdapter();
    }
}
