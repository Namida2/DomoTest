package interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        ArrayList<Pair<String, Integer>> getCategoryNames();
        void setCategoryNames(ArrayList<Pair<String, Integer>> categoryNames);
    }
    interface View {
        RecyclerView prepareRecyclerView();
        void onError(int errorCode);
        android.view.View onDataFillingComplete(MenuDialogOrderActivityInterface.Model model);
    }
    interface Presenter {
        void initialization();
    }

}
