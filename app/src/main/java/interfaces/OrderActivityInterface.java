package interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import java.util.Map;

public interface OrderActivityInterface {

    String GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY = "guestCountRecyclerView";
    String GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY = "guestsCountRecyclerViewAdapter";
    String GUEST_COUNT_DIALOG_VIEW_KEY = "guestCountDialogView";

    interface Model {
        RecyclerView getGuestCountRecyclerView();
        GuestsCountRecyclerViewAdapter getGuestsCountRecyclerViewAdapter();
        View getGuestCountDialogView();
    }

    interface Activity {
        interface MyView {
            void setGuestsCount(int guestsCount);
        }
        interface Presenter {
            Map<String, Object> getModelState();
            void setModelState(Map<String, Object> modelState);
            View getGuestCountDialogView ();
        }
    }

    interface Dialog {
        interface MyView {

        }
        interface Presenter {
            View getDialogView ();
        }
    }

}
