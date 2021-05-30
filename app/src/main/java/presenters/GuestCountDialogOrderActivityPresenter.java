package presenters;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import java.util.HashMap;
import java.util.Map;

import interfaces.GuestCountDialogOrderActivityInterface;
import model.GuestCountDialogModel;

import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_VIEW_KEY;

public class GuestCountDialogOrderActivityPresenter implements GuestCountDialogOrderActivityInterface.Activity.Presenter {

    private GuestCountDialogOrderActivityInterface.Activity.MyView view;
    private GuestCountDialogOrderActivityInterface.Model model;

    @Override
    public void setGuestCountModelState(Map<String, Object> modelState) {
        View view = (android.view.View) modelState.get(GUEST_COUNT_DIALOG_VIEW_KEY);
        RecyclerView recyclerView = (RecyclerView) modelState.get(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY);
        GuestsCountRecyclerViewAdapter adapter = (GuestsCountRecyclerViewAdapter) modelState.get(GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY);
        model.setGuestCountDialogView(view);
        model.setGuestCountRecyclerView(recyclerView);
        model.setGuestsCountRecyclerViewAdapter(adapter);
        this.view.setGuestCountDialogView(model.getGuestCountDialogView());
    }

    public GuestCountDialogOrderActivityPresenter(GuestCountDialogOrderActivityInterface.Activity.MyView view) {
        this.view = view;
        model = new GuestCountDialogModel();
    }

    @Override
    public Map<String, Object> getGuestCountModelState() {
        Map<String, Object> modelState = new HashMap<>();
        modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY, model.getGuestCountRecyclerView());
        modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY, model.getGuestsCountRecyclerViewAdapter());
        modelState.put(GUEST_COUNT_DIALOG_VIEW_KEY, model.getGuestCountDialogView());
        return modelState;
    }

}
