package cook.interfaces;

import com.google.firebase.firestore.FirebaseFirestore;

import cook.ReadyDish;
import cook.adapters.CookDetailOrderItemsRecyclerViewAdapter;

public interface CookDetailOrderActivityInterface {
    interface Model {
        FirebaseFirestore getDatabase();
        void setRecyclerViewAdapter(CookDetailOrderItemsRecyclerViewAdapter adapter);
        CookDetailOrderItemsRecyclerViewAdapter getRecyclerViewAdapter();
    }
    interface View {
        String getTableNumber();
        void showSetDishReadyDialog (ReadyDish dishData);
        void setViewData();
        void onError(int errorCode);
    }
    interface Presenter {
        void onResume();
        void setDishState(ReadyDish dishData);
        CookDetailOrderItemsRecyclerViewAdapter getAdapter(String tableNumber);
    }
}
