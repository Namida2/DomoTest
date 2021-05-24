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
//        categoryNameArrayList = new ArrayList<>();
//        db.collection(MENU_COLLECTION_NAME).get().addOnCompleteListener(task -> {
//            if(task.isSuccessful()) {
//                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                    categoryNameArrayList.add(documentSnapshot.getId());
//                }
//                getCategoryNameObservable(categoryNameArrayList)
//                    .subscribe(categoryName -> {
//                        db.collection(MENU_COLLECTION_NAME)
//                            .document(categoryName)
//                            .collection(DISHES_COLLECTION_NAME)
//                            .get().addOnCompleteListener(task1 -> {
//                            if(task1.isSuccessful()) {
//                                for(QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
//                                    Log.d(TAG, categoryName + ": " + documentSnapshot.getId() + ": " + documentSnapshot.getData());
//                                }
//                            }
//                            else {
//                                Log.d(TAG, "getCategoryNameObservable: " + task1.getException());
//                            }
//                        });
//                    });
//            }
//            else {
//                Log.d(TAG, task.getException().toString());
//            }
//        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword("nikit.mahno@yandex.ru", "pppppp").addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
               ArrayList<Dish> arrayList = getDishesArrayList();
                for(int i = 0; i < arrayList.size(); ++i){
                    db.collection(MENU_COLLECTION_NAME)
                        .document(arrayList.get(i).getCategoryName())
                        .collection(DISHES_COLLECTION_NAME)
                        .document(arrayList.get(i).getName()).set(arrayList.get(i)).addOnCompleteListener(task1 -> {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "SUCCESS");
                        }
                        else Log.d(TAG, "getCategoryNameObservable: " + task1.getException());
                    });
                }

            }
            else
                Log.d(TAG, "getCategoryNameObservable: " + task.getException());
        });


    }

    private Observable<String> getCategoryNameObservable (ArrayList<String> categoryNameArrayList) {
        return Observable.create(emitter -> {
            for (int i = 0 ; i < categoryNameArrayList.size(); ++i) {
                emitter.onNext(categoryNameArrayList.get(i));
            }
        });
    }

    private ArrayList<Dish> getDishesArrayList () {
        ArrayList<Dish> dishesArrayList = new ArrayList<>();
        Dish dish = new Dish();

        dish.setName("Эби-сию рамен");
        dish.setCategoryName("Рамен");
        dish.setWeight("600г");
        dish.setCost("450₽");
        dishesArrayList.add(dish);
        dish = new Dish();

        dish.setName("Ореховый сёю рамен");
        dish.setCategoryName("Рамен");
        dish.setWeight("600г");
        dish.setCost("350₽");
        dishesArrayList.add(dish);
        dish = new Dish();

        dish.setName("Мраморный гю-сию рамен");
        dish.setCategoryName("Рамен");
        dish.setWeight("600г");
        dish.setCost("445₽");
        dishesArrayList.add(dish);
        dish = new Dish();

        dish.setName("Сёю рамен");
        dish.setCategoryName("Рамен");
        dish.setWeight("600г");
        dish.setCost("350₽");
        dishesArrayList.add(dish);
        dish = new Dish();

        dish.setName("Томато мисо рамен с креветкой");
        dish.setCategoryName("Рамен");
        dish.setWeight("600г");
        dish.setCost("450₽");
        dishesArrayList.add(dish);
        dish = new Dish();

        return dishesArrayList;
    }

}
