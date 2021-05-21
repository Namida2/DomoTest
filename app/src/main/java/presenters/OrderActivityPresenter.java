package presenters;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import java.util.HashMap;
import java.util.Map;

import interfaces.OrderActivityInterface;
import model.GuestCountDialogModel;

import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY;
import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY;
import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_VIEW_KEY;

public class OrderActivityPresenter implements OrderActivityInterface.Activity.Presenter {

    private OrderActivityInterface.Activity.MyView view;
    private OrderActivityInterface.Model model;

    public OrderActivityPresenter(OrderActivityInterface.Activity.MyView view) {
        this.view = view;
        model = new GuestCountDialogModel();
    }

    @Override
    public Map<String, Object> getModelState() {
        Map<String, Object> modelState = new HashMap<>();
        modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY, model.getGuestCountRecyclerView());
        modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY, model.getGuestsCountRecyclerViewAdapter());
        modelState.put(GUEST_COUNT_DIALOG_VIEW_KEY, model.getGuestCountDialogView());
        return modelState;
    }

    @Override
    public void setModelState(Map<String, Object> modelState) {
        View view = (android.view.View) modelState.get(GUEST_COUNT_DIALOG_VIEW_KEY);
        RecyclerView recyclerView = (RecyclerView) modelState.get(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY);
        GuestsCountRecyclerViewAdapter adapter = (GuestsCountRecyclerViewAdapter) modelState.get(GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY);
        model.setGuestCountDialogView(view);
        model.setGuestCountRecyclerView(recyclerView);
        model.setGuestsCountRecyclerViewAdapter(adapter);
        this.view.setDialogView(model.getGuestCountDialogView());
    }

}
