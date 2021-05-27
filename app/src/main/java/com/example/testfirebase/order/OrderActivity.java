package com.example.testfirebase.order;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;
import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import dialogsTools.ErrorAlertDialog;
import interfaces.GuestCountDialogOrderActivityInterface;
import interfaces.MenuDialogOrderActivityInterface;
import model.MenuDialogModel;
import presenters.MenuDialogPresenter;
import presenters.OrderActivityPresenter;
import tools.Pair;

import static com.example.testfirebase.mainActivityFragments.TablesFragment.EXTRA_TAG;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_VIEW_KEY;
import static registration.LogInActivity.TAG;

public class OrderActivity extends AppCompatActivity implements GuestCountDialogOrderActivityInterface.Activity.MyView, MenuDialogOrderActivityInterface.View {

    private GuestCountDialogOrderActivityInterface.Activity.Presenter guestsCountDialogPresenter;
    private MenuDialogOrderActivityInterface.Presenter menuDialogPresenter;
    private GuestsCountBottomSheetDialog guestCountDialog;
    private View guestCountDialogView;
    private TextView guestsCount;
    private TextView tableNumber;
    private View menuDialogView;
    private MenuBottomSheetDialog menuDialog;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fba;

    private final String MENU_COLLECTION_NAME = "menu";
    private final String TAG = "myLogs";

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initialisation();

    }

    private void initialisation () {
        int number = getIntent().getIntExtra(EXTRA_TAG, 0);
        tableNumber = findViewById(R.id.table_number);
        tableNumber.setText(Integer.toString(number));

        guestsCountDialogPresenter = new OrderActivityPresenter(this);
        guestsCount = findViewById(R.id.guests_count);
        prepareGuestCountModel();

        guestCountDialog = new GuestsCountBottomSheetDialog(guestCountDialogView);
        new Handler().postDelayed(() -> {
            guestCountDialog.show(getSupportFragmentManager(), "");
        }, 10);

        MenuDialogModel model = new MenuDialogModel();
        menuDialogPresenter = new MenuDialogPresenter(this);

        bottomAppBar = findViewById(R.id.bottom_app_bar);
        fba = findViewById(R.id.menu_floating_action_button);
        fba.setOnClickListener(view -> {
            if(menuDialogView != null)
                menuDialog.show(getSupportFragmentManager(), "");
        });
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
    public void setGuestsCount(int guestsCount) {
        guestCountDialog.dismiss();
        this.guestsCount.setText(Integer.toString(guestsCount));
    }
    @Override
    public void onModelComplete(View menuDialogView) {
        this.menuDialogView = menuDialogView;
        menuDialog = new MenuBottomSheetDialog(menuDialogView);
        Log.d(TAG, "COMPLETE");
    }
    @Override
    public Pair<View, MenuRecyclerViewAdapter> onDataFillingComplete(MenuDialogOrderActivityInterface.Model model) {
        View view = View.inflate(this, R.layout.dialog_menu, null);
        RecyclerView recyclerView = view.findViewById(R.id.menu_recycler_view);
        MenuRecyclerViewAdapter adapter = new MenuRecyclerViewAdapter(model.getMenu(), model.getCategoryNames());
        recyclerView.setAdapter(adapter);
        return new Pair<>(view, adapter);
    }
    @Override
    public void onError(int errorCode) {
        if(!ErrorAlertDialog.isIsExist())
            ErrorAlertDialog.getNewInstance(errorCode).show(getSupportFragmentManager(), "");
    }
    @Override
    public void setDialogView(View view) {
        guestCountDialogView = view;
    }
}
