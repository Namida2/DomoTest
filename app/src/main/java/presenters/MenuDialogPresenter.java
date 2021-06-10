package presenters;

import android.util.Log;

import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;
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

import com.example.testfirebase.order.DishCategoryInfo;

import static model.MenuDialogModel.DISHES_COLLECTION_NAME;
import static model.MenuDialogModel.MENU_COLLECTION_NAME;
import static registration.LogInActivity.TAG;

public class MenuDialogPresenter implements MenuDialogOrderActivityInterface.Presenter {

    private static MenuDialogOrderActivityInterface.Model model;
    private MenuDialogOrderActivityInterface.View view;
    private ArrayList<Integer> categoryNamesPosition;

    public MenuDialogPresenter (MenuDialogOrderActivityInterface.View view) {
        this.view = view;
        if (model == null) {
            model = new MenuDialogModel();
            setModelDataState();
        } else view.onMenuDialogModelComplete(model.getMenuItemAdapter());
    }
    @Override
    public void setModelViewState() {
        MenuRecyclerViewAdapter adapter = new MenuRecyclerViewAdapter(
            model.getMenu(),
            model.getCategoryNames()
        );
        adapter.setAcceptDishConsumer(view::showMenuItemDishDialog);
        model.setMenuItemAdapter(adapter);
        view.onMenuDialogModelComplete(model.getMenuItemAdapter());
    }
    @Override
    public void onResume () {
        if (model.getMenuItemAdapter() != null)
            model.getMenuItemAdapter().setAcceptDishConsumer(view::showMenuItemDishDialog);
    }
    @Override
    public void onDestroy() {
        view = null;
    }
    @Override
    public void resetItemIsPressed() {
        model.getMenuItemAdapter().resetIsPressed();
    }
    @Override
    public void setModelDataState() {
        ArrayList<DishCategoryInfo<String, Integer>> categoryNames = new ArrayList<>();
        Map<String, List<Dish>> menu = new HashMap<>();
        model.setCategoryNames(categoryNames);
        model.setMenu(menu);
        model.getDatabase().collection(MENU_COLLECTION_NAME).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                    categoryNames.add(new DishCategoryInfo<>(documentSnapshot.getId(), 0));
                getCategoryNameObservable(categoryNames)
                    .subscribe(categoryNameItem -> {
                        Log.d(TAG, "MenuDialogPresenter.initialization: " + categoryNameItem);
                        model.getDatabase().collection(MENU_COLLECTION_NAME)
                            .document(categoryNameItem.categoryName)
                            .collection(DISHES_COLLECTION_NAME)
                            .get().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                List<Dish> dishesArrayList = task1.getResult().toObjects(Dish.class);
                                Dish.incrementPosition();
                                menu.put(categoryNameItem.categoryName, dishesArrayList);
                                categoryNameItem.categorySize = dishesArrayList.size();
                                for(int i = 0; i < categoryNames.size(); ++i) {
                                    int numberOfAllDishesBefore = 0;
                                    if (categoryNames.get(i).categorySize != 0
                                        && categoryNames.get(i).categoryNamePosition != null) continue;
                                    if (categoryNames.get(i).categorySize == 0) break;
                                    for(int j = 0; j <= i; ++j) {
                                        if (i == j) continue;
                                        numberOfAllDishesBefore += categoryNames.get(j).categorySize;
                                    }
                                    categoryNames.get(i).categoryNamePosition = numberOfAllDishesBefore + i;
                                    if (i == categoryNames.size()-1)
                                        setModelViewState();
                                }
                            }
                            else  Log.d(TAG, "MenuDialogPresenter.initialization: " + task1.getException());
                        });
                    }, error -> {
                    }, () -> {
                    });
            }
            else {
                Log.d(TAG, "MenuDialogPresenter.initialization: " + task.getException().toString());
                view.onMenuDialogError(ErrorAlertDialog.INTERNET_CONNECTION);
                // add a category "SOMETHING WRONG"
            }
        });
    }
    private Observable<DishCategoryInfo<String, Integer>> getCategoryNameObservable (ArrayList<DishCategoryInfo<String, Integer>> categoryNameArrayList) {
        return Observable.create(emitter -> {
            for (int i = 0 ; i < categoryNameArrayList.size(); ++i) {
                emitter.onNext(categoryNameArrayList.get(i));
            }
        });
    }
}
