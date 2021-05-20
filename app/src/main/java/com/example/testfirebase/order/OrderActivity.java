package com.example.testfirebase.order;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import java.util.Map;

import dialogs.GuestsCountBottomSheetDialog;
import interfaces.OrderActivityInterface;
import presenters.OrderActivityPresenter;

import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY;
import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY;
import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_VIEW_KEY;

public class OrderActivity extends AppCompatActivity implements OrderActivityInterface.Activity.MyView {

    private OrderActivityInterface.Activity.Presenter presenter;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter = new OrderActivityPresenter(this);
        prepareGuestCountRecyclerView();
        GuestsCountBottomSheetDialog dialog = new GuestsCountBottomSheetDialog(presenter.getGuestCountDialogView());
        dialog.show(getSupportFragmentManager(), "");
    }

    private void prepareGuestCountRecyclerView() {
        RecyclerView recyclerView = null;
        GuestsCountRecyclerViewAdapter adapter = null;
        android.view.View view = null;
        Map<String, Object> modelState = presenter.getModelState();
        if(modelState.containsValue(null)) {
            view = android.view.View.inflate(this, R.layout.dialog_guests_count, null);
            recyclerView = view.findViewById(R.id.guests_count_recycler_view);
            adapter = new GuestsCountRecyclerViewAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            modelState.put(GUEST_COUNT_DIALOG_VIEW_KEY, view);
            modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY, recyclerView);
            modelState.put(GUEST_COUNT_DIALOG_RECYCLER_VIEW_ADAPTER_KEY, adapter);
            presenter.setModelState(modelState);////// View
        }
    }

    @Override
    public void setGuestsCount(int guestsCount) {

    }
}
