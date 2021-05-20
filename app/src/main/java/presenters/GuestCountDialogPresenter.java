package presenters;


import java.util.HashMap;
import java.util.Map;

import interfaces.OrderActivityInterface;
import model.GuestCountDialogModel;

import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY;
import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY;
import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_VIEW_KEY;

public class GuestCountDialogPresenter implements OrderActivityInterface.GuestsCountDialogPresenter {

    private OrderActivityInterface.MyView view;
    private OrderActivityInterface.GuestsCountDialogModel guestsCountDialogModel;

    public GuestCountDialogPresenter(OrderActivityInterface.MyView view) {
        this.view = view;
        guestsCountDialogModel = new GuestCountDialogModel();
    }

    @Override
    public Map<String, Object> getModelState() {
        Map<String, Object> modelState = new HashMap<>();
        modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY, guestsCountDialogModel.getGuestCountRecyclerView());
        modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY, guestsCountDialogModel.getGuestsCountRecyclerViewAdapter());
        modelState.put(GUEST_COUNT_DIALOG_VIEW_KEY, guestsCountDialogModel.getGuestCountDialogView());
        return modelState;
    }

    @Override
    public void setState() {

    }
}
