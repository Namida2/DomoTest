package com.example.testfirebase.order;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import java.util.Map;

import interfaces.OrderActivityInterface;
import presenters.OrderActivityPresenter;

import static com.example.testfirebase.mainActivityFragments.TablesFragment.EXTRA_TAG;
import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY;
import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY;
import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_VIEW_KEY;

public class OrderActivity extends AppCompatActivity implements OrderActivityInterface.Activity.MyView {

    private OrderActivityInterface.Activity.Presenter presenter;
    private GuestsCountBottomSheetDialog dialog;
    private View guestCountDialogView;
    private TextView guestsCount;
    private TextView tableNumber;

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
        presenter = new OrderActivityPresenter(this);
        guestsCount = findViewById(R.id.guests_count);
        prepareGuestCountRecyclerView();
        dialog = new GuestsCountBottomSheetDialog(guestCountDialogView);
        new Handler().postDelayed(() -> {
            dialog.show(getSupportFragmentManager(), "");
        }, 180);
    }

    private void prepareGuestCountRecyclerView() {
        RecyclerView recyclerView = null;
        GuestsCountRecyclerViewAdapter adapter = null;
        android.view.View view = null;
        Map<String, Object> modelState = presenter.getModelState();
        if(modelState.containsValue(null)) {
            view = android.view.View.inflate(this, R.layout.dialog_guests_count, null);
            recyclerView = view.findViewById(R.id.guests_count_recycler_view);
            adapter = new GuestsCountRecyclerViewAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
            modelState.put(GUEST_COUNT_DIALOG_VIEW_KEY, view);
            modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY, recyclerView);
            modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY, adapter);
            presenter.setModelState(modelState);
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

    private void prepareModel() {

    }

}
