package model;

import android.util.Log;

import com.example.testfirebase.order.Dish;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import interfaces.OrderActivityInterface;
import io.reactivex.rxjava3.core.Observable;

import static registration.LogInActivity.TAG;

public class MenuDialogModel implements OrderActivityInterface.MenuDialog.Model {

    private final String MENU_COLLECTION_NAME = "menu";
    private final String DISHES_COLLECTION_NAME = "dishes";
    private Map<String, ArrayList<Dish>> dishesHashMap = new HashMap<>();

    private ArrayList<String> categoryNameArrayList;

    private FirebaseFirestore db;

    public MenuDialogModel () {
        db = FirebaseFirestore.getInstance();
        initialisation();
    }

    public Map<String, ArrayList<Dish>> getDishesHashMap() {
        return dishesHashMap;
    }

    @Override
    public void initialisation() {
        categoryNameArrayList = new ArrayList<>();
        db.collection(MENU_COLLECTION_NAME).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    categoryNameArrayList.add(documentSnapshot.getId());
                }
                getCategoryNameObservable(categoryNameArrayList)
                    .subscribe(categoryName -> {
                        db.collection(MENU_COLLECTION_NAME)
                            .document(categoryName)
                            .collection(DISHES_COLLECTION_NAME)
                            .get().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                for(QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
                                    Log.d(TAG, categoryName + ": " + documentSnapshot.getId() + ": " + documentSnapshot.getData());
                                }
                            }
                            else {
                                Log.d(TAG, "getCategoryNameObservable: " + task1.getException());
                            }
                        });
                    });
            }
            else {
                Log.d(TAG, task.getException().toString());
            }
        });
    }

    private Observable<String> getCategoryNameObservable (ArrayList<String> categoryNameArrayList) {
        return Observable.create(emitter -> {
            for (int i = 0 ; i < categoryNameArrayList.size(); ++i) {
                emitter.onNext(categoryNameArrayList.get(i));
            }
        });
    }



}
