package cook.model;

import com.google.firebase.firestore.FirebaseFirestore;

import cook.adapters.CookDetailOrderItemsRecyclerViewAdapter;
import cook.interfaces.CookDetailOrderActivityInterface;

public class DetailOrderActivityModel implements CookDetailOrderActivityInterface.Model {

    public static final String EXTRA_TAG = "extra_tag";
    public static final String COLLECTION_ITEMS_LISTENER_NAME = "items_listener";
    public static final String DOCUMENT_LISTENER_NAME = "listener";
    public static final String FIELD_NOTIFY_NAME = "notify";

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
