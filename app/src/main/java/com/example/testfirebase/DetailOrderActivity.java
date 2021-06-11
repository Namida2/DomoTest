package com.example.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.recyclerview.widget.RecyclerView;

import cook.model.DetailOrderActivityModel;

import interfaces.DetailOrderItemsActivityInterface;
import presenters.DetailOrderItemsActivityPresenter;

public class DetailOrderActivity extends AppCompatActivity implements DetailOrderItemsActivityInterface.View {

    private DetailOrderItemsActivityInterface.Presenter presenter;
    private String tableNumber;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initialization();
    }
    private void initialization () {
        Intent intent = getIntent();
        tableNumber = intent.getStringExtra(DetailOrderActivityModel.EXTRA_TAG);
        presenter = new DetailOrderItemsActivityPresenter(this);
        setViewData();
    }
    @Override
    public void setViewData() {
        RecyclerView recyclerView = findViewById(R.id.order_items_recycler_view);
        recyclerView.setAdapter(presenter.getAdapter(tableNumber));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
