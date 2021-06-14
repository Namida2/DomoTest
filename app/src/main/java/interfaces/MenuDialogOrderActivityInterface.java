package interfaces;

import com.example.domo.adapters.MenuRecyclerViewAdapter;
import com.example.domo.order.Dish;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.domo.order.DishCategoryInfo;

public interface MenuDialogOrderActivityInterface {

    interface Model {
        MenuRecyclerViewAdapter getMenuItemAdapter();
        void setMenuItemAdapter(MenuRecyclerViewAdapter adapter);
        FirebaseFirestore getDatabase();
        Map<String, List<Dish>> getMenu();
        void setMenu(Map<String, List<Dish>> menu);
        ArrayList<DishCategoryInfo<String, Integer>> getCategoryNames();
        void setCategoryNames(ArrayList<DishCategoryInfo<String, Integer>> categoryNames);
    }
    interface View {
        void showMenuItemDishDialog(Dish orderItem);
        void onMenuDialogError(int errorCode);
        void onMenuDialogModelComplete(MenuRecyclerViewAdapter adapter);
    }
    interface Presenter {
        void onResume ();
        void onDestroy();
        void resetItemIsPressed();
        void setModelDataState();
        void setModelViewState();
    }

}
