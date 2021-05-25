package com.example.testfirebase.order;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;
import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;

import java.util.Map;

import dialogsTools.ErrorAlertDialog;
import interfaces.GuestCountDialogOrderActivityInterface;
import interfaces.MenuDialogOrderActivityInterface;
import model.MenuDialogModel;
import presenters.MenuDialogPresenter;
import presenters.OrderActivityPresenter;

import static com.example.testfirebase.mainActivityFragments.TablesFragment.EXTRA_TAG;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_VIEW_KEY;

public class OrderActivity extends AppCompatActivity implements GuestCountDialogOrderActivityInterface.Activity.MyView, MenuDialogOrderActivityInterface.View {

    private GuestCountDialogOrderActivityInterface.Activity.Presenter guestsCountDialogPresenter;
    private MenuDialogOrderActivityInterface.Presenter menuDialogPresenter;
    private GuestsCountBottomSheetDialog dialog;
    private View guestCountDialogView;
    private TextView guestsCount;
    private TextView tableNumber;

    private final String MENU_COLLECTION_NAME = "menu";
    private final String TAG = "myLogs";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initialisation();
        MenuDialogModel model = new MenuDialogModel();
        menuDialogPresenter = new MenuDialogPresenter(this);
    }

    private void initialisation () {
        int number = getIntent().getIntExtra(EXTRA_TAG, 0);
        tableNumber = findViewById(R.id.table_number);
        tableNumber.setText(Integer.toString(number));
        guestsCountDialogPresenter = new OrderActivityPresenter(this);
        guestsCount = findViewById(R.id.guests_count);
        prepareGuestCountModel();
        dialog = new GuestsCountBottomSheetDialog(guestCountDialogView);
//        new Handler().postDelayed(() -> {
//            dialog.show(getSupportFragmentManager(), "");
//        }, 180);
    }

    private void prepareGuestCountModel() {
        RecyclerView recyclerView = null;
        GuestsCountRecyclerViewAdapter adapter = null;
        android.view.View view = null;
        Map<String, Object> modelState = guestsCountDialogPresenter.getModelState();
        if(modelState.containsValue(null)) {
            view = android.view.View.inflate(this, R.layout.dialog_guests_count, null);
            recyclerView = view.findViewById(R.id.guests_count_recycler_view);
            adapter = new GuestsCountRecyclerViewAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
            modelState.put(GUEST_COUNT_DIALOG_VIEW_KEY, view);
            modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY, recyclerView);
            modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY, adapter);
            guestsCountDialogPresenter.setModelState(modelState);
        }
    }

    @Override
    public void setDialogView(View view) {
        guestCountDialogView = view;
    }

    @Override
    public void setGuestsCount(int guestsCount) {
        dialog.dismiss();
        this.guestsCount.setText(Integer.toString(guestsCount));
    }

    @Override
    public View onDataFillingComplete(MenuDialogOrderActivityInterface.Model model) {
        View view = View.inflate(this, R.layout.dialog_menu, null);
        RecyclerView recyclerView = view.findViewById(R.id.menu_recycler_view);
        MenuRecyclerViewAdapter adapter = new MenuRecyclerViewAdapter(model.getMenu(), model.getCategoryNames());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public RecyclerView prepareRecyclerView() {
        return null;
    }

    @Override
    public void onError(int errorCode) {
        if(!ErrorAlertDialog.isIsExist())
            ErrorAlertDialog.getNewInstance(errorCode).show(getSupportFragmentManager(), "");
    }
}
