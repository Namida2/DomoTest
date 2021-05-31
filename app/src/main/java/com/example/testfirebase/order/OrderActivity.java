package com.example.testfirebase.order;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;
import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;
import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;
import java.util.function.Consumer;

import dialogsTools.ErrorAlertDialog;
import interfaces.GuestCountDialogOrderActivityInterface;
import interfaces.MenuDialogOrderActivityInterface;
import interfaces.OrderActivityInterface;
import model.MenuDialogModel;
import presenters.MenuDialogPresenter;
import presenters.GuestCountDialogOrderActivityPresenter;
import presenters.OrderActivityPresenter;
import tools.Pair;

import static com.example.testfirebase.mainActivityFragments.TablesFragment.EXTRA_TAG;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY;
import static interfaces.GuestCountDialogOrderActivityInterface.GUEST_COUNT_DIALOG_VIEW_KEY;
import static registration.LogInActivity.TAG;

public class OrderActivity extends AppCompatActivity implements GuestCountDialogOrderActivityInterface.Activity.MyView,
    MenuDialogOrderActivityInterface.View, OrderActivityInterface.View {

    private OrderActivityInterface.Presenter orderPresenter;
    private Consumer<Pair<OrderItem, String>> notifyOrderAdapterConsumer;

    private GuestCountDialogOrderActivityInterface.Activity.Presenter guestsCountDialogPresenter;
    private MenuDialogOrderActivityInterface.Presenter menuDialogPresenter;
    private GuestsCountBottomSheetDialog guestCountDialog;

    private View guestCountDialogView;
    private TextView guestsCount;
    private TextView tableNumberTextView;
    private View menuDialogView;
    private MenuBottomSheetDialog menuDialog;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fba;
    private int tableNumber;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        Dish dish = new Dish();
//        dish.setCost("999r");
//        dish.setWeight("9000g");
//        dish.setCategoryName("category");
//        dish.setName("name");
//        dish.setDescription("description");
//
//        OrderItem orderItem = new OrderItem(dish, "my commentary", 71);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("orders")
//            .document("table_1")
//            .collection("order_items")
//            .document("name + commentary")
//            .set(orderItem).addOnCompleteListener(task -> {
//                if(task.isSuccessful()) {
//                    Log.d(TAG, "Success");
//                }
//                else  Log.d(TAG, task.getException().toString());
//        });

        initialisation();

    }

    private void initialisation () {
        tableNumber = getIntent().getIntExtra(EXTRA_TAG, 0);
        tableNumberTextView = findViewById(R.id.table_number);
        tableNumberTextView.setText(Integer.toString(tableNumber));

        // First - GuestCountDialog, Second - OrderRecyclerView, Third - menuDialog
        guestsCountDialogPresenter = new GuestCountDialogOrderActivityPresenter(this);
        guestsCount = findViewById(R.id.guests_count);
        prepareGuestCountModel();

        guestCountDialog = new GuestsCountBottomSheetDialog(guestCountDialogView);
        new Handler().postDelayed(() -> {
            guestCountDialog.show(getSupportFragmentManager(), "");
        }, 10);

        orderPresenter = new OrderActivityPresenter(this);
        MenuDialogModel model = new MenuDialogModel();
        menuDialogPresenter = new MenuDialogPresenter(this);

        bottomAppBar = findViewById(R.id.bottom_app_bar);
        fba = findViewById(R.id.menu_floating_action_button);
        fba.setOnClickListener(view -> {
            if(menuDialogView != null) // add isExist
                menuDialog.show(getSupportFragmentManager(), "");
        });
        bottomAppBar.setNavigationOnClickListener(view -> {
            // add isExist
            OrderMenuBottomSheetDialog dialog = new OrderMenuBottomSheetDialog();
            dialog.show(getSupportFragmentManager(), "");
        });
    }
    private void prepareGuestCountModel() {
        RecyclerView recyclerView = null;
        GuestsCountRecyclerViewAdapter adapter = null;
        android.view.View view = null;
        Map<String, Object> modelState = guestsCountDialogPresenter.getGuestCountModelState();
        if(modelState.containsValue(null)) {
            view = android.view.View.inflate(this, R.layout.dialog_guests_count, null);
            recyclerView = view.findViewById(R.id.guests_count_recycler_view);
            adapter = new GuestsCountRecyclerViewAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
            modelState.put(GUEST_COUNT_DIALOG_VIEW_KEY, view);
            modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY, recyclerView);
            modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY, adapter);
            guestsCountDialogPresenter.setGuestCountModelState(modelState);
        }
    }
    @Override
    public void setGuestsCount(int guestsCount) {
        guestCountDialog.dismiss();
        this.guestsCount.setText(Integer.toString(guestsCount));
    }
    @Override
    public void setGuestCountDialogView(View view) {
        guestCountDialogView = view;
    }
    @Override
    public void onMenuDialogModelComplete(View menuDialogView, MenuRecyclerViewAdapter adapter) {
        this.menuDialogView = menuDialogView;
        adapter.setFragmentManager(getSupportFragmentManager());
        menuDialog = new MenuBottomSheetDialog(menuDialogView);
        Log.d(TAG, "COMPLETE");
    }
    @Override
    public Pair<View, MenuRecyclerViewAdapter> onMenuDialogDataFillingComplete(MenuDialogOrderActivityInterface.Model model) {
        View view = View.inflate(this, R.layout.dialog_menu, null);
        RecyclerView recyclerView = view.findViewById(R.id.menu_recycler_view);
        MenuRecyclerViewAdapter adapter = new MenuRecyclerViewAdapter(
            getSupportFragmentManager(),
            model.getMenu(),
            model.getCategoryNames(),
            notifyOrderAdapterConsumer,
            tableNumber
        );
        recyclerView.setAdapter(adapter);
        return new Pair<>(view, adapter);
    }
    @Override
    public void onMenuDialogError(int errorCode) {
        if(!ErrorAlertDialog.isIsExist())
            ErrorAlertDialog.getNewInstance(errorCode).show(getSupportFragmentManager(), "");
    }
    @Override
    public void setOrderRecyclerView(RecyclerView orderRecyclerView) {
        CoordinatorLayout coordinatorLayout = findViewById(R.id.order_recycler_view_container);
        coordinatorLayout.removeView(findViewById(R.id.order_recycler_view));
        coordinatorLayout.addView(orderRecyclerView);
    }
    @Override
    public RecyclerView prepareOrderRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.order_recycler_view);
        OrderRecyclerViewAdapter adapter = new OrderRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }
    @Override
    public void setOrderRecyclerViewConsumer(Consumer<Pair<OrderItem, String>> notifyOrderAdapterConsumer) {
        this.notifyOrderAdapterConsumer = notifyOrderAdapterConsumer;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderPresenter.orderRecyclerViewOnActivityDestroy();
    }
}
