package interfaces;

import com.example.testfirebase.adapters.DetailOrderItemsRecyclerViewAdapter;
import com.google.firebase.firestore.FirebaseFirestore;


public interface DetailOrderItemsActivityInterface {

    interface Model {
        FirebaseFirestore getDatabase();
        void setRecyclerViewAdapter(DetailOrderItemsRecyclerViewAdapter adapter);
        DetailOrderItemsRecyclerViewAdapter getRecyclerViewAdapter();
    }
    interface View {
        void setViewData();
    }
    interface Presenter {
        void onDestroy();
        DetailOrderItemsRecyclerViewAdapter getAdapter(String tableNumber);
    }

}
