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
    private TextView guestsCountTextView;
    private TextView tableNumberTextView;
    private View menuDialogView;
    private MenuBottomSheetDialog menuDialog;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fba;

    private int tableNumber;
    private int guestsCount;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialisation();

    }
    private void initialisation () {
        tableNumber = getIntent().getIntExtra(EXTRA_TAG, 0);
        tableNumberTextView = findViewById(R.id.table_number);
        tableNumberTextView.setText(Integer.toString(tableNumber));
        guestsCountTextView = findViewById(R.id.guests_count);

        // First - GuestCountDialog, Second - OrderRecyclerView, Third - menuDialog
        guestsCountDialogPresenter = new GuestCountDialogOrderActivityPresenter(this);
        setGuestCountDialogView();
        guestCountDialog = new GuestsCountBottomSheetDialog(guestCountDialogView);

        new Handler().postDelayed(() -> {
            guestCountDialog.show(getSupportFragmentManager(), "");
        }, 10);

        menuDialogPresenter = new MenuDialogPresenter(this);
        orderPresenter = new OrderActivityPresenter(this, tableNumber);

        bottomAppBar = findViewById(R.id.bottom_app_bar);
        fba = findViewById(R.id.menu_floating_action_button);
        fba.setOnClickListener(view -> {
            if(menuDialogView != null) // add isExist
                menuDialog.show(getSupportFragmentManager(), "");
        });
        bottomAppBar.setNavigationOnClickListener(view -> {
            // add isExist
            OrderMenuBottomSheetDialog dialog = OrderMenuBottomSheetDialog.getNewInstance(orderWasAccepted -> {
                orderPresenter.acceptAndWriteOrderToDb(tableNumber, guestsCount);
            });
            dialog.show(getSupportFragmentManager(), "");
        });
    }

    @Override
    public void setGuestsCountTextView(int guestsCount) {
        this.guestsCount = guestsCount;
        this.guestsCountTextView.setText(Integer.toString(guestsCount));
        guestCountDialog.dismiss();
    }
    @Override
    public void setGuestCountDialogView() {
        View view = View.inflate(this, R.layout.dialog_guests_count, null);
        RecyclerView recyclerView = view.findViewById(R.id.guests_count_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(guestsCountDialogPresenter.getGuestCountAdapter());
        guestCountDialogView = view;
    }
    @Override
    public void onMenuDialogModelComplete(MenuRecyclerViewAdapter adapter) {
        View contentView = View.inflate(this, R.layout.dialog_menu, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.menu_recycler_view);
        adapter.setFragmentManager(getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
        menuDialog = new MenuBottomSheetDialog(contentView);
        this.menuDialogView = contentView;
    }
    @Override
    public MenuRecyclerViewAdapter onMenuDialogDataFillingComplete(MenuDialogOrderActivityInterface.Model model) {
        MenuRecyclerViewAdapter adapter = new MenuRecyclerViewAdapter(
            getSupportFragmentManager(),
            model.getMenu(),
            model.getCategoryNames(),
            notifyOrderAdapterConsumer,
            tableNumber
        );
        return adapter;
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
        orderPresenter.orderRecyclerViewOnActivityDestroy(tableNumber);
    }
}
