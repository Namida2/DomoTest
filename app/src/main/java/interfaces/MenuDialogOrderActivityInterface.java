package interfaces;


import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.testfirebase.order.DishCategoryInfo;

import tools.Pair;

public interface MenuDialogOrderActivityInterface {

    interface Model {
        void setView (android.view.View view);
        android.view.View getView();
        RecyclerView getRecyclerView();
        MenuRecyclerViewAdapter getMenuItemAdapter();
        void setRecyclerView(RecyclerView recyclerView);
        void setMenuItemAdapter(MenuRecyclerViewAdapter adapter);
        FirebaseFirestore getDatabase();
        Map<String, List<Dish>> getMenu();
        Map<String, Object> getModelState();
        void setMenu(Map<String, List<Dish>> menu);
        ArrayList<DishCategoryInfo<String, Integer>> getCategoryNames();
        void setCategoryNames(ArrayList<DishCategoryInfo<String, Integer>> categoryNames);
    }
    interface View {
        void onError(int errorCode);
        void onModelComplete(android.view.View menuDialogView);
        Pair<android.view.View, MenuRecyclerViewAdapter> onDataFillingComplete(MenuDialogOrderActivityInterface.Model model);
    }
    interface Presenter {
        void setModelDataState();
        void setModelViewState();
    }

}
