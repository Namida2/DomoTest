package cook.model;

import com.google.firebase.firestore.FirebaseFirestore;

import cook.adapters.DetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.DetailOrderActivityInterface;

public class DetailOrderActivityModel implements DetailOrderActivityInterface.Model {

    public static final String EXTRA_TAG = "extra_tag";


    private DetailOrderItemsRecyclerViewAdapter adapter;
    private FirebaseFirestore db;

    public DetailOrderActivityModel () {
        this.db = FirebaseFirestore.getInstance();
    }
    @Override
    public FirebaseFirestore getDatabase() {
        return db;
    }
    @Override
    public void setRecyclerViewAdapter(DetailOrderItemsRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public DetailOrderItemsRecyclerViewAdapter getRecyclerViewAdapter() {
        return adapter;
    }
}
