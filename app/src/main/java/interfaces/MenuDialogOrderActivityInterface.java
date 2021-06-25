package interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.CategoryNamesRecyclerViewAdapter;
import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.testfirebase.order.DishCategoryInfo;

public interface MenuDialogOrderActivityInterface {

    interface Model {
        void setCategoryNamesAdapter(CategoryNamesRecyclerViewAdapter adapter);
        CategoryNamesRecyclerViewAdapter getCategoryNamesAdapter();
        MenuRecyclerViewAdapter getMenuItemAdapter();
        void setMenuItemAdapter(MenuRecyclerViewAdapter adapter);
        FirebaseFirestore getDatabase();
        Map<String, List<Dish>> getMenu();
        void setMenu(Map<String, List<Dish>> menu);
        ArrayList<DishCategoryInfo<String, Integer>> getCategoryNames();
        void setCategoryNames(ArrayList<DishCategoryInfo<String, Integer>> categoryNames);
    }
    interface View {
        RecyclerView getCategoryNamesRecyclerView();
        void showMenuItemDishDialog(Dish orderItem);
        void onMenuDialogError(int errorCode);
        RecyclerView onMenuDialogModelComplete(MenuRecyclerViewAdapter adapter);
    }
    interface Presenter {
        void onResume ();
        void onDestroy();
        void resetItemIsPressed();
        void setModelDataState();
        void setModelViewState();
    }

}
