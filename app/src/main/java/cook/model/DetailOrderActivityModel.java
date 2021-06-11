package cook.model;

import com.google.firebase.firestore.FirebaseFirestore;

import cook.adapters.CookDetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.CookDetailOrderActivityInterface;

public class DetailOrderActivityModel implements CookDetailOrderActivityInterface.Model {

    public static final String EXTRA_TAG = "extra_tag";

    private CookDetailOrderItemsRecyclerViewAdapter adapter;
    private FirebaseFirestore db;

    public DetailOrderActivityModel () {
        this.db = FirebaseFirestore.getInstance();
    }
    @Override
    public FirebaseFirestore getDatabase() {
        return db;
    }
    @Override
    public void setRecyclerViewAdapter(CookDetailOrderItemsRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public CookDetailOrderItemsRecyclerViewAdapter getRecyclerViewAdapter() {
        return adapter;
    }
}
