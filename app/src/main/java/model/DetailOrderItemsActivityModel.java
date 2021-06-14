package model;

import com.example.testfirebase.adapters.DetailOrderItemsRecyclerViewAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import interfaces.DetailOrderItemsActivityInterface;

public class DetailOrderItemsActivityModel implements DetailOrderItemsActivityInterface.Model {

    private DetailOrderItemsRecyclerViewAdapter adapter;

    @Override
    public FirebaseFirestore getDatabase() {
        return null;
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
