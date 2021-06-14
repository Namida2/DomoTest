package interfaces;

import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

public interface GuestCountDialogOrderActivityInterface {

    String GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY = "guestCountRecyclerView";
    String GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY = "guestsCountRecyclerViewAdapter";
    String GUEST_COUNT_DIALOG_VIEW_KEY = "guestCountDialogView";

    interface Model {
        GuestsCountRecyclerViewAdapter getGuestsCountRecyclerViewAdapter();
        void setGuestsCountRecyclerViewAdapter(GuestsCountRecyclerViewAdapter guestsCountRecyclerViewAdapter);
    }
    interface Activity {
        interface MyView {
            void setGuestsCountTextView(int guestsCountTextView);
            void setGuestCountDialogView();
        }
        interface Presenter {
            GuestsCountRecyclerViewAdapter getGuestCountAdapter();
        }
    }
}
