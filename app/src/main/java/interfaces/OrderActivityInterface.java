package interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import java.util.Map;

public interface OrderActivityInterface {

    String GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY = "guestCountRecyclerView";
    String GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY = "guestsCountRecyclerViewAdapter";
    String GUEST_COUNT_DIALOG_VIEW_KEY = "guestCountDialogView";

    interface GuestsCountDialogModel {
        RecyclerView getGuestCountRecyclerView();
        GuestsCountRecyclerViewAdapter getGuestsCountRecyclerViewAdapter();
        View getGuestCountDialogView();

    }
    interface MyView {
        void setGuestsCount(int guestsCount);
    }
    interface GuestsCountDialogPresenter {
        Map<String, Object> getModelState();
        void setState ();
    }

}
