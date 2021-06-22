package com.example.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import cook.model.DetailOrderActivityModel;

import dialogsTools.ErrorAlertDialog;
import interfaces.DetailOrderItemsActivityInterface;
import presenters.DetailOrderItemsActivityPresenter;
import tools.EmployeeData;

public class DetailOrderActivity extends AppCompatActivity implements DetailOrderItemsActivityInterface.View {

    private DetailOrderItemsActivityInterface.Presenter presenter;
    private String tableNumber;
    private TextView title;
    private final String TABLE = "Столик ";

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        getPermission();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initialization();
    }
    private void initialization () {
        Intent intent = getIntent();
        tableNumber = intent.getStringExtra(DetailOrderActivityModel.EXTRA_TAG);
        presenter = new DetailOrderItemsActivityPresenter(this);
        title = findViewById(R.id.title);
        title.setText(TABLE + tableNumber);
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
}
