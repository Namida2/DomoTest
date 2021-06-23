package com.example.testfirebase.order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.adapters.MenuRecyclerViewAdapter;
import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import dialogsTools.ErrorAlertDialog;
import interfaces.GuestCountDialogOrderActivityInterface;
import interfaces.MenuDialogOrderActivityInterface;
import interfaces.OrderActivityInterface;
import interfaces.ToolsInterface;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import presenters.MenuDialogPresenter;
import presenters.GuestCountDialogOrderActivityPresenter;
import presenters.OrderActivityPresenter;
import tools.EmployeeData;
import tools.Pair;

import static com.example.testfirebase.mainActivityFragments.TablesFragment.EXTRA_TAG;


public class OrderActivity extends AppCompatActivity implements GuestCountDialogOrderActivityInterface.Activity.MyView,
    MenuDialogOrderActivityInterface.View, OrderActivityInterface.View, ToolsInterface.Notifiable {

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getPermission();
        initialisation();

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initialisation () {

        ImageView avatarImageView = findViewById(R.id.guest_avatar);
        int avatarId = (int) (Math.random() * 6);
        switch (avatarId) {
            case 0: avatarImageView.setImageResource(R.drawable.ic_avatar_0); break;
            case 1: avatarImageView.setImageResource(R.drawable.ic_avatar_1); break;
            case 2: avatarImageView.setImageResource(R.drawable.ic_avatar_2); break;
            case 3: avatarImageView.setImageResource(R.drawable.ic_avatar_3); break;
            case 4: avatarImageView.setImageResource(R.drawable.ic_avatar_4); break;
            case 5: avatarImageView.setImageResource(R.drawable.ic_avatar_5); break;
        }

        tableNumber = getIntent().getIntExtra(EXTRA_TAG, 0);
        tableNumberTextView = findViewById(R.id.table_number);
        tableNumberTextView.setText(Integer.toString(tableNumber));
        guestsCountTextView = findViewById(R.id.guests_count);

        guestsCountDialogPresenter = new GuestCountDialogOrderActivityPresenter(this);
        setGuestCountDialogView();
        guestCountDialog = new GuestsCountBottomSheetDialog(guestCountDialogView);
        new Handler().postDelayed(() -> {
            guestCountDialog.show(getSupportFragmentManager(), "");
        }, 10);

        menuDialogPresenter = new MenuDialogPresenter(this);
        orderPresenter = new OrderActivityPresenter(this);
        notifyMe();

        bottomAppBar = findViewById(R.id.bottom_app_bar);
        fba = findViewById(R.id.menu_floating_action_button);
        RxView.clicks(fba)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                if(!getPermission()) return;
                if(menuDialogView != null)
                    menuDialog.show(getSupportFragmentManager(), "");
            });
        bottomAppBar.setNavigationOnClickListener(view -> { // add isExist
            OrderMenuBottomSheetDialog dialog = OrderMenuBottomSheetDialog.getNewInstance(orderWasAccepted -> {
                if(!getPermission()) return;
                orderPresenter.acceptAndWriteOrderToDb(tableNumber, guestsCount);
                finish();
            });
            dialog.show(getSupportFragmentManager(), "");
        });
    }
    //----------GUESTS_COUNT
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
    //----------MENU
    @Override
    public void onMenuDialogModelComplete(MenuRecyclerViewAdapter adapter) {
        View contentView = View.inflate(this, R.layout.dialog_menu, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.menu_recycler_view);
        recyclerView.setAdapter(adapter);
        menuDialog = new MenuBottomSheetDialog(contentView);
        this.menuDialogView = contentView;
    }
    @Override
    public void onMenuDialogError(int errorCode) {
        if(!ErrorAlertDialog.isIsExist())
            ErrorAlertDialog.getNewInstance(errorCode).show(getSupportFragmentManager(), "");
    }
    @Override
    public void showMenuItemDishDialog(Dish dish) {
        AddDishAlertDialog dialog = AddDishAlertDialog.getNewInstance( orderItem -> {
            orderPresenter.notifyAdapterDataSetChanged(orderItem);
        }, dish, accept -> {
            menuDialogPresenter.resetItemIsPressed();
        });
        dialog.show(getSupportFragmentManager(), "");
    }
    //----------ORDER
    @Override
    public void notifyMe() {
        OrderRecyclerViewAdapter orderRecyclerViewAdapter = orderPresenter.getOrderRecyclerViewAdapter(tableNumber);
        if(orderRecyclerViewAdapter != null) {
            RecyclerView recyclerView = findViewById(R.id.order_recycler_view);
            recyclerView.setAdapter(orderRecyclerViewAdapter);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        menuDialogPresenter.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        menuDialogPresenter.onDestroy();
        menuDialog = null;
        guestCountDialog = null;
        guestCountDialogView = null;
        menuDialogPresenter = null;
        guestsCountDialogPresenter = null;
        orderPresenter = null;
    }
    public boolean getPermission () {
        if(!EmployeeData.permission && !ErrorAlertDialog.isIsExist()) {
            ErrorAlertDialog dialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.PERMISSION_ERROR);
            dialog.setActionConsumer(finish -> {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finishAndRemoveTask();
            });
            dialog.show(getSupportFragmentManager(), "");
            return false;
        }
        return true;
    }
    @Override
    public void showEditOrderItemDialog(OrderItem orderItem) {
        if(!EditOrderDialog.isExist()) {
            EditOrderDialog dialog = EditOrderDialog.getNewInstance(orderItem);
            dialog.setNotifyOrderDataChangedConsumer(newOrderItem -> {
                orderPresenter.notifyOrderItemDataSetChanged(orderItem);
            });
            dialog.setRemoveOrderItemConsumer(removableOrderItem -> {
                orderPresenter.removeOrderItem(orderItem);
            });
            dialog.show(getSupportFragmentManager(), "");
        }
    }
}
