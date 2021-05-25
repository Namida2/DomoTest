package presenters;

import android.util.Log;
import android.view.View;

import com.example.testfirebase.R;
import com.example.testfirebase.order.Dish;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dialogsTools.ErrorAlertDialog;
import interfaces.MenuDialogOrderActivityInterface;
import io.reactivex.rxjava3.core.Observable;
import model.MenuDialogModel;
import tools.Pair;

import static model.MenuDialogModel.DISHES_COLLECTION_NAME;
import static model.MenuDialogModel.MENU_COLLECTION_NAME;
import static registration.LogInActivity.TAG;

public class MenuDialogPresenter implements MenuDialogOrderActivityInterface.Presenter {

    private static MenuDialogOrderActivityInterface.Model model;
    private MenuDialogOrderActivityInterface.View view;

    public MenuDialogPresenter (MenuDialogOrderActivityInterface.View view) {
        this.view = view;
        if (model == null)
            model = new MenuDialogModel();
        initialization();
    }

    @Override
    public void initialization() {
        ArrayList<Pair<String, Integer>> categoryNames = new ArrayList<>();
        Map<String, List<Dish>> menu = new HashMap<>();
        model.setCategoryNames(categoryNames);
        model.setMenu(menu);
        model.getDatabase().collection(MENU_COLLECTION_NAME).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                    categoryNames.add(new Pair<>(documentSnapshot.getId(), 0));
                getCategoryNameObservable(categoryNames)
                    .subscribe(categoryNameItem -> {
                        model.getDatabase().collection(MENU_COLLECTION_NAME)
                            .document(categoryNameItem.categoryName)
                            .collection(DISHES_COLLECTION_NAME)
                            .get().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                List<Dish> dishesArrayList = task1.getResult().toObjects(Dish.class);
                                menu.put(categoryNameItem.categoryName, task1.getResult().toObjects(Dish.class));
                                categoryNameItem.categorySize = dishesArrayList.size();
                                for(int i = 0; i < categoryNames.size(); ++i) {
                                    int numberOfAllDishesBefore = 0;
                                    if (categoryNames.get(i).categorySize != 0 && categoryNames.get(i).numberOfAllDishesBefore != null) continue;
                                    if (categoryNames.get(i).categorySize == 0) break;
                                    for(int j = 0; j < i; ++j)
                                        numberOfAllDishesBefore += categoryNames.get(j).categorySize;
                                    categoryNames.get(i).numberOfAllDishesBefore = numberOfAllDishesBefore;
                                    if (i == categoryNames.size()-1) {
                                        View dialogView = view.onDataFillingComplete(model);
                                        model.setRecyclerView(dialogView.findViewById(R.id.menu_recycler_view));
                                        //ADAPTER
                                    }
                                }
                            }
                            else { Log.d(TAG, "MenuDialogPresenter.initialization: " + task1.getException());
                            }
                        });
                    }, error -> {
                        Log.d(TAG,"MenuDialogPresenter.initialization: " + error.getMessage());
                    }, () -> {
                    });
            }
            else {
                Log.d(TAG, "MenuDialogPresenter.initialization: " + task.getException().toString());
                view.onError(ErrorAlertDialog.INTERNET_CONNECTION);
            }
        });
    }

    private Observable<Pair<String, Integer>> getCategoryNameObservable (ArrayList<Pair<String, Integer>> categoryNameArrayList) {
        return Observable.create(emitter -> {
            for (int i = 0 ; i < categoryNameArrayList.size(); ++i) {
                emitter.onNext(categoryNameArrayList.get(i));
            }
        });
    }
}
