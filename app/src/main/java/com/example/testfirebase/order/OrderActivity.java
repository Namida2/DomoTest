package com.example.testfirebase.order;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.adapters.GuestsCountRecyclerViewAdapter;

import java.util.Map;

import interfaces.OrderActivityInterface;
import presenters.GuestCountDialogPresenter;

import static interfaces.OrderActivityInterface.GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY;

public class OrderActivity extends AppCompatActivity implements OrderActivityInterface.MyView {

    private OrderActivityInterface.GuestsCountDialogPresenter presenter;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter = new GuestCountDialogPresenter(this);

    }

    private void prepareGuestCountRecyclerView() {
        RecyclerView recyclerView;
        GuestsCountRecyclerViewAdapter adapter;
        View view;
        Map<String, Object> modelState = presenter.getModelState();
        if (modelState.get(GUEST_COUNT_DIALOG_RECYCLER_VIEW_KEY) == null) {
            recyclerView = findViewById(R.id.guests_count_recycler_view);
        }
        else {
            ///THIS
        }
    }

    @Override
    public void setGuestsCount(int guestsCount) {

    }
}
