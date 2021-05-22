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
        void setGuestCountRecyclerView(RecyclerView recyclerView);
        void setGuestsCountRecyclerViewAdapter(GuestsCountRecyclerViewAdapter guestsCountRecyclerViewAdapter);
        void setGuestCountDialogView(View view);
    }

    interface Activity {
        interface MyView {
            void setGuestsCount(int guestsCount);
            void setDialogView(View view);
        }
        interface Presenter {
            Map<String, Object> getModelState();
            void setModelState(Map<String, Object> modelState);
        }
    }

    interface GuestsCountAdapter {
        interface MyView {

        }
        interface Presenter {
            void setGuestsCountToView(int guestsCount);
        }
    }

    interface MenuDialog {
        interface Model {

        }
        interface View {

        }
        interface Presenter {
            void initialisationModel();
        }
    }

}
