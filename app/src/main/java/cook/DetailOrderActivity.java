package cook;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;

import cook.interfaces.DetailOrderActivityInterface;
import cook.model.DetailOrderActivityModel;
import cook.presenters.DetailOrderActivityPresenter;

public class DetailOrderActivity extends AppCompatActivity implements DetailOrderActivityInterface.View{

    private DetailOrderActivityInterface.Presenter presenter;
    private String tableNumber;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initialization();
        new SetDishReadyDialog().show(getSupportFragmentManager(), "");
    }

    private void initialization () {
        Intent intent = getIntent();
        tableNumber = intent.getStringExtra(DetailOrderActivityModel.EXTRA_TAG);
        presenter = new DetailOrderActivityPresenter(this);
        setViewData();
    }
    @Override
    public void setViewData() {
        RecyclerView recyclerView = findViewById(R.id.order_items_recycler_view);
        recyclerView.setAdapter(presenter.getAdapter(tableNumber));
    }

}
